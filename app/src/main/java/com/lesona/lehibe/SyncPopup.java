package com.lesona.lehibe;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Popup de téléchargement des nouvelles leçons.
 *
 * États :
 *   - téléchargement : titre "An-dalam-panavaozana", anneau bleu + %, bouton "Ajanona"
 *   - erreur         : "Misy olana ny fifandraisana", boutons "Anandrana indray" / "Ajanona"
 *   - terminé        : "Vita ny fanavaozana", anneau vert + coche, bouton "Akatona"
 *   - rien de neuf   : "Mbola tsy misy lesona vaovao", bouton "Akatona"
 *
 * Toutes les méthodes publiques peuvent être appelées depuis n'importe quel thread.
 * Compatible Android 5.0+ (API 21) : aucune API plus récente n'est utilisée.
 */
public class SyncPopup {

    public interface Listener {
        /** "Anandrana indray" : relancer le téléchargement. */
        void onRetry();
        /** "Ajanona" (ou touche retour) : interrompre le téléchargement. */
        void onCancel();
    }

    private static final int COLOR_CARD = 0xFF1B1C28;
    private static final int COLOR_CARD_BORDER = 0xFF2C2F44;
    private static final int COLOR_TRACK = 0xFF2B2D40;
    private static final int COLOR_BLUE = 0xFF4DA3FF;
    private static final int COLOR_GREEN = 0xFF3ECF8E;
    private static final int COLOR_RED = 0xFFE5534B;
    private static final int COLOR_BTN = 0xFF1F2030;
    private static final int COLOR_BTN_PRESSED = 0xFF2C2E45;
    private static final int COLOR_BTN_BORDER = 0xFF3A3D57;

    private final Activity activity;
    private final Handler ui = new Handler(Looper.getMainLooper());
    private final float density;

    private Listener listener;
    private Dialog dialog;
    private TextView titleView;
    private FrameLayout ringBox;
    private RingView ring;
    private TextView percentView;
    private LinearLayout buttons;

    public SyncPopup(Activity activity) {
        this.activity = activity;
        this.density = activity.getResources().getDisplayMetrics().density;
    }

    public void setListener(Listener l) {
        this.listener = l;
    }

    // ── API publique (thread-safe) ────────────────────────────────────

    /** Affiche le popup en mode téléchargement (anneau qui tourne, puis % réel). */
    public void showProgress() {
        post(new Runnable() {
            @Override public void run() {
                ensureDialog();
                renderProgress();
                if (!dialog.isShowing()) dialog.show();
                fitWindow();
            }
        });
    }

    /** Met à jour le pourcentage (0-100). Passe de l'anneau "indéterminé" au % réel. */
    public void setProgress(final int percent) {
        post(new Runnable() {
            @Override public void run() {
                if (dialog == null || !dialog.isShowing()) return;
                ring.setProgress(percent);
                percentView.setText(Math.max(0, Math.min(100, percent)) + "%");
            }
        });
    }

    /** Problème réseau : message + "Anandrana indray" / "Ajanona". */
    public void showError() {
        post(new Runnable() {
            @Override public void run() {
                if (dialog == null || !dialog.isShowing()) return;
                titleView.setText("Misy olana ny fifandraisana");
                titleView.setTextColor(COLOR_RED);
                ringBox.setVisibility(View.GONE);
                buttons.removeAllViews();
                addButton("Anandrana indray", COLOR_BLUE, new Runnable() {
                    @Override public void run() {
                        if (listener != null) listener.onRetry();
                        renderProgress();
                    }
                });
                addButton("Ajanona", COLOR_BTN_BORDER, new Runnable() {
                    @Override public void run() {
                        dismissWithCancel();
                    }
                });
            }
        });
    }

