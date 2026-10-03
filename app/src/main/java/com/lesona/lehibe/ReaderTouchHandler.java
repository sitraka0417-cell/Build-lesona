package com.lesona.lehibe;

import android.content.Context;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Arbitre unique des gestes de la page de lecture.
 *
 * Trois gestes se disputent le même TextView :
 *   1. le swipe horizontal (page suivante / précédente),
 *   2. le tap sur un verset cliquable (ClickableSpan),
 *   3. l'appui long + glissement (sélection pour le surlignage).
 *
 * Chaque geste est décidé UNE fois, dans une machine à états, puis les
 * autres sont ignorés jusqu'à la fin du geste (ACTION_UP / ACTION_CANCEL) :
 *
 *   PENDING   : le doigt vient de toucher, rien n'est encore décidé
 *   SWIPING   : mouvement nettement horizontal -> on change de page
 *   SCROLLING : mouvement nettement vertical   -> le ScrollView défile
 *   SELECTING : appui long (ou sélection existante) -> le TextView gère la
 *               sélection et personne d'autre ne la lui vole
 *
 * Deux modes :
 *   - spansEnabled = false : simple relais du swipe (ScrollView, cadres...)
 *   - spansEnabled = true  : TextView avec versets cliquables / sélection
 */
public class ReaderTouchHandler implements View.OnTouchListener {

    public interface PageNavigator {
        void onNextPage();
        void onPreviousPage();
    }

    private static final int IDLE = 0;
    private static final int PENDING = 1;
    private static final int SWIPING = 2;
    private static final int SCROLLING = 3;
    private static final int SELECTING = 4;

    private final boolean spansEnabled;
    private final float density;
    private final float claimDistance;   // distance avant de décider swipe / scroll
    private final float tapSlop;         // tolérance de mouvement pour un tap
    private final long longPressTimeout;
    private final GestureDetector swipeDetector;

    private PageNavigator navigator;

    private int state = IDLE;
    private float downX;
    private float downY;
    private long downTime;
    private boolean hadSelectionAtDown;
    private boolean cancelSentToView;

