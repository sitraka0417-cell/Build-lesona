package com.lesona.lehibe;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.*;
import android.content.Intent;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.json.*;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.Gravity;
import android.view.View;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.content.res.AssetManager;
import java.io.IOException;

public class MainActivity extends Activity {
	
	private Intent i = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
	}
	
	private void initializeLogic() {
		final float D = getResources().getDisplayMetrics().density;
		
		final int C_BG       = 0xFF0c0e14;
		final int C_SURFACE  = 0xFF141720;
		final int C_SURFACE2 = 0xFF1c2033;
		final int C_GOLD     = 0xFFd4a853;
		final int C_GOLD_DIM = 0xFF7a5e28;
		final int C_TEXT     = 0xFFf0eff4;
		final int C_MUTED    = 0xFF7b829a;
		final int C_BORDER   = 0xFF252a3d;
		final int C_GREEN    = 0xFF3ecf8e;
		final int C_ACCENT   = 0xFF5b8def;
		final int C_RED      = 0xFFe5534b;
		
		final String[][] MOIS_TRIMESTRE = {
			    {"Janoary","Febroary","Marsa"},
			    {"Aprily","Mey","Jona"},
			    {"Jolay","Aogositra","Septambra"},
			    {"Oktobra","Novambra","Desambra"}
		};
		final String[] NOM_TRIMESTRE = {
			    "Telovolana voalohany","Telovolana faharoa",
			    "Telovolana fahatelo","Telovolana fahefatra"
		};
		
		java.util.Calendar calToday = java.util.Calendar.getInstance();
		final int todayDay   = calToday.get(java.util.Calendar.DAY_OF_MONTH);
		final int todayMonth = calToday.get(java.util.Calendar.MONTH)+1;
		final int todayYear  = calToday.get(java.util.Calendar.YEAR);
		final String todayFilename = String.format("%02d_%02d_%04d",
		    todayDay, todayMonth, todayYear);
		
		final AssetManager am = getAssets();
		final java.io.File fDir = getFilesDir();
		
		final String[][] hybridResult = {null};
		final String[]   hybridPath   = {null};
		final Runnable hybridList = new Runnable() {
			    @Override public void run() {
				        String path = hybridPath[0];
				        java.util.LinkedHashSet<String> names =
				            new java.util.LinkedHashSet<String>();
				        try {
					            String[] a = am.list(path);
					            if (a != null) for (String s : a) names.add(s);
					        } catch (Exception e) {}
				        java.io.File dir = new java.io.File(fDir, path);
				        if (dir.exists() && dir.isDirectory()) {
					            String[] f = dir.list();
					            if (f != null) for (String s : f) names.add(s);
					        }
				        hybridResult[0] = names.toArray(new String[0]);
				    }
		};
		
		final java.util.Comparator<String> dateComparator =
		    new java.util.Comparator<String>() {
			        @Override public int compare(String a, String b) {
				            return dk(a)-dk(b);
				        }
			        private int dk(String n) {
				            try {
					                String s = n.replaceAll("\\.(txt|html)","");
					                String[] p = s.split("_");
					                if (p.length==3)
					                    return Integer.parseInt(p[2])*10000
					                         + Integer.parseInt(p[1])*100
					                         + Integer.parseInt(p[0]);
					            } catch (Exception e) {}
				            return 0;
				        }
			    };
		
		final int[]    navLevel    = {0};
		final String[] selTaona    = {null};
		final String[] selTrimPath = {null};
		final int[]    selTrimIdx  = {-1};
		final String[] foundLessonPath = {null};
		final String[] foundLessonFile = {null};
		
		final String[] toastMsgArg   = {null};
		final int[]    toastColorArg = {0};
		final Runnable showColoredToast = new Runnable() {
			    @Override public void run() {
				        Toast t = new Toast(MainActivity.this);
				        TextView tv = new TextView(MainActivity.this);
				        tv.setText(toastMsgArg[0]);
				        tv.setTextColor(0xFFFFFFFF);
				        tv.setTextSize(14);
				        tv.setTypeface(Typeface.DEFAULT_BOLD);
				        tv.setPadding((int)(20*D),(int)(12*D),(int)(20*D),(int)(12*D));
				        GradientDrawable tbg = new GradientDrawable();
				        tbg.setColor(toastColorArg[0]);
				        tbg.setCornerRadius(30*D);
				        tv.setBackground(tbg);
				        t.setView(tv);
				        t.setDuration(Toast.LENGTH_SHORT);
				        t.setGravity(Gravity.BOTTOM,0,(int)(110*D));
				        t.show();
				    }
		};
		
		// ── Toast persistant "Eo am-panavaozana..." ────────────────────
		final Toast[] persistentSyncToast = {null};
		final Handler syncToastHandler = new Handler(android.os.Looper.getMainLooper());
		final boolean[] syncToastActive = {false};
		final Runnable[] syncToastLoop = new Runnable[1];
		syncToastLoop[0] = new Runnable() {
			    @Override public void run() {
				        if (!syncToastActive[0]) return;
				        if (persistentSyncToast[0] != null) persistentSyncToast[0].cancel();
				
				        Toast t = new Toast(MainActivity.this);
				        TextView tv = new TextView(MainActivity.this);
				        tv.setText("Eo am-panavaozana...");
				        tv.setTextColor(0xFFFFFFFF);
				        tv.setTextSize(14);
				        tv.setTypeface(Typeface.DEFAULT_BOLD);
				        tv.setPadding((int)(20*D),(int)(12*D),(int)(20*D),(int)(12*D));
				        GradientDrawable tbg = new GradientDrawable();
				        tbg.setColor(C_ACCENT);
				        tbg.setCornerRadius(30*D);
				        tv.setBackground(tbg);
				        t.setView(tv);
				        t.setDuration(Toast.LENGTH_LONG);
				        t.setGravity(Gravity.BOTTOM,0,(int)(110*D));
				        t.show();
				        persistentSyncToast[0] = t;
				
				        syncToastHandler.postDelayed(syncToastLoop[0], 3200);
				    }
		};
		final Runnable startPersistentSyncToast = new Runnable() {
			    @Override public void run() {
				        if (syncToastActive[0]) return;
				        syncToastActive[0] = true;
				        syncToastLoop[0].run();
				    }
		};
		final Runnable stopPersistentSyncToast = new Runnable() {
			    @Override public void run() {
				        syncToastActive[0] = false;
				        syncToastHandler.removeCallbacks(syncToastLoop[0]);
				        if (persistentSyncToast[0] != null) {
					            persistentSyncToast[0].cancel();
					            persistentSyncToast[0] = null;
					        }
				    }
		};
		
		LinearLayout root = new LinearLayout(this);
		root.setOrientation(LinearLayout.VERTICAL);
		root.setBackgroundColor(C_BG);
		setContentView(root);
		
		final LinearLayout header = new LinearLayout(this);
		header.setOrientation(LinearLayout.VERTICAL);
		header.setBackgroundColor(C_SURFACE);
		header.setPadding((int)(20*D),(int)(20*D),(int)(20*D),(int)(18*D));
		
		LinearLayout headerTop = new LinearLayout(this);
		headerTop.setOrientation(LinearLayout.HORIZONTAL);
		headerTop.setGravity(Gravity.CENTER_VERTICAL);
		
		final android.widget.ImageView infoIcon = new android.widget.ImageView(this);
		try {
			    infoIcon.setImageBitmap(android.graphics.BitmapFactory
			        .decodeStream(getAssets().open("info.png")));
		} catch (Exception e) {}
		infoIcon.setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
		GradientDrawable infoBg = new GradientDrawable();
		infoBg.setColor(0xFF1e2435);
		infoBg.setCornerRadius(12*D);
		infoIcon.setBackground(infoBg);
		infoIcon.setPadding((int)(8*D),(int)(8*D),(int)(8*D),(int)(8*D));
		infoIcon.setClickable(true);
		infoIcon.setFocusable(true);
		int infoSize = (int)(38*D);
		headerTop.addView(infoIcon, new LinearLayout.LayoutParams(infoSize, infoSize));
		
		LinearLayout titleBlock = new LinearLayout(this);
		titleBlock.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams tbLp = new LinearLayout.LayoutParams(
		    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
		tbLp.setMargins((int)(12*D),0,0,0);
		
		final TextView toolbarTitle = new TextView(this);
		toolbarTitle.setText("Lesona Kilasy Lehibe");
		toolbarTitle.setTextColor(C_TEXT);
		toolbarTitle.setTextSize(17);
		toolbarTitle.setTypeface(Typeface.DEFAULT_BOLD);
		titleBlock.addView(toolbarTitle);
		
		final TextView subtitleView = new TextView(this);
		subtitleView.setTextColor(C_MUTED);
		subtitleView.setTextSize(12);
		LinearLayout.LayoutParams subLp = new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.WRAP_CONTENT,
		    LinearLayout.LayoutParams.WRAP_CONTENT);
		// FIX espacement demandé : plus d'air sous le titre
		subLp.topMargin = (int)(6*D);
		titleBlock.addView(subtitleView, subLp);
		headerTop.addView(titleBlock, tbLp);
		
		final TextView btnHeaderBack = new TextView(this);
		btnHeaderBack.setText("‹");
		btnHeaderBack.setTextColor(C_GOLD);
		btnHeaderBack.setTextSize(26);
		btnHeaderBack.setTypeface(Typeface.DEFAULT_BOLD);
		btnHeaderBack.setGravity(Gravity.CENTER);
		btnHeaderBack.setPadding((int)(6*D),0,(int)(6*D),0);
		btnHeaderBack.setVisibility(View.GONE);
		btnHeaderBack.setClickable(true);
		btnHeaderBack.setFocusable(true);
		headerTop.addView(btnHeaderBack, new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.WRAP_CONTENT,
		    LinearLayout.LayoutParams.WRAP_CONTENT));
		
		final android.widget.ImageView updateIcon = new android.widget.ImageView(this);
		try {
			    updateIcon.setImageBitmap(android.graphics.BitmapFactory
			        .decodeStream(getAssets().open("down.png")));
		} catch (Exception e) {}
		updateIcon.setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
		GradientDrawable updBg = new GradientDrawable();
		updBg.setColor(0xFF1e2435);
		updBg.setCornerRadius(12*D);
		updateIcon.setBackground(updBg);
		updateIcon.setPadding((int)(9*D),(int)(9*D),(int)(9*D),(int)(9*D));
		updateIcon.setClickable(true);
		updateIcon.setFocusable(true);
		LinearLayout.LayoutParams updLp = new LinearLayout.LayoutParams(infoSize, infoSize);
		updLp.setMargins((int)(8*D),0,0,0);
		headerTop.addView(updateIcon, updLp);
		
		header.addView(headerTop);
		
		final View headerSepView = new View(this);
		headerSepView.setBackgroundColor(C_BORDER);
		LinearLayout.LayoutParams sepHLp = new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.MATCH_PARENT,(int)(1*D));
		// FIX espacement demandé : 18 -> 24
		sepHLp.topMargin = (int)(24*D);
		header.addView(headerSepView, sepHLp);
		
		final LinearLayout statsBar = new LinearLayout(this);
		statsBar.setOrientation(LinearLayout.HORIZONTAL);
		statsBar.setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams statsLp = new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.MATCH_PARENT,
		    LinearLayout.LayoutParams.WRAP_CONTENT);
		// FIX espacement demandé : 10 -> 16
		statsLp.topMargin = (int)(16*D);
		header.addView(statsBar, statsLp);
		root.addView(header, new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.MATCH_PARENT,
		    LinearLayout.LayoutParams.WRAP_CONTENT));
		
		final ScrollView scroll = new ScrollView(this);
		scroll.setBackgroundColor(C_BG);
		scroll.setVerticalScrollBarEnabled(false);
		final LinearLayout listContainer = new LinearLayout(this);
		listContainer.setOrientation(LinearLayout.VERTICAL);
		listContainer.setPadding((int)(16*D),(int)(12*D),(int)(16*D),(int)(24*D));
		scroll.addView(listContainer, new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.MATCH_PARENT,
		    LinearLayout.LayoutParams.WRAP_CONTENT));
		root.addView(scroll, new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.MATCH_PARENT,0,1f));
		
		final LinearLayout[] bandeauHolder = {null};
		final Runnable removeBandeau = new Runnable() {
			    @Override public void run() {
				        if (bandeauHolder[0]!=null && bandeauHolder[0].getParent()!=null) {
					            ((android.view.ViewGroup)bandeauHolder[0].getParent())
					                .removeView(bandeauHolder[0]);
					            bandeauHolder[0]=null;
					        }
				    }
		};
		final String[] bandeauFolder = {null};
		final String[] bandeauFile   = {null};
		
		java.util.Calendar cal = java.util.Calendar.getInstance();
		int dow = cal.get(java.util.Calendar.DAY_OF_WEEK);
		java.util.Calendar sabataCal = (java.util.Calendar)cal.clone();
		sabataCal.set(java.util.Calendar.HOUR_OF_DAY,0);
		sabataCal.set(java.util.Calendar.MINUTE,0);
		sabataCal.set(java.util.Calendar.SECOND,0);
		sabataCal.set(java.util.Calendar.MILLISECOND,0);
		int dss = (dow==java.util.Calendar.SATURDAY)?0:dow;
		sabataCal.add(java.util.Calendar.DAY_OF_YEAR,-dss);
		java.util.Calendar zomaCal = (java.util.Calendar)sabataCal.clone();
		zomaCal.add(java.util.Calendar.DAY_OF_YEAR,6);
		final String[] VOLANA2 = {
			    "janoary","febroary","martsa","aprily","mey","jona",
			    "jolay","aogositra","septambra","oktobra","novambra","desambra"
		};
		final int sabataDay  = sabataCal.get(java.util.Calendar.DAY_OF_MONTH);
		final int sabataMois = sabataCal.get(java.util.Calendar.MONTH);
		final int zomaDay    = zomaCal.get(java.util.Calendar.DAY_OF_MONTH);
		final int zomaMois   = zomaCal.get(java.util.Calendar.MONTH);
		final int zomaAn     = zomaCal.get(java.util.Calendar.YEAR);
		final String weekLabel;
		if (sabataMois==zomaMois) {
			    weekLabel="Sabata "+sabataDay+" - Zoma "+zomaDay
			        +" "+VOLANA2[zomaMois]+" "+zomaAn;
		} else {
			    weekLabel="Sabata "+sabataDay+" "+VOLANA2[sabataMois]
			        +" - Zoma "+zomaDay+" "+VOLANA2[zomaMois]+" "+zomaAn;
		}
		
		final Runnable doAddBandeau = new Runnable() {
			    @Override public void run() {
				        final String ff = bandeauFolder[0];
				        final String sf = bandeauFile[0];
				        if (ff==null||sf==null) return;
				        String lastSeg = ff.contains("/")
				            ?ff.substring(ff.lastIndexOf("/")+1):ff;
				        int lNum=-1;
				        if (lastSeg.contains("_")) {
					            try { lNum=Integer.parseInt(
						                lastSeg.substring(0,lastSeg.indexOf("_")));
						            } catch(Exception e2){}
					        }
				        final int lesonaNum=lNum;
				        LinearLayout bandeau = new LinearLayout(MainActivity.this);
				        bandeau.setOrientation(LinearLayout.HORIZONTAL);
				        bandeau.setGravity(Gravity.CENTER_VERTICAL);
				        bandeau.setPadding((int)(14*D),(int)(10*D),(int)(14*D),(int)(10*D));
				        bandeau.setClickable(true);
				        bandeau.setFocusable(true);
				        GradientDrawable bg2 = new GradientDrawable();
				        bg2.setColor(0xFF1a2035);
				        bg2.setCornerRadius(12*D);
				        bg2.setStroke((int)(1*D),C_ACCENT);
				        bandeau.setBackground(bg2);
				        TextView calIco = new TextView(MainActivity.this);
				        calIco.setText("🗓"); calIco.setTextSize(15);
				        bandeau.addView(calIco);
				        LinearLayout bText = new LinearLayout(MainActivity.this);
				        bText.setOrientation(LinearLayout.VERTICAL);
				        LinearLayout.LayoutParams btLp2 = new LinearLayout.LayoutParams(
				            0,LinearLayout.LayoutParams.WRAP_CONTENT,1f);
				        btLp2.setMargins((int)(10*D),0,0,0);
				        TextView ank = new TextView(MainActivity.this);
				        ank.setText("Lesona ankehitriny");
				        ank.setTextColor(C_ACCENT); ank.setTextSize(11f);
				        ank.setTypeface(Typeface.DEFAULT_BOLD);
				        bText.addView(ank);
				        TextView wTv = new TextView(MainActivity.this);
				        wTv.setText(weekLabel); wTv.setTextColor(C_TEXT);
				        wTv.setTextSize(12.5f); wTv.setTypeface(Typeface.DEFAULT_BOLD);
				        LinearLayout.LayoutParams wlp2 = new LinearLayout.LayoutParams(
				            LinearLayout.LayoutParams.WRAP_CONTENT,
				            LinearLayout.LayoutParams.WRAP_CONTENT);
				        wlp2.topMargin=(int)(2*D);
				        bText.addView(wTv,wlp2);
				        bandeau.addView(bText,btLp2);
				        if (lesonaNum>0) {
					            TextView badge = new TextView(MainActivity.this);
					            badge.setText("L"+lesonaNum);
					            badge.setTextColor(C_GOLD); badge.setTextSize(12f);
					            badge.setTypeface(Typeface.DEFAULT_BOLD);
					            badge.setGravity(Gravity.CENTER);
					            GradientDrawable lbBg2 = new GradientDrawable();
					            lbBg2.setColor(0xFF251e0e); lbBg2.setCornerRadius(8*D);
					            lbBg2.setStroke((int)(1*D),C_GOLD_DIM);
					            badge.setBackground(lbBg2);
					            badge.setPadding((int)(8*D),(int)(4*D),(int)(8*D),(int)(4*D));
					            LinearLayout.LayoutParams badgeLp2 =
					                new LinearLayout.LayoutParams(
					                    LinearLayout.LayoutParams.WRAP_CONTENT,
					                    LinearLayout.LayoutParams.WRAP_CONTENT);
					            badgeLp2.setMargins((int)(6*D),0,(int)(6*D),0);
					            bandeau.addView(badge,badgeLp2);
					        }
				        TextView arr = new TextView(MainActivity.this);
				        arr.setText("›"); arr.setTextSize(22);
				        arr.setTextColor(C_ACCENT); arr.setGravity(Gravity.CENTER);
				        LinearLayout.LayoutParams alp2 = new LinearLayout.LayoutParams(
				            LinearLayout.LayoutParams.WRAP_CONTENT,
				            LinearLayout.LayoutParams.WRAP_CONTENT);
				        alp2.setMargins((int)(4*D),0,0,0);
				        bandeau.addView(arr,alp2);
				        bandeau.setOnClickListener(new View.OnClickListener() {
					            @Override public void onClick(View v) {
						                Intent it = new Intent(
						                    MainActivity.this,LesonaActivity.class);
						                it.putExtra("lesona_folder",ff);
						                it.putExtra("lesona_start_file",sf);
						                startActivity(it);
						            }
					        });
				        LinearLayout.LayoutParams bLp2 = new LinearLayout.LayoutParams(
				            LinearLayout.LayoutParams.MATCH_PARENT,
				            LinearLayout.LayoutParams.WRAP_CONTENT);
				        // FIX espacement demandé : 20 -> 26
				        bLp2.topMargin=(int)(26*D);
				        header.addView(bandeau,header.indexOfChild(headerSepView));
				        bandeauHolder[0]=bandeau;
				    }
		};
		
		final Runnable[] renderScreen     = new Runnable[1];
		final Runnable[] renderAnnees     = new Runnable[1];
		final Runnable[] renderTrimestres = new Runnable[1];
		final Runnable[] renderLesona     = new Runnable[1];
		
		renderAnnees[0] = new Runnable() {
			    @Override public void run() {
				        toolbarTitle.setText("Lesona Kilasy Lehibe");
				        subtitleView.setText("Safidio ny taona");
				        btnHeaderBack.setVisibility(View.GONE);
				        removeBandeau.run();
				        statsBar.removeAllViews();
				        listContainer.removeAllViews();
				        scroll.scrollTo(0,0);
				
				        hybridPath[0]="";
				        hybridList.run();
				        String[] all = hybridResult[0]!=null?hybridResult[0]:new String[0];
				        java.util.List<String> taonaList = new java.util.ArrayList<String>();
				        for (String f:all) if(f.startsWith("Taona ")) taonaList.add(f);
				        java.util.Collections.sort(taonaList);
				
				        foundLessonPath[0]=null; foundLessonFile[0]=null;
				        for (int ti=0;ti<taonaList.size()&&foundLessonPath[0]==null;ti++) {
					            String taona=taonaList.get(ti);
					            hybridPath[0]=taona; hybridList.run();
					            String[] tr=hybridResult[0]!=null?hybridResult[0]:new String[0];
					            for (int tj=0;tj<tr.length&&foundLessonPath[0]==null;tj++) {
						                String trimPath=taona+"/"+tr[tj];
						                hybridPath[0]=trimPath; hybridList.run();
						                String[] lf=hybridResult[0]!=null?hybridResult[0]:new String[0];
						                for (int tk=0;tk<lf.length&&foundLessonPath[0]==null;tk++) {
							                    String lp=trimPath+"/"+lf[tk];
							                    hybridPath[0]=lp; hybridList.run();
							                    String[] hf=hybridResult[0]!=null?hybridResult[0]:new String[0];
							                    for (String f:hf) {
								                        if(f.equals(todayFilename+".txt")) {
									                            foundLessonPath[0]=lp;
									                            foundLessonFile[0]=f; break;
									                        }
								                    }
							                }
						            }
					        }
				        if (foundLessonPath[0]!=null) {
					            bandeauFolder[0]=foundLessonPath[0];
					            bandeauFile[0]=foundLessonFile[0];
					            doAddBandeau.run();
					        }
				
				        TextView statCount = new TextView(MainActivity.this);
				        statCount.setText("✦  "+taonaList.size()+" taona");
				        statCount.setTextColor(C_GREEN); statCount.setTextSize(12);
				        statsBar.addView(statCount);
				
				        if (taonaList.isEmpty()) {
					            TextView empty = new TextView(MainActivity.this);
					            empty.setText("Tsy misy taona hita");
					            empty.setTextColor(C_MUTED); empty.setTextSize(14);
					            empty.setGravity(Gravity.CENTER);
					            empty.setPadding(0,(int)(40*D),0,0);
					            listContainer.addView(empty); return;
					        }
				        for (int ti=0;ti<taonaList.size();ti++) {
					            final String taona=taonaList.get(ti);
					            boolean hasCur=foundLessonPath[0]!=null
					                &&foundLessonPath[0].startsWith(taona+"/");
					            LinearLayout card = new LinearLayout(MainActivity.this);
					            card.setOrientation(LinearLayout.HORIZONTAL);
					            card.setGravity(Gravity.CENTER_VERTICAL);
					            card.setPadding((int)(16*D),(int)(16*D),(int)(16*D),(int)(16*D));
					            card.setClickable(true); card.setFocusable(true);
					            GradientDrawable cbg2 = new GradientDrawable();
					            cbg2.setColor(hasCur?0xFF1e2540:C_SURFACE2);
					            cbg2.setCornerRadius(16*D);
					            cbg2.setStroke(hasCur?(int)(1.5f*D):(int)(1f*D),
					                hasCur?C_ACCENT:0xFF2e3550);
					            card.setBackground(cbg2);
					            TextView ico = new TextView(MainActivity.this);
					            ico.setText("📅"); ico.setTextSize(22);
					            ico.setGravity(Gravity.CENTER);
					            ico.setPadding(0,0,(int)(14*D),0);
					            card.addView(ico);
					            LinearLayout textCol = new LinearLayout(MainActivity.this);
					            textCol.setOrientation(LinearLayout.VERTICAL);
					            TextView tv2 = new TextView(MainActivity.this);
					            tv2.setText(taona); tv2.setTextColor(C_TEXT);
					            tv2.setTextSize(16); tv2.setTypeface(Typeface.DEFAULT_BOLD);
					            textCol.addView(tv2);
					            if (hasCur) {
						                TextView cur = new TextView(MainActivity.this);
						                cur.setText("● Lesona ankehitriny");
						                cur.setTextColor(C_ACCENT); cur.setTextSize(11f);
						                LinearLayout.LayoutParams lp3 =
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.WRAP_CONTENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT);
						                lp3.topMargin=(int)(3*D);
						                textCol.addView(cur,lp3);
						            }
					            card.addView(textCol, new LinearLayout.LayoutParams(
					                0,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
					            TextView arrow2 = new TextView(MainActivity.this);
					            arrow2.setText("›"); arrow2.setTextSize(22);
					            arrow2.setTextColor(hasCur?C_ACCENT:C_GOLD);
					            arrow2.setGravity(Gravity.CENTER);
					            GradientDrawable ab2 = new GradientDrawable();
					            ab2.setShape(GradientDrawable.OVAL);
					            ab2.setColor(hasCur?0xFF1a2040:0xFF1c1f2e);
					            arrow2.setBackground(ab2);
					            int as2=(int)(34*D);
					            LinearLayout.LayoutParams alp3 =
					                new LinearLayout.LayoutParams(as2,as2);
					            alp3.setMargins((int)(8*D),0,0,0);
					            card.addView(arrow2,alp3);
					            card.setOnClickListener(new View.OnClickListener() {
						                @Override public void onClick(View v) {
							                    selTaona[0]=taona; navLevel[0]=1;
							                    renderScreen[0].run();
							                }
						            });
					            LinearLayout.LayoutParams cardLp2 =
					                new LinearLayout.LayoutParams(
					                    LinearLayout.LayoutParams.MATCH_PARENT,
					                    LinearLayout.LayoutParams.WRAP_CONTENT);
					            cardLp2.setMargins(0,0,0,(int)(10*D));
					            listContainer.addView(card,cardLp2);
					        }
				    }
		};
		
		renderTrimestres[0] = new Runnable() {
			    @Override public void run() {
				        toolbarTitle.setText(selTaona[0]);
				        subtitleView.setText("Safidio ny telovolana");
				        btnHeaderBack.setVisibility(View.VISIBLE);
				        removeBandeau.run();
				        statsBar.removeAllViews();
				        listContainer.removeAllViews();
				        scroll.scrollTo(0,0);
				
				        hybridPath[0]=selTaona[0]; hybridList.run();
				        String[] trimFolders=hybridResult[0]!=null
				            ?hybridResult[0]:new String[0];
				        final String[] curTrimFolder={null};
				        foundLessonPath[0]=null; foundLessonFile[0]=null;
				
				        for (int tj=0;tj<trimFolders.length&&foundLessonPath[0]==null;tj++) {
					            String tf=trimFolders[tj];
					            String trimPath=selTaona[0]+"/"+tf;
					            hybridPath[0]=trimPath; hybridList.run();
					            String[] lf=hybridResult[0]!=null?hybridResult[0]:new String[0];
					            for (int tk=0;tk<lf.length&&foundLessonPath[0]==null;tk++) {
						                String lp=trimPath+"/"+lf[tk];
						                hybridPath[0]=lp; hybridList.run();
						                String[] hf=hybridResult[0]!=null?hybridResult[0]:new String[0];
						                for (String f:hf) {
							                    if(f.equals(todayFilename+".txt")) {
								                        curTrimFolder[0]=tf;
								                        foundLessonPath[0]=lp;
								                        foundLessonFile[0]=f; break;
								                    }
							                }
						            }
					        }
				        if (foundLessonPath[0]!=null) {
					            bandeauFolder[0]=foundLessonPath[0];
					            bandeauFile[0]=foundLessonFile[0];
					            doAddBandeau.run();
					        }
				
				        TextView statCount2 = new TextView(MainActivity.this);
				        statCount2.setText("✦  "+trimFolders.length+" telovolana");
				        statCount2.setTextColor(C_GREEN); statCount2.setTextSize(12);
				        statsBar.addView(statCount2);
				
				        for (int t=0;t<4;t++) {
					            final int tidx=t;
					            String foundTrim=null;
					            for (String f:trimFolders)
					                if(f.startsWith((t+1)+"_")){foundTrim=f;break;}
					            final String trimFolder=foundTrim;
					            final boolean avail=(trimFolder!=null);
					            boolean isCur=avail&&trimFolder.equals(curTrimFolder[0]);
					
					            LinearLayout card2 = new LinearLayout(MainActivity.this);
					            card2.setOrientation(LinearLayout.HORIZONTAL);
					            card2.setGravity(Gravity.CENTER_VERTICAL);
					            card2.setPadding((int)(16*D),(int)(16*D),(int)(16*D),(int)(16*D));
					            card2.setClickable(true); card2.setFocusable(true);
					            if(!avail) card2.setAlpha(0.5f);
					            GradientDrawable cbg3 = new GradientDrawable();
					            cbg3.setColor(isCur?0xFF1e2540:C_SURFACE2);
					            cbg3.setCornerRadius(16*D);
					            cbg3.setStroke(isCur?(int)(1.5f*D):(int)(1f*D),
					                isCur?C_ACCENT:0xFF2e3550);
					            card2.setBackground(cbg3);
					
					            TextView numV = new TextView(MainActivity.this);
					            numV.setText(String.valueOf(t+1));
					            numV.setTextSize(14); numV.setTypeface(Typeface.DEFAULT_BOLD);
					            numV.setTextColor(isCur?C_ACCENT:(avail?C_GOLD:C_MUTED));
					            numV.setGravity(Gravity.CENTER);
					            int cs2=(int)(44*D);
					            GradientDrawable cb2 = new GradientDrawable();
					            cb2.setShape(GradientDrawable.OVAL);
					            cb2.setColor(isCur?0xFF1a2040:(avail?0xFF1f2030:0xFF161820));
					            cb2.setStroke((int)(1.5f*D),isCur?C_ACCENT:(avail?C_GOLD_DIM:0xFF252840));
					            numV.setBackground(cb2);
					            LinearLayout.LayoutParams numLp2 =
					                new LinearLayout.LayoutParams(cs2,cs2);
					            numLp2.setMargins(0,0,(int)(14*D),0);
					            card2.addView(numV,numLp2);
					
					            LinearLayout textCol2 = new LinearLayout(MainActivity.this);
					            textCol2.setOrientation(LinearLayout.VERTICAL);
					            TextView nameV = new TextView(MainActivity.this);
					            nameV.setText(NOM_TRIMESTRE[t]);
					            nameV.setTextColor(avail?C_TEXT:C_MUTED);
					            nameV.setTextSize(15); nameV.setTypeface(Typeface.DEFAULT_BOLD);
					            textCol2.addView(nameV);
					            if (avail&&trimFolder.contains("_")) {
						                String st=trimFolder.substring(trimFolder.indexOf("_")+1);
						                if(!st.isEmpty()) {
							                    TextView stV = new TextView(MainActivity.this);
							                    stV.setText(st);
							                    stV.setTextColor(isCur?C_GOLD:C_MUTED);
							                    stV.setTextSize(12f); stV.setTypeface(Typeface.DEFAULT_BOLD);
							                    LinearLayout.LayoutParams stLp2 =
							                        new LinearLayout.LayoutParams(
							                            LinearLayout.LayoutParams.WRAP_CONTENT,
							                            LinearLayout.LayoutParams.WRAP_CONTENT);
							                    stLp2.topMargin=(int)(2*D);
							                    textCol2.addView(stV,stLp2);
							                }
						            }
					            TextView moisV = new TextView(MainActivity.this);
					            moisV.setText(MOIS_TRIMESTRE[t][0]+" · "
					                +MOIS_TRIMESTRE[t][1]+" · "+MOIS_TRIMESTRE[t][2]);
					            moisV.setTextColor(isCur?C_ACCENT:C_MUTED);
					            moisV.setTextSize(11f);
					            LinearLayout.LayoutParams mLp2 =
					                new LinearLayout.LayoutParams(
					                    LinearLayout.LayoutParams.WRAP_CONTENT,
					                    LinearLayout.LayoutParams.WRAP_CONTENT);
					            mLp2.topMargin=(int)(3*D);
					            textCol2.addView(moisV,mLp2);
					            card2.addView(textCol2, new LinearLayout.LayoutParams(
					                0,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
					
					            if (avail) {
						                TextView arrow3 = new TextView(MainActivity.this);
						                arrow3.setText("›"); arrow3.setTextSize(22);
						                arrow3.setTextColor(isCur?C_ACCENT:C_GOLD);
						                arrow3.setGravity(Gravity.CENTER);
						                GradientDrawable ab3 = new GradientDrawable();
						                ab3.setShape(GradientDrawable.OVAL);
						                ab3.setColor(isCur?0xFF1a2040:0xFF1c1f2e);
						                arrow3.setBackground(ab3);
						                int as3=(int)(34*D);
						                LinearLayout.LayoutParams alp4 =
						                    new LinearLayout.LayoutParams(as3,as3);
						                alp4.setMargins((int)(8*D),0,0,0);
						                card2.addView(arrow3,alp4);
						            }
					            card2.setOnClickListener(new View.OnClickListener() {
						                @Override public void onClick(View v) {
							                    if(!avail){
								                        Toast.makeText(MainActivity.this,
								                            "Mbola tsisy telovolana eto",
								                            Toast.LENGTH_SHORT).show();
								                        return;
								                    }
							                    selTrimPath[0]=selTaona[0]+"/"+trimFolder;
							                    selTrimIdx[0]=tidx; navLevel[0]=2;
							                    renderScreen[0].run();
							                }
						            });
					            LinearLayout.LayoutParams cardLp3 =
					                new LinearLayout.LayoutParams(
					                    LinearLayout.LayoutParams.MATCH_PARENT,
					                    LinearLayout.LayoutParams.WRAP_CONTENT);
					            cardLp3.setMargins(0,0,0,(int)(10*D));
					            listContainer.addView(card2,cardLp3);
					        }
				    }
		};
		
		renderLesona[0] = new Runnable() {
			    @Override public void run() {
				        toolbarTitle.setText(NOM_TRIMESTRE[selTrimIdx[0]]);
				        subtitleView.setText(selTaona[0]);
				        btnHeaderBack.setVisibility(View.VISIBLE);
				        removeBandeau.run();
				        statsBar.removeAllViews();
				        listContainer.removeAllViews();
				        scroll.scrollTo(0,0);
				
				        hybridPath[0]=selTrimPath[0]; hybridList.run();
				        String[] allFolders=hybridResult[0]!=null
				            ?hybridResult[0]:new String[0];
				
				        int maxNum=0;
				        for (String f:allFolders) {
					            if(f.contains("_")) {
						                try {
							                    int n=Integer.parseInt(f.substring(0,f.indexOf("_")));
							                    if(n>maxNum) maxNum=n;
							                } catch(Exception e){}
						            }
					        }
				        final int TOTAL=Math.max(13,maxNum);
				
				        final String[] displayTitles = new String[TOTAL];
				        final String[] folderNames   = new String[TOTAL];
				        final boolean[] available    = new boolean[TOTAL];
				        final boolean[] isCurrent    = new boolean[TOTAL];
				        final String[]  startFile    = new String[TOTAL];
				
				        for (int i=0;i<TOTAL;i++) {
					            int num=i+1;
					            String found=null;
					            for (String f:allFolders)
					                if(f.startsWith(num+"_")){found=f;break;}
					            if (found!=null) {
						                String lessonPath=selTrimPath[0]+"/"+found;
						                hybridPath[0]=lessonPath; hybridList.run();
						                String[] files=hybridResult[0]!=null
						                    ?hybridResult[0]:new String[0];
						                java.util.List<String> txts =
						                    new java.util.ArrayList<String>();
						                for (String f:files) if(f.endsWith(".txt")) txts.add(f);
						                if (!txts.isEmpty()) {
							                    java.util.Collections.sort(txts,dateComparator);
							                    available[i]=true;
							                    folderNames[i]=found;
							                    displayTitles[i]=found.substring((num+"_").length());
							                    boolean ft=false;
							                    for (String f:txts)
							                        if(f.equals(todayFilename+".txt")){ft=true;break;}
							                    isCurrent[i]=ft;
							                    startFile[i]=ft?(todayFilename+".txt"):txts.get(0);
							                } else {
							                    available[i]=false;
							                    displayTitles[i]="Lesona "+num;
							                }
						            } else {
						                available[i]=false;
						                displayTitles[i]="Lesona "+num;
						            }
					        }
				
				        int countAvail=0;
				        for(boolean b:available) if(b) countAvail++;
				        int semCourant=-1;
				        for(int i=0;i<TOTAL;i++) if(isCurrent[i]){semCourant=i;break;}
				
				        if (semCourant>=0&&available[semCourant]) {
					            bandeauFolder[0]=selTrimPath[0]+"/"+folderNames[semCourant];
					            bandeauFile[0]=startFile[semCourant];
					            doAddBandeau.run();
					        }
				
				        TextView sa = new TextView(MainActivity.this);
				        sa.setText("✦  "+countAvail+" vonona");
				        sa.setTextColor(C_GREEN); sa.setTextSize(12);
				        statsBar.addView(sa);
				        TextView sd = new TextView(MainActivity.this);
				        sd.setText("   ·   "); sd.setTextColor(C_BORDER);
				        sd.setTextSize(12); statsBar.addView(sd);
				        TextView st2 = new TextView(MainActivity.this);
				        st2.setText((TOTAL-countAvail)+" mbola tsy ampy");
				        st2.setTextColor(C_MUTED); st2.setTextSize(12);
				        statsBar.addView(st2);
				
				        for (int i=0;i<TOTAL;i++) {
					            final boolean avail2=available[i];
					            final boolean isCur2=isCurrent[i];
					            final String sfFile=startFile[i];
					            final String ff2=avail2?(selTrimPath[0]+"/"+folderNames[i]):null;
					
					            LinearLayout card3 = new LinearLayout(MainActivity.this);
					            card3.setOrientation(LinearLayout.HORIZONTAL);
					            card3.setGravity(Gravity.CENTER_VERTICAL);
					            card3.setPadding((int)(16*D),(int)(16*D),(int)(16*D),(int)(16*D));
					            card3.setClickable(true); card3.setFocusable(true);
					            GradientDrawable cbg4 = new GradientDrawable();
					            cbg4.setColor(isCur2?0xFF1e2540:(avail2?C_SURFACE2:C_SURFACE));
					            cbg4.setCornerRadius(16*D);
					            cbg4.setStroke(isCur2?(int)(1.5f*D):(int)(1f*D),
					                isCur2?C_ACCENT:(avail2?0xFF2e3550:C_BORDER));
					            card3.setBackground(cbg4);
					
					            TextView numView = new TextView(MainActivity.this);
					            numView.setText(String.format("%02d",i+1));
					            numView.setTextSize(14); numView.setTypeface(Typeface.DEFAULT_BOLD);
					            numView.setTextColor(isCur2?C_ACCENT:(avail2?C_GOLD:C_MUTED));
					            numView.setGravity(Gravity.CENTER);
					            int circleSize=(int)(44*D);
					            GradientDrawable circleBg2 = new GradientDrawable();
					            circleBg2.setShape(GradientDrawable.OVAL);
					            circleBg2.setColor(isCur2?0xFF1a2040:(avail2?0xFF1f2030:0xFF161820));
					            circleBg2.setStroke((int)(1.5f*D),
					                isCur2?C_ACCENT:(avail2?C_GOLD_DIM:0xFF252840));
					            numView.setBackground(circleBg2);
					            LinearLayout.LayoutParams numLp3 =
					                new LinearLayout.LayoutParams(circleSize,circleSize);
					            numLp3.setMargins(0,0,(int)(14*D),0);
					            card3.addView(numView,numLp3);
					
					            LinearLayout textCol3 = new LinearLayout(MainActivity.this);
					            textCol3.setOrientation(LinearLayout.VERTICAL);
					            TextView titleV2 = new TextView(MainActivity.this);
					            titleV2.setText(displayTitles[i]);
					            titleV2.setTextSize(15); titleV2.setTypeface(Typeface.DEFAULT_BOLD);
					            titleV2.setTextColor(avail2?C_TEXT:C_MUTED);
					            textCol3.addView(titleV2);
					
					            if (isCur2) {
						                TextView wr = new TextView(MainActivity.this);
						                wr.setText("📅 "+weekLabel);
						                wr.setTextColor(C_ACCENT); wr.setTextSize(11f);
						                LinearLayout.LayoutParams wrLp =
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.WRAP_CONTENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT);
						                wrLp.topMargin=(int)(4*D);
						                textCol3.addView(wr,wrLp);
						            } else {
						                TextView b2 = new TextView(MainActivity.this);
						                b2.setText(avail2?"● Vonona":"○ Mbola tsisy");
						                b2.setTextColor(avail2?C_GREEN:0xFF555a72);
						                b2.setTextSize(11f);
						                LinearLayout.LayoutParams bLp3 =
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.WRAP_CONTENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT);
						                bLp3.topMargin=(int)(4*D);
						                textCol3.addView(b2,bLp3);
						            }
					            card3.addView(textCol3, new LinearLayout.LayoutParams(
					                0,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
					
					            if (avail2) {
						                TextView arrow4 = new TextView(MainActivity.this);
						                arrow4.setText("›"); arrow4.setTextSize(22);
						                arrow4.setTextColor(isCur2?C_ACCENT:C_GOLD);
						                arrow4.setGravity(Gravity.CENTER);
						                GradientDrawable ab4 = new GradientDrawable();
						                ab4.setShape(GradientDrawable.OVAL);
						                ab4.setColor(isCur2?0xFF1a2040:0xFF1c1f2e);
						                arrow4.setBackground(ab4);
						                int as4=(int)(34*D);
						                LinearLayout.LayoutParams alp5 =
						                    new LinearLayout.LayoutParams(as4,as4);
						                alp5.setMargins((int)(8*D),0,0,0);
						                card3.addView(arrow4,alp5);
						            }
					            card3.setOnClickListener(new View.OnClickListener() {
						                @Override public void onClick(View v) {
							                    if(!avail2){
								                        Toast.makeText(MainActivity.this,
								                            "Mbola tsisy lesona eto",
								                            Toast.LENGTH_SHORT).show();
								                        return;
								                    }
							                    Intent it2 = new Intent(
							                        MainActivity.this,LesonaActivity.class);
							                    it2.putExtra("lesona_folder",ff2);
							                    it2.putExtra("lesona_start_file",sfFile);
							                    startActivity(it2);
							                }
						            });
					            LinearLayout.LayoutParams cardLp4 =
					                new LinearLayout.LayoutParams(
					                    LinearLayout.LayoutParams.MATCH_PARENT,
					                    LinearLayout.LayoutParams.WRAP_CONTENT);
					            cardLp4.setMargins(0,0,0,(int)(10*D));
					            listContainer.addView(card3,cardLp4);
					        }
				
				        final java.util.List<String> tFolders =
				            new java.util.ArrayList<String>();
				        final java.util.List<String> tNames =
				            new java.util.ArrayList<String>();
				        for (int i=0;i<TOTAL;i++) {
					            if(available[i]) {
						                tFolders.add(selTrimPath[0]+"/"+folderNames[i]);
						                tNames.add(displayTitles[i]);
						            }
					        }
				        if (!tFolders.isEmpty()) {
					            View sep3 = new View(MainActivity.this);
					            sep3.setBackgroundColor(C_BORDER);
					            LinearLayout.LayoutParams sepLp2 =
					                new LinearLayout.LayoutParams(
					                    LinearLayout.LayoutParams.MATCH_PARENT,(int)(1*D));
					            sepLp2.setMargins(0,(int)(8*D),0,(int)(16*D));
					            listContainer.addView(sep3,sepLp2);
					            LinearLayout btnTs = new LinearLayout(MainActivity.this);
					            btnTs.setOrientation(LinearLayout.HORIZONTAL);
					            btnTs.setGravity(Gravity.CENTER_VERTICAL);
					            btnTs.setPadding((int)(18*D),(int)(16*D),(int)(18*D),(int)(16*D));
					            btnTs.setClickable(true); btnTs.setFocusable(true);
					            GradientDrawable tbg2 = new GradientDrawable();
					            tbg2.setColor(0xFF1a1f30);
					            tbg2.setCornerRadius(16*D);
					            tbg2.setStroke((int)(1*D),C_GOLD_DIM);
					            btnTs.setBackground(tbg2);
					            TextView tIco2 = new TextView(MainActivity.this);
					            tIco2.setText("✨"); tIco2.setTextSize(18);
					            tIco2.setPadding(0,0,(int)(12*D),0);
					            btnTs.addView(tIco2);
					            TextView tLabel2 = new TextView(MainActivity.this);
					            tLabel2.setText("Tsianjery "+countAvail);
					            tLabel2.setTextColor(C_GOLD); tLabel2.setTextSize(15);
					            tLabel2.setTypeface(Typeface.DEFAULT_BOLD);
					            btnTs.addView(tLabel2, new LinearLayout.LayoutParams(
					                0,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
					            TextView tArr2 = new TextView(MainActivity.this);
					            tArr2.setText("›"); tArr2.setTextSize(22);
					            tArr2.setTextColor(C_GOLD); tArr2.setGravity(Gravity.CENTER);
					            btnTs.addView(tArr2);
					            btnTs.setOnClickListener(new View.OnClickListener() {
						                @Override public void onClick(View v) {
							                    Intent it3 = new Intent(
							                        MainActivity.this,LesonaActivity.class);
							                    it3.putExtra("mode_tsianjery",true);
							                    it3.putExtra("tsianjery_folders",
							                        tFolders.toArray(new String[0]));
							                    it3.putExtra("tsianjery_names",
							                        tNames.toArray(new String[0]));
							                    startActivity(it3);
							                }
						            });
					            listContainer.addView(btnTs, new LinearLayout.LayoutParams(
					                LinearLayout.LayoutParams.MATCH_PARENT,
					                LinearLayout.LayoutParams.WRAP_CONTENT));
					        }
				    }
		};
		
		renderScreen[0] = new Runnable() {
			    @Override public void run() {
				        switch(navLevel[0]) {
					            case 0: renderAnnees[0].run();     break;
					            case 1: renderTrimestres[0].run(); break;
					            case 2: renderLesona[0].run();     break;
					        }
				    }
		};
		
		btnHeaderBack.setOnClickListener(new View.OnClickListener() {
			    @Override public void onClick(View v) {
				        if(navLevel[0]>0) {
					            navLevel[0]--;
					            if(navLevel[0]<=0) selTaona[0]=null;
					            if(navLevel[0]<=1){selTrimPath[0]=null;selTrimIdx[0]=-1;}
					            renderScreen[0].run();
					        }
				    }
		});
		
		Object[] state = new Object[]{
			    navLevel,renderScreen,selTaona,selTrimPath,selTrimIdx};
		root.setTag(state);
		
		// ══════════════════════════════════════════════════════════════
		// ── SYNC GITHUB via jsDelivr + fallback raw (Android 4.1+) ────
		// ══════════════════════════════════════════════════════════════
		final java.util.concurrent.atomic.AtomicBoolean syncInProgress =
		    new java.util.concurrent.atomic.AtomicBoolean(false);
		final java.util.concurrent.atomic.AtomicBoolean manualWaiting =
		    new java.util.concurrent.atomic.AtomicBoolean(false);
		final Runnable[] renderRef = renderScreen;
		final boolean[] manualSyncFlag = {false};
		
		// TLS 1.2 forcé en secours pour l'appel API GitHub et le fallback raw (Android < 6)
		final javax.net.ssl.SSLSocketFactory[] tls12Factory = {null};
		if (android.os.Build.VERSION.SDK_INT < 23) {
			    try {
				        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("TLSv1.2");
				        sc.init(null, null, null);
				        final javax.net.ssl.SSLSocketFactory delegate = sc.getSocketFactory();
				        tls12Factory[0] = new javax.net.ssl.SSLSocketFactory() {
					            private java.net.Socket enable(java.net.Socket s) {
						                if (s instanceof javax.net.ssl.SSLSocket)
						                    ((javax.net.ssl.SSLSocket) s).setEnabledProtocols(new String[]{"TLSv1.2"});
						                return s;
						            }
					            @Override public String[] getDefaultCipherSuites() { return delegate.getDefaultCipherSuites(); }
					            @Override public String[] getSupportedCipherSuites() { return delegate.getSupportedCipherSuites(); }
					            @Override public java.net.Socket createSocket(java.net.Socket s, String host, int port, boolean autoClose) throws java.io.IOException {
						                return enable(delegate.createSocket(s, host, port, autoClose));
						            }
					            @Override public java.net.Socket createSocket(String host, int port) throws java.io.IOException {
						                return enable(delegate.createSocket(host, port));
						            }
					            @Override public java.net.Socket createSocket(String host, int port, java.net.InetAddress localHost, int localPort) throws java.io.IOException {
						                return enable(delegate.createSocket(host, port, localHost, localPort));
						            }
					            @Override public java.net.Socket createSocket(java.net.InetAddress host, int port) throws java.io.IOException {
						                return enable(delegate.createSocket(host, port));
						            }
					            @Override public java.net.Socket createSocket(java.net.InetAddress address, int port, java.net.InetAddress localAddress, int localPort) throws java.io.IOException {
						                return enable(delegate.createSocket(address, port, localAddress, localPort));
						            }
					        };
				    } catch (Exception e) { tls12Factory[0] = null; }
		}
		
		// Popup de téléchargement (bouton "down") + drapeau d'annulation ("Ajanona")
		final SyncPopup syncPopup = new SyncPopup(MainActivity.this);
		final java.util.concurrent.atomic.AtomicBoolean syncCancelled =
		    new java.util.concurrent.atomic.AtomicBoolean(false);

		final Runnable[] doSync = new Runnable[1];
		doSync[0] = new Runnable() {
			    @Override public void run() {
				        boolean wantManual = manualSyncFlag[0];
				        manualSyncFlag[0] = false;
				
				        if (!syncInProgress.compareAndSet(false, true)) {
					            if (wantManual) manualWaiting.set(true);
					            return;
					        }
				
				        final boolean isManual = wantManual || manualWaiting.getAndSet(false);
				
				        android.net.ConnectivityManager cm =
				            (android.net.ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
				        android.net.NetworkInfo ni = cm != null ? cm.getActiveNetworkInfo() : null;
				        boolean connected = ni != null && ni.isConnected();
				
			int nouveaux = 0;
			int modifies = 0;
			int echecs = 0;
				        int total = 0;
			boolean apiOk = false;

			if (connected && !(isManual && syncCancelled.get())) {
					            try {
						                java.net.URL apiUrl = new java.net.URL(
						                    "https://api.github.com/repos/sitraka0417-cell/"
						                    + "Lesona/git/trees/main?recursive=1");
						                java.net.HttpURLConnection conn =
						                    (java.net.HttpURLConnection) apiUrl.openConnection();
						                if (tls12Factory[0] != null && conn instanceof javax.net.ssl.HttpsURLConnection) {
							                    ((javax.net.ssl.HttpsURLConnection) conn).setSSLSocketFactory(tls12Factory[0]);
							                }
						                conn.setRequestMethod("GET");
						                conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
						                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
						                conn.setConnectTimeout(15000);
						                conn.setReadTimeout(20000);
						                conn.setInstanceFollowRedirects(true);
						                int code = conn.getResponseCode();
						
						                if (code == 200) {
							                    apiOk = true;
							                    java.io.InputStream is = conn.getInputStream();
							                    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
							                    byte[] buf = new byte[4096];
							                    int len;
							                    while ((len = is.read(buf)) > 0) baos.write(buf, 0, len);
							                    is.close();
							                    conn.disconnect();
							                    String json = baos.toString("UTF-8");
							
							                    // Extraction des chemins .txt, quelle que soit leur profondeur
							                    // (Taona X/Trimestre Y/Lesona Z/fichier.txt)
							                    java.util.List<String> txtPaths = new java.util.ArrayList<String>();
							                    int idx = 0;
							                    while (true) {
								                        int pi = json.indexOf("\"path\":\"", idx);
								                        if (pi < 0) break;
								                        int st = pi + 8;
								                        int en = json.indexOf("\"", st);
								                        if (en < 0) break;
								                        String path = json.substring(st, en);
								                        if (path.endsWith(".txt") && !path.contains("README"))
								                            txtPaths.add(path);
								                        idx = en + 1;
								                    }
					// Fichiers à vérifier = ceux qui ne sont pas déjà livrés dans les assets
					java.util.List<String> toProcess = new java.util.ArrayList<String>();
					for (String cand : txtPaths) {
						if (cand.trim().isEmpty()) continue;
						boolean inAssets = false;
						try {
							java.io.InputStream ta = am.open(cand);
							ta.close();
							inAssets = true;
						} catch (Exception ea) {}
						if (!inAssets) toProcess.add(cand);
					}
					total = toProcess.size();
					int doneCount = 0;

					for (String txtPath : toProcess) {
						if (isManual && syncCancelled.get()) break;
								
								                        java.io.File localFile = new java.io.File(fDir, txtPath);
								                        boolean existedBefore = localFile.exists();
								                        String ancienContenu = "";
								                        if (existedBefore) {
									                            try {
										                                java.io.BufferedReader r2 = new java.io.BufferedReader(
										                                    new java.io.InputStreamReader(
										                                        new java.io.FileInputStream(localFile), "UTF-8"));
										                                StringBuilder s2 = new StringBuilder();
										                                String l2;
										                                while ((l2 = r2.readLine()) != null) s2.append(l2).append("\n");
										                                r2.close();
										                                ancienContenu = s2.toString();
										                            } catch (Exception ig) {}
									                        }
								
								                        // Encode chaque segment du chemin séparément (espaces etc.)
								                        String[] segs = txtPath.split("/");
								                        StringBuilder encPath = new StringBuilder();
								                        for (int si = 0; si < segs.length; si++) {
									                            if (si > 0) encPath.append("/");
									                            encPath.append(segs[si].replace(" ", "%20"));
									                        }
								                        String encodedPath = encPath.toString();
								
								                        boolean fileOk = false;
								                        String nouveauContenu = null;
								
								                        // 1. Tentative jsDelivr (HTTP simple, tolérant TLS)
								                        try {
									                            java.net.URL fu = new java.net.URL(
									                                "https://cdn.jsdelivr.net/gh/sitraka0417-cell/"
									                                + "Lesona@main/" + encodedPath
									                                + "?t=" + System.currentTimeMillis());
									                            java.net.HttpURLConnection fc =
									                                (java.net.HttpURLConnection) fu.openConnection();
									                            fc.setConnectTimeout(15000);
									                            fc.setReadTimeout(30000);
									                            fc.setRequestProperty("User-Agent", "Mozilla/5.0");
									                            fc.setInstanceFollowRedirects(true);
									                            int fCode = fc.getResponseCode();
									
									                            if (fCode == 200) {
										                                java.io.InputStream fis = fc.getInputStream();
										                                java.io.ByteArrayOutputStream fbaos = new java.io.ByteArrayOutputStream();
										                                byte[] fbuf = new byte[4096];
										                                int flen;
										                                while ((flen = fis.read(fbuf)) > 0) fbaos.write(fbuf, 0, flen);
										                                fis.close();
										                                fc.disconnect();
										                                nouveauContenu = fbaos.toString("UTF-8");
										                                fileOk = true;
										                            } else {
										                                fc.disconnect();
										                            }
									                        } catch (Exception jsE) {
									                            fileOk = false;
									                        }
								
								                        // 2. Fallback raw.githubusercontent.com si jsDelivr échoue
								                        if (!fileOk) {
									                            try {
										                                java.net.URL fu2 = new java.net.URL(
										                                    "https://raw.githubusercontent.com/"
										                                    + "sitraka0417-cell/Lesona/main/"
										                                    + encodedPath
										                                    + "?t=" + System.currentTimeMillis());
										                                java.net.HttpURLConnection fc2 =
										                                    (java.net.HttpURLConnection) fu2.openConnection();
										                                if (tls12Factory[0] != null && fc2 instanceof javax.net.ssl.HttpsURLConnection) {
											                                    ((javax.net.ssl.HttpsURLConnection) fc2).setSSLSocketFactory(tls12Factory[0]);
											                                }
										                                fc2.setConnectTimeout(15000);
										                                fc2.setReadTimeout(30000);
										                                fc2.setRequestProperty("User-Agent", "Mozilla/5.0");
										                                int fCode2 = fc2.getResponseCode();
										
										                                if (fCode2 == 200) {
											                                    java.io.InputStream fis2 = fc2.getInputStream();
											                                    java.io.ByteArrayOutputStream fbaos2 = new java.io.ByteArrayOutputStream();
											                                    byte[] fbuf2 = new byte[4096];
											                                    int flen2;
											                                    while ((flen2 = fis2.read(fbuf2)) > 0) fbaos2.write(fbuf2, 0, flen2);
											                                    fis2.close();
											                                    fc2.disconnect();
											                                    nouveauContenu = fbaos2.toString("UTF-8");
											                                    fileOk = true;
											                                } else {
											                                    fc2.disconnect();
											                                }
										                            } catch (Exception rawE) {
										                                fileOk = false;
										                            }
									                        }
								
								                        if (fileOk && nouveauContenu != null) {
									                            String ancienNorm = ancienContenu.replace("\r\n", "\n").trim();
									                            String nouveauNorm = nouveauContenu.replace("\r\n", "\n").trim();
									                            if (!existedBefore || !ancienNorm.equals(nouveauNorm)) {
										                                localFile.getParentFile().mkdirs();
										                                java.io.FileWriter fw = new java.io.FileWriter(localFile, false);
										                                fw.write(nouveauContenu);
										                                fw.close();
									if (!existedBefore) nouveaux++; else modifies++;
										                            }
									                        } else {
echecs++;
						}
						doneCount++;
						if (isManual) syncPopup.setProgress(doneCount * 100 / total);
					}
				} else {
					conn.disconnect();
							                }
						            } catch (Exception e) {
						                apiOk = false;
						            }
					        }
				
final int finalNouveaux = nouveaux;
			final int finalModifies = modifies;
			final boolean finalCancelled = isManual && syncCancelled.get();
				        final int finalEchecs = echecs;
				        final int finalTotal = total;
				        final boolean finalApiOk = apiOk;
				        final boolean finalConnected = connected;
				
				        runOnUiThread(new Runnable() {
					            @Override public void run() {
						                stopPersistentSyncToast.run();
						
						                if (finalApiOk) {
							                    renderRef[0].run();
							                }
						
								// Résultat d'une synchro manuelle : affiché dans le popup (pas de toast)
								if (isManual && !finalCancelled) {
									if (!finalConnected || !finalApiOk || finalEchecs > 0) {
										syncPopup.showError();        // Misy olana ny fifandraisana
									} else if (finalNouveaux + finalModifies > 0) {
										syncPopup.showDone();         // Vita ny fanavaozana
									} else {
										syncPopup.showNothingNew();   // Mbola tsy misy lesona vaovao
									}
								}
						            }
					        });
				
				        syncInProgress.set(false);
				        if (manualWaiting.getAndSet(false)) {
					            manualSyncFlag[0] = true;
					            new Thread(doSync[0]).start();
					        }
				    }
		};
		
		manualSyncFlag[0] = false;
		new Thread(doSync[0]).start();
		
		if (android.os.Build.VERSION.SDK_INT >= 23) {
			    android.net.ConnectivityManager connMgr =
			        (android.net.ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			    if (connMgr != null) {
				        android.net.NetworkRequest req = new android.net.NetworkRequest.Builder()
				            .addCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
				            .build();
				        android.net.ConnectivityManager.NetworkCallback cb =
				            new android.net.ConnectivityManager.NetworkCallback() {
					                @Override public void onAvailable(android.net.Network n) {
						                    manualSyncFlag[0] = false;
						                    new Thread(doSync[0]).start();
						                }
					            };
				        try { connMgr.registerNetworkCallback(req, cb); } catch (Exception e) {}
				    }
		}
		
		// "Anandrana indray" relance la synchro ; "Ajanona" / retour l'interrompt
		syncPopup.setListener(new SyncPopup.Listener() {
			@Override public void onRetry() {
				syncCancelled.set(false);
				manualSyncFlag[0] = true;
				new Thread(doSync[0]).start();
			}
			@Override public void onCancel() {
				syncCancelled.set(true);
			}
		});

		updateIcon.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				if (syncPopup.isShowing()) return;
				syncCancelled.set(false);
				syncPopup.showProgress();
				manualSyncFlag[0] = true;
				new Thread(doSync[0]).start();
			}
		});
		
		infoIcon.setOnClickListener(new View.OnClickListener() {
			    @Override public void onClick(View v) {
				        final android.app.Dialog dlg =
				            new android.app.Dialog(MainActivity.this);
				        dlg.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
				        dlg.setCancelable(true);
				
				        LinearLayout main = new LinearLayout(MainActivity.this);
				        main.setOrientation(LinearLayout.VERTICAL);
				        main.setPadding((int)(24*D),(int)(20*D),(int)(24*D),(int)(28*D));
				        GradientDrawable mbg = new GradientDrawable();
				        mbg.setColor(0xFF1a1e27);
				        mbg.setCornerRadii(new float[]{24*D,24*D,24*D,24*D,0,0,0,0});
				        main.setBackground(mbg);
				
				        View handle = new View(MainActivity.this);
				        GradientDrawable hBg = new GradientDrawable();
				        hBg.setColor(C_BORDER); hBg.setCornerRadius(4*D);
				        handle.setBackground(hBg);
				        LinearLayout.LayoutParams hLp =
				            new LinearLayout.LayoutParams((int)(40*D),(int)(4*D));
				        hLp.gravity = Gravity.CENTER_HORIZONTAL;
				        hLp.bottomMargin = (int)(20*D);
				        main.addView(handle, hLp);
				
				        TextView title = new TextView(MainActivity.this);
				        title.setText("Mombamomba");
				        title.setTextColor(C_TEXT);
				        title.setTextSize(18);
				        title.setTypeface(Typeface.DEFAULT_BOLD);
				        LinearLayout.LayoutParams ttLp = new LinearLayout.LayoutParams(
				            LinearLayout.LayoutParams.MATCH_PARENT,
				            LinearLayout.LayoutParams.WRAP_CONTENT);
				        ttLp.bottomMargin = (int)(16*D);
				        main.addView(title, ttLp);
				
				        TextView body = new TextView(MainActivity.this);
				        body.setText("Novolavolaina ity rindrambaiko ity hahafahanao "
				            +"misitraka sy mizara ny Lesona'ny Kilasy lehibe "
				            +"isan-kerinandro.\n\n"
				            +"Isaky ny tapitra ny tahiry, tsindrio ny bokotra "
				            +"fanavaozana hahafahanao mahazo ny lesona vaovao.");
				        body.setTextColor(C_MUTED);
				        body.setTextSize(14);
				        body.setLineSpacing(0,1.6f);
				        LinearLayout.LayoutParams bLp = new LinearLayout.LayoutParams(
				            LinearLayout.LayoutParams.MATCH_PARENT,
				            LinearLayout.LayoutParams.WRAP_CONTENT);
				        bLp.bottomMargin = (int)(20*D);
				        main.addView(body, bLp);
				
				        View sep5 = new View(MainActivity.this);
				        sep5.setBackgroundColor(C_BORDER);
				        LinearLayout.LayoutParams sep5Lp = new LinearLayout.LayoutParams(
				            LinearLayout.LayoutParams.MATCH_PARENT,(int)(1*D));
				        sep5Lp.bottomMargin=(int)(14*D);
				        main.addView(sep5, sep5Lp);
				
				        TextView footer = new TextView(MainActivity.this);
				        footer.setText("Mpikarakara: Sitraka Nambinintsoa");
				        footer.setTextColor(C_GOLD);
				        footer.setTextSize(13);
				        footer.setTypeface(Typeface.create("serif",Typeface.ITALIC));
				        footer.setGravity(Gravity.CENTER);
				        main.addView(footer);
				
				        dlg.setContentView(main);
				        android.view.Window w = dlg.getWindow();
				        if (w!=null) {
					            w.setLayout(
					                android.view.WindowManager.LayoutParams.MATCH_PARENT,
					                android.view.WindowManager.LayoutParams.WRAP_CONTENT);
					            w.setGravity(Gravity.BOTTOM);
					            w.setBackgroundDrawable(
					                new android.graphics.drawable.ColorDrawable(
					                    android.graphics.Color.TRANSPARENT));
					        }
				        dlg.show();
				    }
		});
		
		renderScreen[0].run();
	}
	
	
	@Override
	public void onBackPressed() {
		View v = findViewById(android.R.id.content);
		if (v instanceof android.view.ViewGroup) {
			    View r = ((android.view.ViewGroup)v).getChildAt(0);
			    if (r!=null && r.getTag() instanceof Object[]) {
				        Object[] st    = (Object[]) r.getTag();
				        int[]      nav = (int[])      st[0];
				        Runnable[] ren = (Runnable[]) st[1];
				        String[]   tao = (String[])  st[2];
				        String[]   tri = (String[])  st[3];
				        int[]      idx = (int[])     st[4];
				        if (nav[0]>0) {
					            nav[0]--;
					            if(nav[0]<=0) tao[0]=null;
					            if(nav[0]<=1){tri[0]=null;idx[0]=-1;}
					            ren[0].run();
					            return;
					        }
				    }
		}
		super.onBackPressed();
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}