    /** Téléchargement réussi : anneau vert, "Vita ny fanavaozana", "Akatona". */
    public void showDone() {
        post(new Runnable() {
            @Override public void run() {
                if (dialog == null || !dialog.isShowing()) return;
                titleView.setText("Vita ny fanavaozana");
                titleView.setTextColor(COLOR_GREEN);
                ringBox.setVisibility(View.VISIBLE);
                ring.setDone();
                percentView.setVisibility(View.GONE);
                buttons.removeAllViews();
                addButton("Akatona", COLOR_GREEN, new Runnable() {
                    @Override public void run() {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    /** Rien de nouveau sur GitHub : message + "Akatona". */
    public void showNothingNew() {
        post(new Runnable() {
            @Override public void run() {
                if (dialog == null || !dialog.isShowing()) return;
                titleView.setText("Mbola tsy misy lesona vaovao");
                titleView.setTextColor(Color.WHITE);
                ringBox.setVisibility(View.GONE);
                buttons.removeAllViews();
                addButton("Akatona", COLOR_BTN_BORDER, new Runnable() {
                    @Override public void run() {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void dismiss() {
        post(new Runnable() {
            @Override public void run() {
                if (dialog != null && dialog.isShowing()) {
                    try { dialog.dismiss(); } catch (Exception ignored) {}
                }
            }
        });
    }

    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }

    // ── Construction de l'interface ───────────────────────────────────

    private void post(final Runnable r) {
        if (activity.isFinishing()) return;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            try { r.run(); } catch (RuntimeException ignored) {}
        } else {
            ui.post(new Runnable() {
                @Override public void run() {
                    if (activity.isFinishing()) return;
                    try { r.run(); } catch (RuntimeException ignored) {}
                }
            });
        }
    }

    private void renderProgress() {
        titleView.setText("An-dalam-panavaozana");
        titleView.setTextColor(Color.WHITE);
        ringBox.setVisibility(View.VISIBLE);
        ring.setIndeterminate();
        percentView.setVisibility(View.VISIBLE);
        percentView.setText("0%");
        buttons.removeAllViews();
        addButton("Ajanona", COLOR_BTN_BORDER, new Runnable() {
            @Override public void run() {
                dismissWithCancel();
            }
        });
    }

    private void dismissWithCancel() {
        if (listener != null) listener.onCancel();
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    private void ensureDialog() {
        if (dialog != null) return;

        final Context c = activity;

        LinearLayout card = new LinearLayout(c);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER_HORIZONTAL);
        card.setPadding(dp(24), dp(28), dp(24), dp(24));
        GradientDrawable cardBg = new GradientDrawable();
        cardBg.setColor(COLOR_CARD);
        cardBg.setCornerRadius(dp(26));
        cardBg.setStroke(Math.max(1, dp(1)), COLOR_CARD_BORDER);
        card.setBackground(cardBg);

        titleView = new TextView(c);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        titleView.setTypeface(Typeface.DEFAULT_BOLD);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams titleLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleLp.bottomMargin = dp(22);
        card.addView(titleView, titleLp);

        ringBox = new FrameLayout(c);
        ring = new RingView(c, density);
        ringBox.addView(ring, new FrameLayout.LayoutParams(dp(108), dp(108)));
        // le % est dans le FrameLayout pour rester centré dans l'anneau
        percentView = new TextView(c);
        percentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        percentView.setTypeface(Typeface.DEFAULT_BOLD);
        percentView.setTextColor(Color.WHITE);
        percentView.setGravity(Gravity.CENTER);
        ringBox.addView(percentView, new FrameLayout.LayoutParams(
                dp(108), dp(108), Gravity.CENTER));
        LinearLayout.LayoutParams ringLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ringLp.bottomMargin = dp(22);
        card.addView(ringBox, ringLp);

        buttons = new LinearLayout(c);
        buttons.setOrientation(LinearLayout.VERTICAL);
        buttons.setGravity(Gravity.CENTER_HORIZONTAL);
        card.addView(buttons, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(card);
        dialog.setCancelable(true);               // touche retour = "Ajanona"
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {
            @Override public void onCancel(android.content.DialogInterface d) {
                if (listener != null) listener.onCancel();
            }
        });
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setDimAmount(0.72f);
        }
    }

    private void fitWindow() {
        if (dialog == null || dialog.getWindow() == null) return;
        int w = activity.getResources().getDisplayMetrics().widthPixels;
        dialog.getWindow().setLayout((int) (w * 0.86f), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void addButton(String label, int strokeColor, final Runnable action) {
        TextView b = new TextView(activity);
        b.setText(label);
        b.setTextColor(Color.WHITE);
        b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        b.setTypeface(Typeface.DEFAULT_BOLD);
        b.setGravity(Gravity.CENTER);
        b.setSingleLine(true);
        b.setMinWidth(dp(190));
        b.setMinHeight(dp(48));
        b.setPadding(dp(24), dp(10), dp(24), dp(10));
        b.setClickable(true);
        b.setFocusable(true);

        GradientDrawable normal = new GradientDrawable();
        normal.setColor(COLOR_BTN);
        normal.setCornerRadius(dp(16));
        normal.setStroke(Math.max(2, Math.round(1.5f * density)), strokeColor);
        GradientDrawable pressed = new GradientDrawable();
        pressed.setColor(COLOR_BTN_PRESSED);
        pressed.setCornerRadius(dp(16));
        pressed.setStroke(Math.max(2, Math.round(1.5f * density)), strokeColor);
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, pressed);
        states.addState(new int[]{}, normal);
        b.setBackground(states);

        b.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                action.run();
            }
        });

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp(10);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        buttons.addView(b, lp);
    }

    private int dp(int v) {
        return Math.round(v * density);
    }

    // ── Anneau de progression ─────────────────────────────────────────

    private static class RingView extends View {
        private final Paint track = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint arc = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint tick = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final RectF rect = new RectF();
        private final Path tickPath = new Path();
        private final float stroke;

        private int percent = 0;
        private boolean indeterminate = true;
        private boolean done = false;

        RingView(Context c, float density) {
            super(c);
            stroke = 8f * density;
            track.setStyle(Paint.Style.STROKE);
            track.setStrokeWidth(stroke);
            track.setColor(COLOR_TRACK);
            arc.setStyle(Paint.Style.STROKE);
            arc.setStrokeWidth(stroke);
            arc.setStrokeCap(Paint.Cap.ROUND);
            arc.setColor(COLOR_BLUE);
            tick.setStyle(Paint.Style.STROKE);
            tick.setStrokeWidth(stroke);
            tick.setStrokeCap(Paint.Cap.ROUND);
            tick.setStrokeJoin(Paint.Join.ROUND);
            tick.setColor(COLOR_GREEN);
        }

        void setIndeterminate() {
            indeterminate = true;
            done = false;
            percent = 0;
            arc.setColor(COLOR_BLUE);
            invalidate();
        }

        void setProgress(int p) {
            indeterminate = false;
            done = false;
            percent = Math.max(0, Math.min(100, p));
            arc.setColor(COLOR_BLUE);
            invalidate();
        }

        void setDone() {
            indeterminate = false;
            done = true;
            percent = 100;
            arc.setColor(COLOR_GREEN);
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            float half = stroke / 2f;
            rect.set(half, half, getWidth() - half, getHeight() - half);
            canvas.drawOval(rect, track);

            if (done) {
                canvas.drawArc(rect, -90f, 360f, false, arc);
                float w = getWidth();
                float h = getHeight();
                tickPath.reset();
                tickPath.moveTo(w * 0.30f, h * 0.52f);
                tickPath.lineTo(w * 0.45f, h * 0.66f);
                tickPath.lineTo(w * 0.71f, h * 0.37f);
                canvas.drawPath(tickPath, tick);
            } else if (indeterminate) {
                float start = (SystemClock.uptimeMillis() % 1100L) / 1100f * 360f;
                canvas.drawArc(rect, start, 80f, false, arc);
                postInvalidateOnAnimation();
            } else if (percent > 0) {
                canvas.drawArc(rect, -90f, 360f * percent / 100f, false, arc);
            }
        }
    }
}