    public ReaderTouchHandler(Context context, boolean spansEnabled) {
        this.spansEnabled = spansEnabled;
        this.density = context.getResources().getDisplayMetrics().density;
        this.claimDistance = 16f * density;
        this.tapSlop = Math.max(12f * density,
                ViewConfiguration.get(context).getScaledTouchSlop());
        this.longPressTimeout = ViewConfiguration.getLongPressTimeout();

        final float d = density;
        this.swipeDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                                           float vx, float vy) {
                        if (e1 == null || e2 == null || navigator == null) return false;
                        float dy = Math.abs(e1.getY() - e2.getY());
                        float dx = e1.getX() - e2.getX();
                        float adx = Math.abs(dx);
                        // Le geste doit être nettement plus horizontal que vertical.
                        if (adx < dy * 1.5f) return false;
                        if (dy > 90f * d) return false;
                        if (dx > 24f * d && Math.abs(vx) > 40) {
                            navigator.onNextPage();
                            return true;
                        }
                        if (-dx > 24f * d && Math.abs(vx) > 40) {
                            navigator.onPreviousPage();
                            return true;
                        }
                        return false;
                    }
                });
    }

    public void setNavigator(PageNavigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        // Les zones de saisie (notes) gardent leur comportement natif.
        if (v instanceof EditText) return false;

        // Mode relais : on observe seulement le swipe, la vue fait le reste.
        if (!spansEnabled || !(v instanceof TextView)) {
            swipeDetector.onTouchEvent(ev);
            return false;
        }

        final TextView tv = (TextView) v;
        final boolean selectable = tv.isTextSelectable();

        switch (ev.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                state = PENDING;
                downX = ev.getX();
                downY = ev.getY();
                downTime = ev.getEventTime();
                cancelSentToView = false;
                hadSelectionAtDown = selectable && hasSelection(tv);
                swipeDetector.onTouchEvent(ev);
                // Empêche le ScrollView de voler le geste avant qu'on sache
                // s'il est horizontal, vertical ou une sélection.
                disallowParentIntercept(v, true);
                // Texte sélectionnable : le TextView doit voir le DOWN pour
                // pouvoir démarrer sa sélection. Sinon on garde la séquence.
                return !selectable;

            case MotionEvent.ACTION_MOVE:
                if (state == PENDING) decideGesture(v, tv, selectable, ev);
                switch (state) {
                    case SWIPING:
                        swipeDetector.onTouchEvent(ev);
                        return true;
                    case SCROLLING:
                    case SELECTING:
                        return false;
                    default: // PENDING
                        swipeDetector.onTouchEvent(ev);
                        return !selectable;
                }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                final int endState = state;
                state = IDLE;
                disallowParentIntercept(v, false);

                if (endState == SWIPING) {
                    // Le fling déclenche (ou non) le changement de page.
                    swipeDetector.onTouchEvent(ev);
                    return true;
                }
                if (endState == SCROLLING || endState == SELECTING) {
                    return false;
                }

                swipeDetector.onTouchEvent(ev);

                // Tap sur un verset : court, immobile, et pas un appui long,
                // sinon on volerait la sélection (surlignage d'une référence).
                if (ev.getActionMasked() == MotionEvent.ACTION_UP
                        && !hadSelectionAtDown) {
                    boolean quick = (ev.getEventTime() - downTime) < longPressTimeout;
                    boolean still = Math.abs(ev.getX() - downX) <= tapSlop
                            && Math.abs(ev.getY() - downY) <= tapSlop;
                    if (quick && still) {
                        ClickableSpan span = findSpanAt(tv, ev.getX(), ev.getY());
                        if (span != null) {
                            if (selectable) {
                                // Nettoie l'état "appui long en attente" du TextView.
                                sendCancel(v, ev);
                            }
                            span.onClick(tv);
                            return true;
                        }
                    }
                }
                return !selectable;
            }

            default:
                return state == SWIPING || (!selectable && state != IDLE);
        }
    }

    // ── Décision du geste (une seule fois par séquence) ──────────────
    private void decideGesture(View v, TextView tv, boolean selectable, MotionEvent ev) {
        final float dx = ev.getX() - downX;
        final float dy = ev.getY() - downY;
        final float adx = Math.abs(dx);
        final float ady = Math.abs(dy);

        // Appui long sur un texte sélectionnable -> sélection : on s'efface.
        if (selectable) {
            boolean longPressed = (ev.getEventTime() - downTime) >= longPressTimeout;
            boolean selectionStarted = !hadSelectionAtDown && hasSelection(tv);
            if (longPressed || selectionStarted) {
                state = SELECTING;
                cancelDetector(ev);
                disallowParentIntercept(v, true); // ni scroll ni swipe pendant la sélection
                return;
            }
        }

        if (adx > claimDistance && adx > ady * 1.3f) {
            state = SWIPING;
            if (!cancelSentToView) {
                cancelSentToView = true;
                sendCancel(v, ev); // le TextView abandonne son appui / sélection naissante
            }
        } else if (ady > claimDistance && ady >= adx) {
            state = SCROLLING;
            cancelDetector(ev);
            disallowParentIntercept(v, false); // le ScrollView peut défiler
        }
    }

    // ── Détection d'un ClickableSpan à la position touchée ───────────
    // N'utilise pas getOffsetForHorizontal (imprécis avec le texte justifié) :
    // compare le X touché à la position réelle du span (getPrimaryHorizontal).
    private ClickableSpan findSpanAt(TextView tv, float evX, float evY) {
        CharSequence cs = tv.getText();
        if (!(cs instanceof Spanned)) return null;
        Spanned buffer = (Spanned) cs;
        Layout layout = tv.getLayout();
        if (layout == null) return null;

        float x = evX - tv.getTotalPaddingLeft() + tv.getScrollX();
        float y = evY - tv.getTotalPaddingTop() + tv.getScrollY();
        if (y < 0 || y > layout.getHeight()) return null;
        int line = layout.getLineForVertical((int) y);

        ClickableSpan[] spans = buffer.getSpans(0, buffer.length(), ClickableSpan.class);
        ClickableSpan best = null;
        float bestDist = Float.MAX_VALUE;
        final float tolerance = 14f * density;

        for (ClickableSpan span : spans) {
            int spStart = buffer.getSpanStart(span);
            int spEnd = buffer.getSpanEnd(span);
            int lineStart = layout.getLineForOffset(spStart);
            int lineEnd = layout.getLineForOffset(spEnd);
            if (line < lineStart || line > lineEnd) continue;

            float left = (lineStart == line)
                    ? layout.getPrimaryHorizontal(spStart) : layout.getLineLeft(line);
            float right = (lineEnd == line)
                    ? layout.getPrimaryHorizontal(spEnd) : layout.getLineRight(line);
            if (left > right) {
                float t = left;
                left = right;
                right = t;
            }
            if (x >= left && x <= right) {
                return span; // le point est exactement dans le span
            }
            float dist = Math.min(Math.abs(x - left), Math.abs(x - right));
            if (dist < bestDist && dist <= tolerance) {
                bestDist = dist;
                best = span;
            }
        }
        return best;
    }

    // ── Utilitaires ──────────────────────────────────────────────────
    private static boolean hasSelection(TextView tv) {
        return tv.getSelectionStart() != tv.getSelectionEnd();
    }

    private static void disallowParentIntercept(View v, boolean disallow) {
        ViewParent p = v.getParent();
        if (p != null) p.requestDisallowInterceptTouchEvent(disallow);
    }

    private static void sendCancel(View v, MotionEvent ev) {
        MotionEvent cancel = MotionEvent.obtain(ev);
        cancel.setAction(MotionEvent.ACTION_CANCEL);
        v.onTouchEvent(cancel);
        cancel.recycle();
    }

    private void cancelDetector(MotionEvent ev) {
        MotionEvent cancel = MotionEvent.obtain(ev);
        cancel.setAction(MotionEvent.ACTION_CANCEL);
        swipeDetector.onTouchEvent(cancel);
        cancel.recycle();
    }
}
