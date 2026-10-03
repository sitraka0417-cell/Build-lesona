package com.lesona.lehibe;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.*;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
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
import android.webkit.WebChromeClient;
import android.webkit.JavascriptInterface;
import android.content.SharedPreferences;
import org.json.JSONObject;
import org.json.JSONArray;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.view.Gravity;
import android.view.View;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.content.Intent;

public class LesonaActivity extends Activity {
	
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.lesona);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		sp = getSharedPreferences("sp", Activity.MODE_PRIVATE);
	}
	
	private void initializeLogic() {
		final float D = getResources().getDisplayMetrics().density;
		
		final int C_BG     = 0xFF111318;
		final int C_CARD   = 0xFF1a1e27;
		final int C_GOLD   = 0xFFc9a84c;
		final int C_GDIM   = 0xFF7a5e28;
		final int C_BLUE   = 0xFF4e7bdb;
		final int C_TEXT   = 0xFFe8e8e8;
		final int C_MUTED  = 0xFF9aa0b2;
		final int C_BORDER = 0xFF2e3347;
		final int C_WHITE  = 0xFFFFFFFF;
		final int C_LINK   = 0xFF7aa3f5;
		
		final int[] fontSize = {24};
		final java.util.List<android.widget.TextView> contentViews =
		    new java.util.ArrayList<android.widget.TextView>();
		
		final boolean modeTsianjery = getIntent()
		    .getBooleanExtra("mode_tsianjery",false);
		final String lessonaFolder = getIntent()
		    .getStringExtra("lesona_folder");
		final String lessonaStartFile = getIntent()
		    .getStringExtra("lesona_start_file");
		
		final java.util.HashMap<Integer,
		    java.util.HashMap<Integer,
		        java.util.HashMap<Integer,String>>> bibleData =
		    new java.util.HashMap<Integer,
		        java.util.HashMap<Integer,
		            java.util.HashMap<Integer,String>>>();
		final boolean[] bibleLoaded = {false};
		
		final java.util.HashMap<String,Integer> ABBREV_TO_BOOKNUM =
		    new java.util.HashMap<String,Integer>();
		ABBREV_TO_BOOKNUM.put("Genesisy",10);
		ABBREV_TO_BOOKNUM.put("Genesis",10);
		ABBREV_TO_BOOKNUM.put("Gen.",10);
		ABBREV_TO_BOOKNUM.put("Gene.",10);
		ABBREV_TO_BOOKNUM.put("Genes.",10);
		ABBREV_TO_BOOKNUM.put("Gen",10);
		
		ABBREV_TO_BOOKNUM.put("Eksodosy",20);
		ABBREV_TO_BOOKNUM.put("Eksodus",20);
		ABBREV_TO_BOOKNUM.put("Eks.",20);
		ABBREV_TO_BOOKNUM.put("Eksod.",20);
		ABBREV_TO_BOOKNUM.put("Eks",20);
		
		ABBREV_TO_BOOKNUM.put("Levitikosy",30);
		ABBREV_TO_BOOKNUM.put("Levitikus",30);
		ABBREV_TO_BOOKNUM.put("Lev.",30);
		ABBREV_TO_BOOKNUM.put("Levit.",30);
		ABBREV_TO_BOOKNUM.put("Lev",30);
		
		ABBREV_TO_BOOKNUM.put("Nomery",40);
		ABBREV_TO_BOOKNUM.put("Nombres",40);
		ABBREV_TO_BOOKNUM.put("Nom.",40);
		ABBREV_TO_BOOKNUM.put("Nomb.",40);
		ABBREV_TO_BOOKNUM.put("Nom",40);
		
		ABBREV_TO_BOOKNUM.put("Deoteronomia",50);
		ABBREV_TO_BOOKNUM.put("Deoteronomy",50);
		ABBREV_TO_BOOKNUM.put("Deo.",50);
		ABBREV_TO_BOOKNUM.put("Deot.",50);
		ABBREV_TO_BOOKNUM.put("Deot",50);
		ABBREV_TO_BOOKNUM.put("Deo",50);
		
		ABBREV_TO_BOOKNUM.put("Josoa",60);
		ABBREV_TO_BOOKNUM.put("Josua",60);
		ABBREV_TO_BOOKNUM.put("Jos.",60);
		ABBREV_TO_BOOKNUM.put("Jos",60);
		
		ABBREV_TO_BOOKNUM.put("Mpitsara",70);
		ABBREV_TO_BOOKNUM.put("Mpits.",70);
		ABBREV_TO_BOOKNUM.put("Mpitsar.",70);
		ABBREV_TO_BOOKNUM.put("Mpits",70);
		
		ABBREV_TO_BOOKNUM.put("Rota",80);
		ABBREV_TO_BOOKNUM.put("Rota.",80);
		ABBREV_TO_BOOKNUM.put("Rot.",80);
		ABBREV_TO_BOOKNUM.put("Ro.",80);
		ABBREV_TO_BOOKNUM.put("Rot",80);
		
		ABBREV_TO_BOOKNUM.put("1 Samoela",90);
		ABBREV_TO_BOOKNUM.put("Voalohany Samoela",90);
		ABBREV_TO_BOOKNUM.put("1 Sam.",90);
		ABBREV_TO_BOOKNUM.put("1Sam.",90);
		ABBREV_TO_BOOKNUM.put("1Sa.",90);
		ABBREV_TO_BOOKNUM.put("1 Sam",90);
		ABBREV_TO_BOOKNUM.put("I Samoela",90);
		ABBREV_TO_BOOKNUM.put("I Sam.",90);
		
		ABBREV_TO_BOOKNUM.put("2 Samoela",100);
		ABBREV_TO_BOOKNUM.put("Faharoa Samoela",100);
		ABBREV_TO_BOOKNUM.put("2 Sam.",100);
		ABBREV_TO_BOOKNUM.put("2Sam.",100);
		ABBREV_TO_BOOKNUM.put("2Sa.",100);
		ABBREV_TO_BOOKNUM.put("2 Sam",100);
		ABBREV_TO_BOOKNUM.put("II Samoela",100);
		ABBREV_TO_BOOKNUM.put("II Sam.",100);
		
		ABBREV_TO_BOOKNUM.put("1 Mpanjaka",110);
		ABBREV_TO_BOOKNUM.put("1 Mpanj.",110);
		ABBREV_TO_BOOKNUM.put("1Mpanj.",110);
		ABBREV_TO_BOOKNUM.put("1Mp.",110);
		ABBREV_TO_BOOKNUM.put("1 Mpanj",110);
		ABBREV_TO_BOOKNUM.put("I Mpanjaka",110);
		ABBREV_TO_BOOKNUM.put("I Mpanj.",110);
		
		ABBREV_TO_BOOKNUM.put("2 Mpanjaka",120);
		ABBREV_TO_BOOKNUM.put("2 Mpanj.",120);
		ABBREV_TO_BOOKNUM.put("2Mpanj.",120);
		ABBREV_TO_BOOKNUM.put("2Mp.",120);
		ABBREV_TO_BOOKNUM.put("2 Mpanj",120);
		ABBREV_TO_BOOKNUM.put("II Mpanjaka",120);
		ABBREV_TO_BOOKNUM.put("II Mpanj.",120);
		
		ABBREV_TO_BOOKNUM.put("1 Tantara",130);
		ABBREV_TO_BOOKNUM.put("1 Tant.",130);
		ABBREV_TO_BOOKNUM.put("1Tant.",130);
		ABBREV_TO_BOOKNUM.put("1 Tant",130);
		ABBREV_TO_BOOKNUM.put("I Tantara",130);
		ABBREV_TO_BOOKNUM.put("I Tant.",130);
		
		ABBREV_TO_BOOKNUM.put("2 Tantara",140);
		ABBREV_TO_BOOKNUM.put("2 Tant.",140);
		ABBREV_TO_BOOKNUM.put("2Tant.",140);
		ABBREV_TO_BOOKNUM.put("2 Tant",140);
		ABBREV_TO_BOOKNUM.put("II Tantara",140);
		ABBREV_TO_BOOKNUM.put("II Tant.",140);
		
		ABBREV_TO_BOOKNUM.put("Ezra",150);
		ABBREV_TO_BOOKNUM.put("Ezr.",150);
		ABBREV_TO_BOOKNUM.put("Ezr",150);
		
		ABBREV_TO_BOOKNUM.put("Nehemia",160);
		ABBREV_TO_BOOKNUM.put("Neh.",160);
		ABBREV_TO_BOOKNUM.put("Neh",160);
		
		ABBREV_TO_BOOKNUM.put("Estera",190);
		ABBREV_TO_BOOKNUM.put("Est.",190);
		ABBREV_TO_BOOKNUM.put("Est",190);
		
		ABBREV_TO_BOOKNUM.put("Joba",220);
		ABBREV_TO_BOOKNUM.put("Job.",220);
		ABBREV_TO_BOOKNUM.put("Jôb.",220);
		ABBREV_TO_BOOKNUM.put("Job",220);
		ABBREV_TO_BOOKNUM.put("Jôb",220);
		
		ABBREV_TO_BOOKNUM.put("Salamo",230);
		ABBREV_TO_BOOKNUM.put("Sal.",230);
		ABBREV_TO_BOOKNUM.put("Salamo.",230);
		ABBREV_TO_BOOKNUM.put("Sal",230);
		
		ABBREV_TO_BOOKNUM.put("Ohabolana",240);
		ABBREV_TO_BOOKNUM.put("Ohab.",240);
		ABBREV_TO_BOOKNUM.put("Ohab",240);
		
		ABBREV_TO_BOOKNUM.put("Mpitoriteny",250);
		ABBREV_TO_BOOKNUM.put("Mpitor.",250);
		ABBREV_TO_BOOKNUM.put("Mpit.",250);
		ABBREV_TO_BOOKNUM.put("Mpi.",250);
		ABBREV_TO_BOOKNUM.put("Mpitor",250);
		
		ABBREV_TO_BOOKNUM.put("Tononkiran'i Solomona",260);
		ABBREV_TO_BOOKNUM.put("Tononkira",260);
		ABBREV_TO_BOOKNUM.put("Tonon.",260);
		ABBREV_TO_BOOKNUM.put("Tononk.",260);
		ABBREV_TO_BOOKNUM.put("Tononkira.",260);
		ABBREV_TO_BOOKNUM.put("Ton.",260);
		
		ABBREV_TO_BOOKNUM.put("Isaia",290);
		ABBREV_TO_BOOKNUM.put("Isaiah",290);
		ABBREV_TO_BOOKNUM.put("Isa.",290);
		ABBREV_TO_BOOKNUM.put("Is.",290);
		ABBREV_TO_BOOKNUM.put("Isa",290);
		
		ABBREV_TO_BOOKNUM.put("Jeremia",300);
		ABBREV_TO_BOOKNUM.put("Jer.",300);
		ABBREV_TO_BOOKNUM.put("Jer",300);
		
		ABBREV_TO_BOOKNUM.put("Fitomaniana",310);
		ABBREV_TO_BOOKNUM.put("Fit.",310);
		ABBREV_TO_BOOKNUM.put("Fitom.",310);
		ABBREV_TO_BOOKNUM.put("Fit",310);
		
		ABBREV_TO_BOOKNUM.put("Ezekiela",330);
		ABBREV_TO_BOOKNUM.put("Ezek.",330);
		ABBREV_TO_BOOKNUM.put("Ezek",330);
		
		ABBREV_TO_BOOKNUM.put("Daniela",340);
		ABBREV_TO_BOOKNUM.put("Dan.",340);
		ABBREV_TO_BOOKNUM.put("Dan",340);
		
		ABBREV_TO_BOOKNUM.put("Hosea",350);
		ABBREV_TO_BOOKNUM.put("Hos.",350);
		ABBREV_TO_BOOKNUM.put("Hôs.",350);
		ABBREV_TO_BOOKNUM.put("Hos",350);
		ABBREV_TO_BOOKNUM.put("Hôs",350);
		
		ABBREV_TO_BOOKNUM.put("Joela",360);
		ABBREV_TO_BOOKNUM.put("Joel.",360);
		ABBREV_TO_BOOKNUM.put("Joel",360);
		
		ABBREV_TO_BOOKNUM.put("Amosa",370);
		ABBREV_TO_BOOKNUM.put("Amo.",370);
		ABBREV_TO_BOOKNUM.put("Amô.",370);
		ABBREV_TO_BOOKNUM.put("Amos.",370);
		ABBREV_TO_BOOKNUM.put("Amôs.",370);
		ABBREV_TO_BOOKNUM.put("Amos",370);
		ABBREV_TO_BOOKNUM.put("Amôs",370);
		
		ABBREV_TO_BOOKNUM.put("Obadia",380);
		ABBREV_TO_BOOKNUM.put("Obad.",380);
		ABBREV_TO_BOOKNUM.put("Obad",380);
		
		ABBREV_TO_BOOKNUM.put("Jona",390);
		ABBREV_TO_BOOKNUM.put("Jon.",390);
		ABBREV_TO_BOOKNUM.put("Jôn.",390);
		ABBREV_TO_BOOKNUM.put("Jon",390);
		ABBREV_TO_BOOKNUM.put("Jôn",390);
		
		ABBREV_TO_BOOKNUM.put("Mika",400);
		ABBREV_TO_BOOKNUM.put("Mik.",400);
		ABBREV_TO_BOOKNUM.put("Mik",400);
		
		ABBREV_TO_BOOKNUM.put("Nahoma",410);
		ABBREV_TO_BOOKNUM.put("Nah.",410);
		ABBREV_TO_BOOKNUM.put("Nah",410);
		
		ABBREV_TO_BOOKNUM.put("Habakoka",420);
		ABBREV_TO_BOOKNUM.put("Hab.",420);
		ABBREV_TO_BOOKNUM.put("Hab",420);
		
		ABBREV_TO_BOOKNUM.put("Zefania",430);
		ABBREV_TO_BOOKNUM.put("Zef.",430);
		ABBREV_TO_BOOKNUM.put("Zef",430);
		
		ABBREV_TO_BOOKNUM.put("Hagay",440);
		ABBREV_TO_BOOKNUM.put("Hagia",440);
		ABBREV_TO_BOOKNUM.put("Hag.",440);
		ABBREV_TO_BOOKNUM.put("Hag",440);
		
		ABBREV_TO_BOOKNUM.put("Zakaria",450);
		ABBREV_TO_BOOKNUM.put("Zak.",450);
		ABBREV_TO_BOOKNUM.put("Zak",450);
		
		ABBREV_TO_BOOKNUM.put("Malakia",460);
		ABBREV_TO_BOOKNUM.put("Mal.",460);
		ABBREV_TO_BOOKNUM.put("Mal",460);
		
		ABBREV_TO_BOOKNUM.put("Matio",470);
		ABBREV_TO_BOOKNUM.put("Matthew",470);
		ABBREV_TO_BOOKNUM.put("Mat.",470);
		ABBREV_TO_BOOKNUM.put("Matt.",470);
		ABBREV_TO_BOOKNUM.put("Mat",470);
		
		ABBREV_TO_BOOKNUM.put("Marka",480);
		ABBREV_TO_BOOKNUM.put("Mar.",480);
		ABBREV_TO_BOOKNUM.put("Mark.",480);
		ABBREV_TO_BOOKNUM.put("Mar",480);
		
		ABBREV_TO_BOOKNUM.put("Lioka",490);
		ABBREV_TO_BOOKNUM.put("Lio.",490);
		ABBREV_TO_BOOKNUM.put("Lioc.",490);
		ABBREV_TO_BOOKNUM.put("Lio",490);
		
		ABBREV_TO_BOOKNUM.put("Jaona",500);
		ABBREV_TO_BOOKNUM.put("Jao.",500);
		ABBREV_TO_BOOKNUM.put("Joan.",500);
		ABBREV_TO_BOOKNUM.put("Jao",500);
		
		ABBREV_TO_BOOKNUM.put("Asan'ny Apostoly",510);
		ABBREV_TO_BOOKNUM.put("Asa.",510);
		ABBREV_TO_BOOKNUM.put("Asan.",510);
		ABBREV_TO_BOOKNUM.put("Asa",510);
		
		ABBREV_TO_BOOKNUM.put("Romana",520);
		ABBREV_TO_BOOKNUM.put("Rom.",520);
		ABBREV_TO_BOOKNUM.put("Rôm.",520);
		ABBREV_TO_BOOKNUM.put("Rom",520);
		ABBREV_TO_BOOKNUM.put("Rôm",520);
		
		ABBREV_TO_BOOKNUM.put("1 Korintiana",530);
		ABBREV_TO_BOOKNUM.put("Voalohany Korintiana",530);
		ABBREV_TO_BOOKNUM.put("1 Kor.",530);
		ABBREV_TO_BOOKNUM.put("1 Kôr.",530);
		ABBREV_TO_BOOKNUM.put("1Kor.",530);
		ABBREV_TO_BOOKNUM.put("1 Kor",530);
		ABBREV_TO_BOOKNUM.put("I Korintiana",530);
		ABBREV_TO_BOOKNUM.put("I Kor.",530);
		
		ABBREV_TO_BOOKNUM.put("2 Korintiana",540);
		ABBREV_TO_BOOKNUM.put("Faharoa Korintiana",540);
		ABBREV_TO_BOOKNUM.put("2 Kor.",540);
		ABBREV_TO_BOOKNUM.put("2 Kôr.",540);
		ABBREV_TO_BOOKNUM.put("2Kor.",540);
		ABBREV_TO_BOOKNUM.put("2 Kor",540);
		ABBREV_TO_BOOKNUM.put("II Korintiana",540);
		ABBREV_TO_BOOKNUM.put("II Kor.",540);
		
		ABBREV_TO_BOOKNUM.put("Galatiana",550);
		ABBREV_TO_BOOKNUM.put("Gal.",550);
		ABBREV_TO_BOOKNUM.put("Gal",550);
		
		ABBREV_TO_BOOKNUM.put("Efesiana",560);
		ABBREV_TO_BOOKNUM.put("Ef.",560);
		ABBREV_TO_BOOKNUM.put("Efes.",560);
		ABBREV_TO_BOOKNUM.put("Efes",560);
		
		ABBREV_TO_BOOKNUM.put("Filipiana",570);
		ABBREV_TO_BOOKNUM.put("Fil.",570);
		ABBREV_TO_BOOKNUM.put("Filip.",570);
		ABBREV_TO_BOOKNUM.put("Filip",570);
		
		ABBREV_TO_BOOKNUM.put("Kolosiana",580);
		ABBREV_TO_BOOKNUM.put("Kol.",580);
		ABBREV_TO_BOOKNUM.put("Kôl.",580);
		ABBREV_TO_BOOKNUM.put("Kol",580);
		ABBREV_TO_BOOKNUM.put("Kôl",580);
		
		ABBREV_TO_BOOKNUM.put("1 Tesaloniana",590);
		ABBREV_TO_BOOKNUM.put("Voalohany Tesaloniana",590);
		ABBREV_TO_BOOKNUM.put("1 Tes.",590);
		ABBREV_TO_BOOKNUM.put("1Tes.",590);
		ABBREV_TO_BOOKNUM.put("1 Tes",590);
		ABBREV_TO_BOOKNUM.put("I Tesaloniana",590);
		ABBREV_TO_BOOKNUM.put("I Tes.",590);
		
		ABBREV_TO_BOOKNUM.put("2 Tesaloniana",600);
		ABBREV_TO_BOOKNUM.put("Faharoa Tesaloniana",600);
		ABBREV_TO_BOOKNUM.put("2 Tes.",600);
		ABBREV_TO_BOOKNUM.put("2Tes.",600);
		ABBREV_TO_BOOKNUM.put("2 Tes",600);
		ABBREV_TO_BOOKNUM.put("II Tesaloniana",600);
		ABBREV_TO_BOOKNUM.put("II Tes.",600);
		
		ABBREV_TO_BOOKNUM.put("1 Timoty",610);
		ABBREV_TO_BOOKNUM.put("Voalohany Timoty",610);
		ABBREV_TO_BOOKNUM.put("1 Tim.",610);
		ABBREV_TO_BOOKNUM.put("1Tim.",610);
		ABBREV_TO_BOOKNUM.put("1 Tim",610);
		ABBREV_TO_BOOKNUM.put("I Timoty",610);
		ABBREV_TO_BOOKNUM.put("I Tim.",610);
		
		ABBREV_TO_BOOKNUM.put("2 Timoty",620);
		ABBREV_TO_BOOKNUM.put("Faharoa Timoty",620);
		ABBREV_TO_BOOKNUM.put("2 Tim.",620);
		ABBREV_TO_BOOKNUM.put("2Tim.",620);
		ABBREV_TO_BOOKNUM.put("2 Tim",620);
		ABBREV_TO_BOOKNUM.put("II Timoty",620);
		ABBREV_TO_BOOKNUM.put("II Tim.",620);
		
		ABBREV_TO_BOOKNUM.put("Titosy",630);
		ABBREV_TO_BOOKNUM.put("Tit.",630);
		ABBREV_TO_BOOKNUM.put("Tit",630);
		
		ABBREV_TO_BOOKNUM.put("Filemona",640);
		ABBREV_TO_BOOKNUM.put("Filem.",640);
		ABBREV_TO_BOOKNUM.put("Filem",640);
		
		ABBREV_TO_BOOKNUM.put("Hebreo",650);
		ABBREV_TO_BOOKNUM.put("Heb.",650);
		ABBREV_TO_BOOKNUM.put("Heb",650);
		
		ABBREV_TO_BOOKNUM.put("Jakoba",660);
		ABBREV_TO_BOOKNUM.put("Jak.",660);
		ABBREV_TO_BOOKNUM.put("Jak",660);
		
		ABBREV_TO_BOOKNUM.put("1 Petera",670);
		ABBREV_TO_BOOKNUM.put("Voalohany Petera",670);
		ABBREV_TO_BOOKNUM.put("1 Pet.",670);
		ABBREV_TO_BOOKNUM.put("1Pet.",670);
		ABBREV_TO_BOOKNUM.put("1 Pet",670);
		ABBREV_TO_BOOKNUM.put("I Petera",670);
		ABBREV_TO_BOOKNUM.put("I Pet.",670);
		
		ABBREV_TO_BOOKNUM.put("2 Petera",680);
		ABBREV_TO_BOOKNUM.put("Faharoa Petera",680);
		ABBREV_TO_BOOKNUM.put("2 Pet.",680);
		ABBREV_TO_BOOKNUM.put("2Pet.",680);
		ABBREV_TO_BOOKNUM.put("2 Pet",680);
		ABBREV_TO_BOOKNUM.put("II Petera",680);
		ABBREV_TO_BOOKNUM.put("II Pet.",680);
		
		ABBREV_TO_BOOKNUM.put("1 Jaona",690);
		ABBREV_TO_BOOKNUM.put("Voalohany Jaona",690);
		ABBREV_TO_BOOKNUM.put("1 Jao.",690);
		ABBREV_TO_BOOKNUM.put("1Jao.",690);
		ABBREV_TO_BOOKNUM.put("1 Jao",690);
		ABBREV_TO_BOOKNUM.put("I Jaona",690);
		ABBREV_TO_BOOKNUM.put("I Jao.",690);
		
		ABBREV_TO_BOOKNUM.put("2 Jaona",700);
		ABBREV_TO_BOOKNUM.put("Faharoa Jaona",700);
		ABBREV_TO_BOOKNUM.put("2 Jao.",700);
		ABBREV_TO_BOOKNUM.put("2Jao.",700);
		ABBREV_TO_BOOKNUM.put("2 Jao",700);
		ABBREV_TO_BOOKNUM.put("II Jaona",700);
		ABBREV_TO_BOOKNUM.put("II Jao.",700);
		
		ABBREV_TO_BOOKNUM.put("3 Jaona",710);
		ABBREV_TO_BOOKNUM.put("Fahatelo Jaona",710);
		ABBREV_TO_BOOKNUM.put("3 Jao.",710);
		ABBREV_TO_BOOKNUM.put("3Jao.",710);
		ABBREV_TO_BOOKNUM.put("3 Jao",710);
		ABBREV_TO_BOOKNUM.put("III Jaona",710);
		ABBREV_TO_BOOKNUM.put("III Jao.",710);
		
		ABBREV_TO_BOOKNUM.put("Joda",720);
		ABBREV_TO_BOOKNUM.put("Jod.",720);
		ABBREV_TO_BOOKNUM.put("Jod",720);
		
		ABBREV_TO_BOOKNUM.put("Apokalypsy",730);
		ABBREV_TO_BOOKNUM.put("Apokalipsy",730);
		ABBREV_TO_BOOKNUM.put("Apok.",730);
		ABBREV_TO_BOOKNUM.put("Apok",730);
		
		final java.util.HashMap<String,Integer> ABBREV_NORM =
		    new java.util.HashMap<String,Integer>();
		for (java.util.Map.Entry<String,Integer> en : ABBREV_TO_BOOKNUM.entrySet()) {
			    String n = en.getKey()
			        .replace("ô","o").replace("Ô","O")
			        .replace("â","a").replace("Â","A")
			        .replace("ê","e").replace("Ê","E");
			    ABBREV_NORM.put(n, en.getValue());
		}
		
		new Thread(new Runnable() {
			    @Override public void run() {
				        try {
					            java.io.InputStream is = getAssets().open("Bible_MG65.json");
					            java.io.ByteArrayOutputStream baos =
					                new java.io.ByteArrayOutputStream();
					            byte[] buf = new byte[8192];
					            int len;
					            while((len=is.read(buf))>0) baos.write(buf,0,len);
					            is.close();
					            org.json.JSONObject root2 =
					                new org.json.JSONObject(baos.toString("UTF-8"));
					            org.json.JSONArray objects = root2.getJSONArray("objects");
					            for (int oi=0;oi<objects.length();oi++) {
						                org.json.JSONObject tbl = objects.getJSONObject(oi);
						                if(!"verses".equals(tbl.getString("name"))) continue;
						                org.json.JSONArray rows = tbl.getJSONArray("rows");
						                for (int ri=0;ri<rows.length();ri++) {
							                    org.json.JSONArray row = rows.getJSONArray(ri);
							                    int bookNum = row.getInt(0);
							                    int chapter = row.getInt(1);
							                    int verse   = row.getInt(2);
							                    String text = row.getString(3);
							                    text = text.replaceAll("<n>\\[.*?\\]</n>","")
							                               .replaceAll("<[^>]+>","")
							                               .trim();
							                    if(!bibleData.containsKey(bookNum))
							                        bibleData.put(bookNum,
							                            new java.util.HashMap<Integer,
							                                java.util.HashMap<Integer,String>>());
							                    if(!bibleData.get(bookNum).containsKey(chapter))
							                        bibleData.get(bookNum).put(chapter,
							                            new java.util.HashMap<Integer,String>());
							                    bibleData.get(bookNum).get(chapter).put(verse,text);
							                }
						                break;
						            }
					            bibleLoaded[0]=true;
					        } catch(Exception e){ bibleLoaded[0]=true; }
				    }
		}).start();
		
		final int[] bookNumResult = {-1};
		final String[] abbrevArg  = {null};
		final Runnable findBookNum = new Runnable() {
			    @Override public void run() {
				        String ref = abbrevArg[0];
				        if(ref==null){bookNumResult[0]=-1;return;}
				        for(int l=Math.min(ref.length(),10);l>=2;l--) {
					            String key=ref.substring(0,l);
					            if(ABBREV_TO_BOOKNUM.containsKey(key)) {
						                bookNumResult[0]=ABBREV_TO_BOOKNUM.get(key);
						                return;
						            }
					        }
				        String refNorm = ref
				            .replace("ô","o").replace("Ô","O")
				            .replace("â","a").replace("Â","A")
				            .replace("ê","e").replace("Ê","E");
				        for(int l=Math.min(refNorm.length(),10);l>=2;l--) {
					            String key=refNorm.substring(0,l);
					            if(ABBREV_NORM.containsKey(key)) {
						                bookNumResult[0]=ABBREV_NORM.get(key);
						                return;
						            }
					        }
				        bookNumResult[0]=-1;
				    }
		};
		
		final String[] versetsResult = {null};
		final String[] versetsArg    = {null};
		final Runnable readVersets = new Runnable() {
			    @Override public void run() {
				        String ref = versetsArg[0];
				        if(ref==null){versetsResult[0]=null;return;}
				        abbrevArg[0]=ref; findBookNum.run();
				        int bookNum = bookNumResult[0];
				        if(bookNum<0){versetsResult[0]=null;return;}
				        String afterBook=ref;
				        for(int l=Math.min(ref.length(),10);l>=2;l--) {
					            String key=ref.substring(0,l);
					            String keyNorm = key.replace("ô","o").replace("â","a")
					                .replace("ê","e");
					            if(ABBREV_TO_BOOKNUM.containsKey(key)
					                    || ABBREV_NORM.containsKey(keyNorm)) {
						                afterBook=ref.substring(l).trim();
						                break;
						            }
					        }
				        java.util.HashMap<Integer,java.util.HashMap<Integer,String>>
				            bookMap = bibleData.get(bookNum);
				        if(bookMap==null){versetsResult[0]="Tsy hita: "+ref;return;}
				        StringBuilder sb = new StringBuilder();
				        try {
					            if(afterBook.isEmpty()) {
						                java.util.HashMap<Integer,String> ch=bookMap.get(1);
						                if(ch!=null) for(int v=1;v<=Math.min(10,ch.size());v++){
							                    String t=ch.get(v);
							                    if(t!=null) sb.append("[").append(v).append("] ")
							                        .append(t).append("\n\n");
							                }
						            } else if(afterBook.contains(":")) {
						                String[] pts = afterBook.split(":",2);
						                int chapNum = Integer.parseInt(pts[0].trim());
						                String vs   = pts[1].trim();
						                java.util.HashMap<Integer,String> ch=bookMap.get(chapNum);
						                if(ch==null){versetsResult[0]="Tsy hita: "+ref;return;}
						                java.util.List<Integer> vList =
						                    new java.util.ArrayList<Integer>();
						                if(vs.contains(",")) {
							                    for(String vv:vs.split(","))
							                        try{vList.add(Integer.parseInt(vv.trim()));}
							                        catch(Exception e){}
							                } else if(vs.contains("-")) {
							                    String[] r=vs.split("-");
							                    try{
								                        int from=Integer.parseInt(r[0].trim());
								                        int to=Integer.parseInt(r[1].trim());
								                        for(int v=from;v<=to;v++) vList.add(v);
								                    }catch(Exception e){}
							                } else {
							                    try{vList.add(Integer.parseInt(vs.trim()));}
							                    catch(Exception e){}
							                }
						                for(int v:vList) {
							                    String t=ch.get(v);
							                    if(t!=null) sb.append("[").append(v).append("] ")
							                        .append(t).append("\n\n");
							                }
						            } else {
						                try{
							                    int chapNum=Integer.parseInt(afterBook.trim());
							                    java.util.HashMap<Integer,String> ch=
							                        bookMap.get(chapNum);
							                    if(ch!=null) {
								                        java.util.List<Integer> keys =
								                            new java.util.ArrayList<Integer>(ch.keySet());
								                        java.util.Collections.sort(keys);
								                        for(int v:keys) {
									                            String t=ch.get(v);
									                            if(t!=null)
									                                sb.append("[").append(v).append("] ")
									                                  .append(t).append("\n\n");
									                        }
								                    }
							                }catch(Exception e){}
						            }
					        } catch(Exception e){sb.append("Tsy hita: "+ref);}
				        versetsResult[0]=sb.toString().trim();
				    }
		};
		
		final String[] popupRefArg = {null};
		final Runnable showBiblePopup = new Runnable() {
			    @Override public void run() {
				        final String ref=popupRefArg[0];
				        if(ref==null||ref.isEmpty()) return;
				
				        final android.app.Dialog dlg =
				            new android.app.Dialog(LesonaActivity.this);
				        dlg.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
				        dlg.setCancelable(true);
				
				        LinearLayout main = new LinearLayout(LesonaActivity.this);
				        main.setOrientation(LinearLayout.VERTICAL);
				        GradientDrawable mainBg = new GradientDrawable();
				        mainBg.setColor(0xFF1a1e27);
				        mainBg.setCornerRadii(new float[]{24*D,24*D,24*D,24*D,0,0,0,0});
				        main.setBackground(mainBg);
				
				        View handle = new View(LesonaActivity.this);
				        GradientDrawable hBg2 = new GradientDrawable();
				        hBg2.setColor(C_BORDER); hBg2.setCornerRadius(4*D);
				        handle.setBackground(hBg2);
				        LinearLayout.LayoutParams hLp2 =
				            new LinearLayout.LayoutParams((int)(40*D),(int)(4*D));
				        hLp2.gravity=Gravity.CENTER_HORIZONTAL;
				        hLp2.topMargin=(int)(12*D); hLp2.bottomMargin=(int)(4*D);
				        main.addView(handle,hLp2);
				
				        LinearLayout hdr2 = new LinearLayout(LesonaActivity.this);
				        hdr2.setOrientation(LinearLayout.HORIZONTAL);
				        hdr2.setGravity(Gravity.CENTER_VERTICAL);
				        hdr2.setPadding((int)(20*D),(int)(14*D),(int)(16*D),(int)(12*D));
				        TextView bookIco2 = new TextView(LesonaActivity.this);
				        bookIco2.setText("📖"); bookIco2.setTextSize(18);
				        LinearLayout.LayoutParams icoLp2 =
				            new LinearLayout.LayoutParams(
				                LinearLayout.LayoutParams.WRAP_CONTENT,
				                LinearLayout.LayoutParams.WRAP_CONTENT);
				        icoLp2.setMargins(0,0,(int)(10*D),0);
				        hdr2.addView(bookIco2,icoLp2);
				        TextView refTitle2 = new TextView(LesonaActivity.this);
				        refTitle2.setText(ref); refTitle2.setTextColor(C_TEXT);
				        refTitle2.setTextSize(16); refTitle2.setTypeface(Typeface.DEFAULT_BOLD);
				        hdr2.addView(refTitle2, new LinearLayout.LayoutParams(
				            0,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
				        TextView btnClose2 = new TextView(LesonaActivity.this);
				        btnClose2.setText("✕"); btnClose2.setTextColor(C_MUTED);
				        btnClose2.setTextSize(16); btnClose2.setGravity(Gravity.CENTER);
				        btnClose2.setPadding((int)(8*D),(int)(4*D),(int)(8*D),(int)(4*D));
				        btnClose2.setClickable(true); btnClose2.setFocusable(true);
				        btnClose2.setOnClickListener(new View.OnClickListener() {
					            @Override public void onClick(View v){dlg.dismiss();}
					        });
				        hdr2.addView(btnClose2, new LinearLayout.LayoutParams(
				            LinearLayout.LayoutParams.WRAP_CONTENT,
				            LinearLayout.LayoutParams.WRAP_CONTENT));
				        main.addView(hdr2);
				
				        View sep4 = new View(LesonaActivity.this);
				        sep4.setBackgroundColor(C_BORDER);
				        main.addView(sep4, new LinearLayout.LayoutParams(
				            LinearLayout.LayoutParams.MATCH_PARENT,(int)(1*D)));
				
				        final android.widget.ScrollView sv2 =
				            new android.widget.ScrollView(LesonaActivity.this);
				        sv2.setVerticalScrollBarEnabled(false);
				        sv2.setBackgroundColor(0xFF1a1e27);
				        final LinearLayout body2 = new LinearLayout(LesonaActivity.this);
				        body2.setOrientation(LinearLayout.VERTICAL);
				        body2.setPadding((int)(20*D),(int)(16*D),(int)(20*D),(int)(32*D));
				        TextView loading2 = new TextView(LesonaActivity.this);
				        loading2.setText("Miandry...");
				        loading2.setTextColor(C_MUTED); loading2.setTextSize(14);
				        loading2.setGravity(Gravity.CENTER);
				        body2.addView(loading2);
				        sv2.addView(body2, new LinearLayout.LayoutParams(
				            LinearLayout.LayoutParams.MATCH_PARENT,
				            LinearLayout.LayoutParams.WRAP_CONTENT));
				        int maxH=(int)(getResources()
				            .getDisplayMetrics().heightPixels*0.65f);
				        LinearLayout.LayoutParams svLp2 =
				            new LinearLayout.LayoutParams(
				                LinearLayout.LayoutParams.MATCH_PARENT,maxH);
				        main.addView(sv2,svLp2);
				
				        dlg.setContentView(main);
				        android.view.Window w2=dlg.getWindow();
				        if(w2!=null) {
					            w2.setLayout(
					                android.view.WindowManager.LayoutParams.MATCH_PARENT,
					                android.view.WindowManager.LayoutParams.WRAP_CONTENT);
					            w2.setGravity(Gravity.BOTTOM);
					            w2.setBackgroundDrawable(
					                new android.graphics.drawable.ColorDrawable(
					                    android.graphics.Color.TRANSPARENT));
					        }
				        dlg.show();
				
				        new Thread(new Runnable() {
					            @Override public void run() {
						                int waited=0;
						                while(!bibleLoaded[0] && waited<3000) {
							                    try{Thread.sleep(50);}catch(Exception e){}
							                    waited+=50;
							                }
						                versetsArg[0]=ref; readVersets.run();
						                final String result=versetsResult[0];
						                runOnUiThread(new Runnable() {
							                    @Override public void run() {
								                        body2.removeAllViews();
								                        if(result==null||result.isEmpty()) {
									                            TextView noRes =
									                                new TextView(LesonaActivity.this);
									                            noRes.setText("Tsy hita: "+ref);
									                            noRes.setTextColor(C_MUTED);
									                            noRes.setTextSize(14);
									                            body2.addView(noRes); return;
									                        }
								                        String[] lines2=result.split("\n\n");
								                        for(String line2:lines2) {
									                            if(line2.trim().isEmpty()) continue;
									                            TextView vTv2 =
									                                new TextView(LesonaActivity.this);
									                            vTv2.setTextColor(C_TEXT);
									                            vTv2.setTextSize(fontSize[0]);
									                            vTv2.setLineSpacing(0,1.7f);
									                            if(android.os.Build.VERSION.SDK_INT>=23) {
										                                vTv2.setBreakStrategy(
										                                    android.text.Layout.BREAK_STRATEGY_SIMPLE);
										                                vTv2.setHyphenationFrequency(
										                                    android.text.Layout.HYPHENATION_FREQUENCY_NONE);
										                            }
									                            if(android.os.Build.VERSION.SDK_INT>=26)
									                                vTv2.setJustificationMode(
									                                    android.text.Layout
									                                    .JUSTIFICATION_MODE_INTER_WORD);
									                            android.text.SpannableStringBuilder ss2 =
									                                new android.text.SpannableStringBuilder(
									                                    line2.trim());
									                            java.util.regex.Matcher m2 =
									                                java.util.regex.Pattern
									                                    .compile("^\\[(\\d+)\\]")
									                                    .matcher(ss2);
									                            if(m2.find()) {
										                                ss2.setSpan(
										                                    new android.text.style
										                                        .ForegroundColorSpan(C_BLUE),
										                                    m2.start(),m2.end(),
										                                    android.text.Spannable
										                                        .SPAN_EXCLUSIVE_EXCLUSIVE);
										                                ss2.setSpan(
										                                    new android.text.style.StyleSpan(
										                                        android.graphics.Typeface.BOLD),
										                                    m2.start(),m2.end(),
										                                    android.text.Spannable
										                                        .SPAN_EXCLUSIVE_EXCLUSIVE);
										                            }
									                            vTv2.setText(ss2);
									                            LinearLayout.LayoutParams vlp2 =
									                                new LinearLayout.LayoutParams(
									                                    LinearLayout.LayoutParams.MATCH_PARENT,
									                                    LinearLayout.LayoutParams.WRAP_CONTENT);
									                            vlp2.bottomMargin=(int)(12*D);
									                            body2.addView(vTv2,vlp2);
									                        }
								                    }
							                });
						            }
					        }).start();
				    }
		};
		
		final android.text.SpannableStringBuilder[] spanResult =
		    new android.text.SpannableStringBuilder[1];
		final boolean[] spanHasLink = {false};
		final String[] spanTextArg = {null};
		final Runnable buildSpan = new Runnable() {
			    @Override public void run() {
				        String text=spanTextArg[0];
				        if(text==null){
					            spanResult[0]=new android.text.SpannableStringBuilder("");
					            spanHasLink[0]=false;
					            return;
					        }
				        java.util.List<int[]>  positions = new java.util.ArrayList<int[]>();
				        java.util.List<String> refs2 = new java.util.ArrayList<String>();
				        java.util.regex.Matcher m3 =
				            java.util.regex.Pattern.compile("\\[([^\\]]+)\\]").matcher(text);
				        while(m3.find()) {
					            String inner=m3.group(1);
					            if(inner!=null && inner.trim().length()>0) {
						                abbrevArg[0]=inner; findBookNum.run();
						                if(bookNumResult[0]>0) {
							                    positions.add(new int[]{m3.start(),m3.end()});
							                    refs2.add(inner);
							                }
						            }
					        }
				        android.text.SpannableStringBuilder sb3 =
				            new android.text.SpannableStringBuilder(text);
				        for(int i=positions.size()-1;i>=0;i--) {
					            int start2=positions.get(i)[0];
					            int end2  =positions.get(i)[1];
					            final String ref2=refs2.get(i);
					            sb3.replace(start2,end2,ref2);
					            int ne=start2+ref2.length();
					            sb3.setSpan(
					                new android.text.style.ForegroundColorSpan(C_LINK),
					                start2,ne,
					                android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					            sb3.setSpan(
					                new android.text.style.StyleSpan(
					                    android.graphics.Typeface.BOLD),
					                start2,ne,
					                android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					            sb3.setSpan(
					                new android.text.style.UnderlineSpan(),
					                start2,ne,
					                android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					            sb3.setSpan(
					                new android.text.style.ClickableSpan() {
						                    @Override public void onClick(View v2) {
							                        popupRefArg[0]=ref2;
							                        showBiblePopup.run();
							                    }
						                    @Override public void updateDrawState(
						                            android.text.TextPaint ds2) {
							                        ds2.setColor(C_LINK);
							                        ds2.setUnderlineText(true);
							                    }
						                },
					                start2,ne,
					                android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					        }
				        spanResult[0]=sb3;
				        spanHasLink[0]=!positions.isEmpty();
				    }
		};
		
		// ══════════════════════════════════════════════════════════════
		// NOTE : la justification (p / tsianjery / question) et le clic
		// des références bibliques (BaseMovementMethod) sont appliqués
		// directement à chaque TextView concerné plus bas dans le code,
		// sans passer par un objet/interface partagé (compatibilité
		// Sketchware — pas de déclaration de classe/interface possible).
		// ══════════════════════════════════════════════════════════════
		
		LinearLayout root3 = new LinearLayout(this);
		root3.setOrientation(LinearLayout.VERTICAL);
		root3.setBackgroundColor(C_BG);
		setContentView(root3);
		final LinearLayout rootRef3 = root3;
		
		LinearLayout toolbar3 = new LinearLayout(this);
		toolbar3.setOrientation(LinearLayout.HORIZONTAL);
		toolbar3.setGravity(Gravity.CENTER_VERTICAL);
		toolbar3.setBackgroundColor(0xFF141720);
		toolbar3.setPadding((int)(4*D),(int)(6*D),(int)(16*D),(int)(6*D));
		
		TextView btnBack3 = new TextView(this);
		btnBack3.setText("‹"); btnBack3.setTextColor(C_GOLD);
		btnBack3.setTextSize(28); btnBack3.setTypeface(Typeface.DEFAULT_BOLD);
		btnBack3.setGravity(Gravity.CENTER);
		btnBack3.setPadding((int)(12*D),0,(int)(12*D),0);
		btnBack3.setClickable(true); btnBack3.setFocusable(true);
		btnBack3.setOnClickListener(new View.OnClickListener() {
			    @Override public void onClick(View v3){finish();}
		});
		toolbar3.addView(btnBack3, new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.WRAP_CONTENT,
		    LinearLayout.LayoutParams.WRAP_CONTENT));
		
		String lastSeg3=lessonaFolder!=null
		    ?(lessonaFolder.contains("/")
		        ?lessonaFolder.substring(lessonaFolder.lastIndexOf("/")+1)
		        :lessonaFolder)
		    :"";
		int lNum3=-1; String lTitle3=lastSeg3;
		if(lastSeg3.contains("_")) {
			    try{lNum3=Integer.parseInt(
				        lastSeg3.substring(0,lastSeg3.indexOf("_")));}
			    catch(Exception e){}
			    lTitle3=lastSeg3.substring(lastSeg3.indexOf("_")+1);
		}
		if(lNum3>0 && !modeTsianjery) {
			    TextView lBadge3 = new TextView(this);
			    lBadge3.setText("L"+lNum3); lBadge3.setTextColor(C_GOLD);
			    lBadge3.setTextSize(12); lBadge3.setTypeface(Typeface.DEFAULT_BOLD);
			    lBadge3.setGravity(Gravity.CENTER);
			    GradientDrawable lbBg3 = new GradientDrawable();
			    lbBg3.setColor(0xFF251e0e); lbBg3.setCornerRadius(8*D);
			    lbBg3.setStroke((int)(1*D),0xFF7a5e28);
			    lBadge3.setBackground(lbBg3);
			    lBadge3.setPadding((int)(8*D),(int)(4*D),(int)(8*D),(int)(4*D));
			    LinearLayout.LayoutParams badgeLp3 =
			        new LinearLayout.LayoutParams(
			            LinearLayout.LayoutParams.WRAP_CONTENT,
			            LinearLayout.LayoutParams.WRAP_CONTENT);
			    badgeLp3.setMargins(0,0,(int)(10*D),0);
			    toolbar3.addView(lBadge3,badgeLp3);
		}
		
		final TextView toolbarTitleTv3 = new TextView(this);
		toolbarTitleTv3.setText(modeTsianjery?"✨ Tsianjery":lTitle3);
		toolbarTitleTv3.setTextColor(C_TEXT);
		toolbarTitleTv3.setTextSize(14);
		toolbarTitleTv3.setTypeface(Typeface.DEFAULT_BOLD);
		toolbarTitleTv3.setSingleLine(true);
		toolbarTitleTv3.setEllipsize(android.text.TextUtils.TruncateAt.END);
		toolbar3.addView(toolbarTitleTv3, new LinearLayout.LayoutParams(
		    0,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
		
		final TextView btnA3 = new TextView(this);
		btnA3.setText("A+"); btnA3.setTextColor(C_GOLD);
		btnA3.setTextSize(13); btnA3.setTypeface(Typeface.DEFAULT_BOLD);
		btnA3.setGravity(Gravity.CENTER);
		btnA3.setPadding((int)(10*D),(int)(5*D),(int)(10*D),(int)(5*D));
		btnA3.setClickable(true); btnA3.setFocusable(true);
		GradientDrawable aBg3 = new GradientDrawable();
		aBg3.setColor(0xFF1e1e2e); aBg3.setCornerRadius(8*D);
		aBg3.setStroke((int)(1.5f*D),C_GOLD);
		btnA3.setBackground(aBg3);
		toolbar3.addView(btnA3, new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.WRAP_CONTENT,
		    LinearLayout.LayoutParams.WRAP_CONTENT));
		
		View toolbarSep3 = new View(this);
		toolbarSep3.setBackgroundColor(C_BORDER);
		root3.addView(toolbar3, new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.MATCH_PARENT,
		    LinearLayout.LayoutParams.WRAP_CONTENT));
		root3.addView(toolbarSep3, new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.MATCH_PARENT,(int)(1*D)));
		
		final LinearLayout subBar3 = new LinearLayout(this);
		subBar3.setOrientation(LinearLayout.HORIZONTAL);
		subBar3.setGravity(Gravity.CENTER_VERTICAL);
		subBar3.setBackgroundColor(0xF0141720);
		subBar3.setPadding((int)(16*D),(int)(8*D),(int)(16*D),(int)(8*D));
		subBar3.setVisibility(View.GONE);
		final TextView subBarTitle3 = new TextView(this);
		subBarTitle3.setTextColor(C_TEXT); subBarTitle3.setTextSize(13);
		subBarTitle3.setTypeface(Typeface.create("serif",Typeface.BOLD));
		subBarTitle3.setSingleLine(true);
		subBarTitle3.setEllipsize(android.text.TextUtils.TruncateAt.END);
		subBar3.addView(subBarTitle3, new LinearLayout.LayoutParams(
		    0,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
		final TextView subBarDate3 = new TextView(this);
		subBarDate3.setTextColor(C_GOLD); subBarDate3.setTextSize(11);
		subBarDate3.setTypeface(Typeface.DEFAULT_BOLD);
		subBar3.addView(subBarDate3, new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.WRAP_CONTENT,
		    LinearLayout.LayoutParams.WRAP_CONTENT));
		final View subBarSep3 = new View(this);
		subBarSep3.setBackgroundColor(C_BORDER);
		subBarSep3.setVisibility(View.GONE);
		root3.addView(subBar3, new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.MATCH_PARENT,
		    LinearLayout.LayoutParams.WRAP_CONTENT));
		root3.addView(subBarSep3, new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.MATCH_PARENT,(int)(1*D)));
		
		btnA3.setOnClickListener(new View.OnClickListener(){
			    @Override public void onClick(View v){
				        final android.app.Dialog dlg5 =
				            new android.app.Dialog(LesonaActivity.this);
				        dlg5.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
				        dlg5.setCancelable(true);
				        LinearLayout main5 = new LinearLayout(LesonaActivity.this);
				        main5.setOrientation(LinearLayout.VERTICAL);
				        main5.setPadding((int)(24*D),(int)(20*D),
				            (int)(24*D),(int)(32*D));
				        GradientDrawable bg5 = new GradientDrawable();
				        bg5.setColor(0xFF1a1e27);
				        bg5.setCornerRadii(new float[]{24*D,24*D,24*D,24*D,0,0,0,0});
				        main5.setBackground(bg5);
				        View ind5=new View(LesonaActivity.this);
				        LinearLayout.LayoutParams inLp5 =
				            new LinearLayout.LayoutParams((int)(40*D),(int)(4*D));
				        inLp5.gravity=Gravity.CENTER_HORIZONTAL;
				        inLp5.bottomMargin=(int)(20*D);
				        GradientDrawable inBg5 = new GradientDrawable();
				        inBg5.setColor(0xFF555566); inBg5.setCornerRadius(4*D);
				        ind5.setBackground(inBg5);
				        main5.addView(ind5,inLp5);
				        TextView title5 = new TextView(LesonaActivity.this);
				        title5.setText("Haben'ny Soratra");
				        title5.setTextSize(16); title5.setTextColor(C_WHITE);
				        title5.setTypeface(Typeface.DEFAULT_BOLD);
				        title5.setGravity(Gravity.CENTER_HORIZONTAL);
				        LinearLayout.LayoutParams ttLp5 =
				            new LinearLayout.LayoutParams(
				                LinearLayout.LayoutParams.MATCH_PARENT,
				                LinearLayout.LayoutParams.WRAP_CONTENT);
				        ttLp5.bottomMargin=(int)(20*D);
				        main5.addView(title5,ttLp5);
				        final TextView preview5 = new TextView(LesonaActivity.this);
				        preview5.setText("Ny teninao dia nataoko tao am-poko.");
				        preview5.setTextColor(C_MUTED);
				        preview5.setTextSize(fontSize[0]);
				        preview5.setGravity(Gravity.CENTER);
				        preview5.setTypeface(Typeface.create("serif",Typeface.ITALIC));
				        LinearLayout.LayoutParams pvLp5 =
				            new LinearLayout.LayoutParams(
				                LinearLayout.LayoutParams.MATCH_PARENT,
				                LinearLayout.LayoutParams.WRAP_CONTENT);
				        pvLp5.bottomMargin=(int)(16*D);
				        main5.addView(preview5,pvLp5);
				        final android.widget.SeekBar seek5 =
				            new android.widget.SeekBar(LesonaActivity.this);
				        seek5.setMax(60);
				        seek5.setProgress(fontSize[0]);
				        seek5.setOnSeekBarChangeListener(
				            new android.widget.SeekBar.OnSeekBarChangeListener(){
					                @Override public void onProgressChanged(
					                        android.widget.SeekBar sb5,
					                        int progress,boolean fromUser){
						                    if(progress<12) progress=12;
						                    if(progress>32) progress=32;
						                    preview5.setTextSize(progress);
						                    fontSize[0]=progress;
						                    for(android.widget.TextView tv5:contentViews)
						                        tv5.setTextSize(progress);
						                }
					                @Override public void onStartTrackingTouch(
					                    android.widget.SeekBar sb5){}
					                @Override public void onStopTrackingTouch(
					                    android.widget.SeekBar sb5){}
					            });
				        LinearLayout.LayoutParams skLp5 =
				            new LinearLayout.LayoutParams(
				                LinearLayout.LayoutParams.MATCH_PARENT,
				                LinearLayout.LayoutParams.WRAP_CONTENT);
				        skLp5.bottomMargin=(int)(8*D);
				        main5.addView(seek5,skLp5);
				        dlg5.setContentView(main5);
				        android.view.Window w5=dlg5.getWindow();
				        if(w5!=null){
					            w5.setLayout(
					                android.view.WindowManager.LayoutParams.MATCH_PARENT,
					                android.view.WindowManager.LayoutParams.WRAP_CONTENT);
					            w5.setGravity(Gravity.BOTTOM);
					            w5.setBackgroundDrawable(
					                new android.graphics.drawable.ColorDrawable(
					                    android.graphics.Color.TRANSPARENT));
					        }
				        dlg5.show();
				    }
		});
		
		if (modeTsianjery) {
			    final String[] tFolders3 = getIntent()
			        .getStringArrayExtra("tsianjery_folders");
			    final String[] tNames3   = getIntent()
			        .getStringArrayExtra("tsianjery_names");
			    if(tFolders3==null||tNames3==null){finish();return;}
			
			    LinearLayout tsContainer3 = new LinearLayout(this);
			    tsContainer3.setOrientation(LinearLayout.VERTICAL);
			    tsContainer3.setPadding((int)(16*D),(int)(16*D),(int)(16*D),(int)(32*D));
			
			    final java.util.Comparator<String> dc3 =
			        new java.util.Comparator<String>() {
				            @Override public int compare(String a2, String b2){
					                return dk3(a2)-dk3(b2);}
				            private int dk3(String n3){
					                try{String s3=n3.replace(".txt","");
						                    String[] p3=s3.split("_");
						                    if(p3.length==3)
						                        return Integer.parseInt(p3[2])*10000
						                             + Integer.parseInt(p3[1])*100
						                             + Integer.parseInt(p3[0]);
						                }catch(Exception e3){}return 0;}
				        };
			
			    java.util.regex.Pattern TAG_RE3 = java.util.regex.Pattern
			        .compile("^<([a-zA-Z\\-]+)>(.*)</\\1>$");
			
			    for(int i3=0;i3<tFolders3.length;i3++) {
				        final String fp3=tFolders3[i3];
				        final String ln3=tNames3[i3];
				
				        java.util.LinkedHashSet<String> afSet3 =
				            new java.util.LinkedHashSet<String>();
				        try{String[] t3=getAssets().list(fp3);
					            if(t3!=null) for(String s3:t3) afSet3.add(s3);}
				        catch(Exception e3){}
				        java.io.File ld3=new java.io.File(getFilesDir(),fp3);
				        if(ld3.exists()){String[] t3=ld3.list();
					            if(t3!=null) for(String s3:t3) afSet3.add(s3);}
				
				        java.util.List<String> txts3 = new java.util.ArrayList<String>();
				        for(String f3:afSet3) if(f3.endsWith(".txt")) txts3.add(f3);
				        java.util.Collections.sort(txts3,dc3);
				
				        String sabataTxt3="";
				        if(!txts3.isEmpty()) {
					            try{
						                java.io.BufferedReader rd3;
						                java.io.File lf3=new java.io.File(
						                    getFilesDir(),fp3+"/"+txts3.get(0));
						                if(lf3.exists())
						                    rd3=new java.io.BufferedReader(
						                        new java.io.InputStreamReader(
						                            new java.io.FileInputStream(lf3),"UTF-8"));
						                else
						                    rd3=new java.io.BufferedReader(
						                        new java.io.InputStreamReader(
						                            getAssets().open(
						                                fp3+"/"+txts3.get(0)),"UTF-8"));
						                StringBuilder sb4=new StringBuilder();
						                String l4;
						                while((l4=rd3.readLine())!=null)
						                    sb4.append(l4).append("\n");
						                rd3.close();
						                sabataTxt3=sb4.toString();
						            }catch(Exception e3){}
					        }
				        while (sabataTxt3.length()>0 &&
				                (sabataTxt3.charAt(0)=='\uFEFF'
				                    || sabataTxt3.charAt(0)=='\u200B')) {
					            sabataTxt3 = sabataTxt3.substring(1);
					        }
				
				        String titre3=ln3,tsianjery3="",tsRef3="";
				        for(String rl3:sabataTxt3.split("\n")) {
					            String l3=rl3.trim().replace("\uFEFF","");
					            java.util.regex.Matcher tm3=TAG_RE3.matcher(l3);
					            if(!tm3.matches()) continue;
					            String tag3=tm3.group(1);
					            String v3=tm3.group(2).trim();
					            if("titre".equals(tag3)) titre3=v3;
					            if("tsianjery".equals(tag3)) {
						                java.util.regex.Matcher rm3 = java.util.regex.Pattern
						                    .compile("\\[([^\\]]+)\\]\\s*$").matcher(v3);
						                if(rm3.find()) {
							                    tsRef3=rm3.group(1);
							                    tsianjery3=v3.substring(0,rm3.start())
							                        .replaceAll("[\\u2014\\u2013\\-]\\s*$","").trim();
							                } else {
							                    tsianjery3=v3;
							                }
						            }
					        }
				
				        String lastSeg4=fp3.contains("/")
				            ?fp3.substring(fp3.lastIndexOf("/")+1):fp3;
				        int lNum4=-1;
				        if(lastSeg4.contains("_"))
				            try{lNum4=Integer.parseInt(
					                lastSeg4.substring(0,lastSeg4.indexOf("_")));}
				            catch(Exception e4){}
				
				        LinearLayout tsCard3 = new LinearLayout(this);
				        tsCard3.setOrientation(LinearLayout.VERTICAL);
				        tsCard3.setPadding((int)(18*D),(int)(18*D),(int)(18*D),(int)(18*D));
				        GradientDrawable tcbg3 = new GradientDrawable();
				        tcbg3.setColor(0xFF141820); tcbg3.setCornerRadius(16*D);
				        tcbg3.setStroke((int)(1*D),0xFF252a3d);
				        tsCard3.setBackground(tcbg3);
				        LinearLayout.LayoutParams tsCardLp3 =
				            new LinearLayout.LayoutParams(
				                LinearLayout.LayoutParams.MATCH_PARENT,
				                LinearLayout.LayoutParams.WRAP_CONTENT);
				        tsCardLp3.setMargins(0,0,0,(int)(12*D));
				
				        LinearLayout tsHeader3 = new LinearLayout(this);
				        tsHeader3.setOrientation(LinearLayout.HORIZONTAL);
				        tsHeader3.setGravity(Gravity.CENTER_VERTICAL);
				        if(lNum4>0) {
					            TextView lBadge4 = new TextView(this);
					            lBadge4.setText("L"+lNum4); lBadge4.setTextColor(C_GOLD);
					            lBadge4.setTextSize(11f); lBadge4.setTypeface(Typeface.DEFAULT_BOLD);
					            lBadge4.setGravity(Gravity.CENTER);
					            GradientDrawable lbg4 = new GradientDrawable();
					            lbg4.setColor(0xFF251e0e); lbg4.setCornerRadius(6*D);
					            lbg4.setStroke((int)(1*D),C_GDIM);
					            lBadge4.setBackground(lbg4);
					            lBadge4.setPadding((int)(7*D),(int)(3*D),(int)(7*D),(int)(3*D));
					            LinearLayout.LayoutParams blp4 =
					                new LinearLayout.LayoutParams(
					                    LinearLayout.LayoutParams.WRAP_CONTENT,
					                    LinearLayout.LayoutParams.WRAP_CONTENT);
					            blp4.setMargins(0,0,(int)(10*D),0);
					            tsHeader3.addView(lBadge4,blp4);
					        }
				        TextView tsTitre3 = new TextView(this);
				        tsTitre3.setText(titre3); tsTitre3.setTextColor(C_TEXT);
				        tsTitre3.setTextSize(14f); tsTitre3.setTypeface(Typeface.DEFAULT_BOLD);
				        tsHeader3.addView(tsTitre3, new LinearLayout.LayoutParams(
				            0,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
				        tsCard3.addView(tsHeader3);
				
				        View cSep3 = new View(this);
				        cSep3.setBackgroundColor(0xFF252a3d);
				        LinearLayout.LayoutParams csLp3 =
				            new LinearLayout.LayoutParams(
				                LinearLayout.LayoutParams.MATCH_PARENT,(int)(1*D));
				        csLp3.setMargins(0,(int)(12*D),0,(int)(12*D));
				        tsCard3.addView(cSep3,csLp3);
				
				        if(!tsianjery3.isEmpty()) {
					            LinearLayout tsBody3 = new LinearLayout(this);
					            tsBody3.setOrientation(LinearLayout.HORIZONTAL);
					            View goldLine3 = new View(this);
					            goldLine3.setBackgroundColor(C_GOLD);
					            LinearLayout.LayoutParams glLp3 =
					                new LinearLayout.LayoutParams(
					                    (int)(3*D),
					                    LinearLayout.LayoutParams.MATCH_PARENT);
					            glLp3.setMargins(0,0,(int)(12*D),0);
					            tsBody3.addView(goldLine3,glLp3);
					            LinearLayout tsInner3 = new LinearLayout(this);
					            tsInner3.setOrientation(LinearLayout.VERTICAL);
					
					            TextView tsTxt3 = new TextView(this);
					            if (android.os.Build.VERSION.SDK_INT >= 23) {
						                tsTxt3.setBreakStrategy(android.text.Layout.BREAK_STRATEGY_SIMPLE);
						                tsTxt3.setHyphenationFrequency(
						                    android.text.Layout.HYPHENATION_FREQUENCY_NONE);
						            }
					            if (android.os.Build.VERSION.SDK_INT >= 26) {
						                tsTxt3.setJustificationMode(
						                    android.text.Layout.JUSTIFICATION_MODE_INTER_WORD);
						            }
					            tsTxt3.setTextColor(0xFFc8c4d4);
					            tsTxt3.setTextSize(fontSize[0]);
					            tsTxt3.setLineSpacing(0,1.6f);
					            tsTxt3.setTypeface(Typeface.create("serif",Typeface.ITALIC));
					            tsTxt3.setText(tsianjery3);
					            contentViews.add(tsTxt3);
					            tsInner3.addView(tsTxt3);
					
					            if(!tsRef3.isEmpty()) {
						                spanTextArg[0]="["+tsRef3+"]";
						                buildSpan.run();
						                TextView tsRefTv3 = new TextView(this);
						                tsRefTv3.setText(spanResult[0]);
						                tsRefTv3.setTextColor(C_BLUE);
						                tsRefTv3.setTextSize(12.5f);
						                tsRefTv3.setTypeface(Typeface.DEFAULT_BOLD);
						                tsRefTv3.setHighlightColor(
						                    android.graphics.Color.TRANSPARENT);
						                tsRefTv3.setOnTouchListener(new View.OnTouchListener() {
							                    @Override public boolean onTouch(View vT3, MotionEvent evT3) {
								                        TextView tv3 = (TextView) vT3;
								                        CharSequence cs3 = tv3.getText();
								                        if (cs3 instanceof android.text.Spanned
								                                && evT3.getAction() == MotionEvent.ACTION_UP) {
									                            android.text.Spanned buf3 = (android.text.Spanned) cs3;
									                            android.text.Layout lay3 = tv3.getLayout();
									                            if (lay3 != null) {
										                                float x3 = evT3.getX() - tv3.getTotalPaddingLeft() + tv3.getScrollX();
										                                float y3 = evT3.getY() - tv3.getTotalPaddingTop() + tv3.getScrollY();
										                                int ln3b = lay3.getLineForVertical((int) y3);
										
										                                android.text.style.ClickableSpan[] allSp3 =
										                                    buf3.getSpans(0, buf3.length(),
										                                        android.text.style.ClickableSpan.class);
										                                android.text.style.ClickableSpan best3 = null;
										                                float bestDist3 = Float.MAX_VALUE;
										                                for (android.text.style.ClickableSpan sp3 : allSp3) {
											                                    int st3 = buf3.getSpanStart(sp3);
											                                    int en3 = buf3.getSpanEnd(sp3);
											                                    int spLnStart3 = lay3.getLineForOffset(st3);
											                                    int spLnEnd3   = lay3.getLineForOffset(en3);
											                                    if (ln3b < spLnStart3 || ln3b > spLnEnd3) continue;
											                                    float left3, right3;
											                                    if (spLnStart3 == ln3b) {
												                                        left3 = lay3.getPrimaryHorizontal(st3);
												                                    } else {
												                                        left3 = lay3.getLineLeft(ln3b);
												                                    }
											                                    if (spLnEnd3 == ln3b) {
												                                        right3 = lay3.getPrimaryHorizontal(en3);
												                                    } else {
												                                        right3 = lay3.getLineRight(ln3b);
												                                    }
											                                    if (left3 > right3) {
												                                        float t3 = left3; left3 = right3; right3 = t3;
												                                    }
											                                    if (x3 >= left3 && x3 <= right3) {
												                                        best3 = sp3; bestDist3 = 0; break;
												                                    }
											                                    float d3 = Math.min(Math.abs(x3-left3), Math.abs(x3-right3));
											                                    if (d3 < bestDist3 && d3 <= (14*D)) {
												                                        bestDist3 = d3; best3 = sp3;
												                                    }
											                                }
										                                if (best3 != null) {
											                                    best3.onClick(tv3);
											                                    return true;
											                                }
										                            }
									                        }
								                        return true;
								                    }
							                });
						                LinearLayout.LayoutParams trLp3 =
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.WRAP_CONTENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT);
						                trLp3.topMargin=(int)(6*D);
						                tsInner3.addView(tsRefTv3,trLp3);
						            }
					            tsBody3.addView(tsInner3, new LinearLayout.LayoutParams(
					                0,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
					            tsCard3.addView(tsBody3);
					        } else {
					            TextView noTs3 = new TextView(this);
					            noTs3.setText("— Tsy hita —"); noTs3.setTextColor(C_MUTED);
					            noTs3.setTextSize(13f);
					            noTs3.setTypeface(Typeface.create("serif",Typeface.ITALIC));
					            tsCard3.addView(noTs3);
					        }
				        tsContainer3.addView(tsCard3,tsCardLp3);
				    }
			
			    android.widget.ScrollView tsScr = new android.widget.ScrollView(this);
			    tsScr.setBackgroundColor(C_BG);
			    tsScr.setVerticalScrollBarEnabled(false);
			    tsScr.addView(tsContainer3, new LinearLayout.LayoutParams(
			        LinearLayout.LayoutParams.MATCH_PARENT,
			        LinearLayout.LayoutParams.WRAP_CONTENT));
			    root3.addView(tsScr, new LinearLayout.LayoutParams(
			        LinearLayout.LayoutParams.MATCH_PARENT,0,1f));
			    return;
		}
		
		java.util.LinkedHashSet<String> afSet4 =
		    new java.util.LinkedHashSet<String>();
		try{String[] t4=getAssets().list(
			        lessonaFolder!=null?lessonaFolder:"");
			    if(t4!=null) for(String s4:t4) afSet4.add(s4);}
		catch(Exception e4){}
		java.io.File ld4=new java.io.File(getFilesDir(),lessonaFolder);
		if(ld4.exists()){String[] t4=ld4.list();
			    if(t4!=null) for(String s4:t4) afSet4.add(s4);}
		
		java.util.List<String> txtFiles4 = new java.util.ArrayList<String>();
		for(String f4:afSet4) if(f4.endsWith(".txt")) txtFiles4.add(f4);
		
		final java.util.Comparator<String> dc4 =
		    new java.util.Comparator<String>() {
			        @Override public int compare(String a4,String b4){
				            return dk4(a4)-dk4(b4);}
			        private int dk4(String n4){
				            try{String s4=n4.replace(".txt","");
					                String[] p4=s4.split("_");
					                if(p4.length==3)
					                    return Integer.parseInt(p4[2])*10000
					                         + Integer.parseInt(p4[1])*100
					                         + Integer.parseInt(p4[0]);
					            }catch(Exception e4){}return 0;}
			    };
		java.util.Collections.sort(txtFiles4,dc4);
		final String[] pageFiles4=txtFiles4.toArray(new String[0]);
		
		int startIdx4=0;
		if(lessonaStartFile!=null) {
			    for(int i4=0;i4<pageFiles4.length;i4++) {
				        if(pageFiles4[i4].equals(lessonaStartFile)) {
					            startIdx4=i4; break;
					        }
				    }
		}
		final int[] currentIndex4={startIdx4};
		
		LinearLayout dotsBar4 = new LinearLayout(this);
		dotsBar4.setOrientation(LinearLayout.HORIZONTAL);
		dotsBar4.setGravity(Gravity.CENTER);
		dotsBar4.setBackgroundColor(0xFF141720);
		dotsBar4.setPadding(0,(int)(10*D),0,(int)(10*D));
		final View[] dots4 = new View[pageFiles4.length];
		for(int i4=0;i4<pageFiles4.length;i4++) {
			    View dot4=new View(this);
			    GradientDrawable dBg4 = new GradientDrawable();
			    dBg4.setShape(GradientDrawable.OVAL);
			    dBg4.setColor(i4==startIdx4?C_GOLD:C_BORDER);
			    dot4.setBackground(dBg4);
			    LinearLayout.LayoutParams dLp4 =
			        new LinearLayout.LayoutParams((int)(8*D),(int)(8*D));
			    dLp4.setMargins((int)(5*D),0,(int)(5*D),0);
			    dotsBar4.addView(dot4,dLp4);
			    dots4[i4]=dot4;
		}
		
		final android.widget.FrameLayout pageContainer4 =
		    new android.widget.FrameLayout(this);
		pageContainer4.setBackgroundColor(C_BG);
		pageContainer4.setClipChildren(true);
		root3.addView(pageContainer4, new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.MATCH_PARENT,0,1f));
		
		View dotsSep4 = new View(this);
		dotsSep4.setBackgroundColor(C_BORDER);
		root3.addView(dotsSep4, new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.MATCH_PARENT,(int)(1*D)));
		root3.addView(dotsBar4, new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.MATCH_PARENT,
		    LinearLayout.LayoutParams.WRAP_CONTENT));
		
		final Runnable updateDots4 = new Runnable() {
			    @Override public void run() {
				        for(int i4=0;i4<dots4.length;i4++) {
					            GradientDrawable d4 = new GradientDrawable();
					            d4.setShape(GradientDrawable.OVAL);
					            d4.setColor(i4==currentIndex4[0]?C_GOLD:C_BORDER);
					            dots4[i4].setBackground(d4);
					        }
				    }
		};
		
		final String[] fileContent4={null};
		final Runnable[] readTxtFile4 = new Runnable[1];
		final String[] readPathArg4 = {null};
		readTxtFile4[0] = new Runnable() {
			    @Override public void run() {
				        String path4=readPathArg4[0];
				        try{
					            java.io.BufferedReader rdr4;
					            java.io.File lf4=new java.io.File(getFilesDir(),path4);
					            if(lf4.exists())
					                rdr4=new java.io.BufferedReader(
					                    new java.io.InputStreamReader(
					                        new java.io.FileInputStream(lf4),"UTF-8"));
					            else
					                rdr4=new java.io.BufferedReader(
					                    new java.io.InputStreamReader(
					                        getAssets().open(path4),"UTF-8"));
					            StringBuilder sb5=new StringBuilder();
					            String l5;
					            while((l5=rdr4.readLine())!=null)
					                sb5.append(l5).append("\n");
					            rdr4.close();
					            String content5 = sb5.toString();
					            while (content5.length()>0 &&
					                    (content5.charAt(0)=='\uFEFF'
					                        || content5.charAt(0)=='\u200B')) {
						                content5 = content5.substring(1);
						            }
					            fileContent4[0]=content5;
					        }catch(Exception e5){fileContent4[0]="";}
				    }
		};
		
		// ══════════════════════════════════════════════════════════════
		// ── SURLIGNAGE DE TEXTE (sélection + menu "Lokoina"/"Ala loko") ──
		// Uniquement sur les tags p et question. Persisté dans
		// SharedPreferences par clé unique de TextView (fichier + tag +
		// index). Chaque highlight est stocké comme une paire (start,end)
		// dans une chaîne "start1:end1,start2:end2,...".
		// Utilise android.view.ActionMode.Callback (pas de classe imbriquée
		// "CustomSelectionActionModeCallback" qui n'existe pas telle quelle
		// et faisait échouer la compilation).
		// ══════════════════════════════════════════════════════════════
		final int C_HIGHLIGHT = 0x66FFEB3B; // jaune semi-transparent
		
		// Charge les intervalles surlignés pour une clé donnée dans hlLoadResult
		final String[] hlKeyArg = {null};
		final java.util.List<int[]> hlLoadResult = new java.util.ArrayList<int[]>();
		final Runnable loadHighlights = new Runnable() {
			    @Override public void run() {
				        hlLoadResult.clear();
				        String key = hlKeyArg[0];
				        String raw = getSharedPreferences("lesona_highlights", MODE_PRIVATE)
				            .getString(key, "");
				        if (raw.isEmpty()) return;
				        for (String part : raw.split(",")) {
					            if (part.trim().isEmpty()) continue;
					            String[] se = part.split(":");
					            if (se.length != 2) continue;
					            try {
						                int s = Integer.parseInt(se[0]);
						                int e = Integer.parseInt(se[1]);
						                hlLoadResult.add(new int[]{s, e});
						            } catch (Exception ignored) {}
					        }
				    }
		};
		
		// Sauvegarde hlSaveListArg[0] sous hlSaveKeyArg[0]
		final String[] hlSaveKeyArg = {null};
		final java.util.List<int[]>[] hlSaveListArg = new java.util.List[1];
		final Runnable saveHighlights = new Runnable() {
			    @Override public void run() {
				        StringBuilder sb = new StringBuilder();
				        for (int[] iv : hlSaveListArg[0]) {
					            if (sb.length() > 0) sb.append(",");
					            sb.append(iv[0]).append(":").append(iv[1]);
					        }
				        getSharedPreferences("lesona_highlights", MODE_PRIVATE)
				            .edit().putString(hlSaveKeyArg[0], sb.toString()).apply();
				    }
		};
		
		// Ré-applique les BackgroundColorSpan de surlignage sur hlApplyTvArg[0]
		// à partir des intervalles sauvegardés sous hlApplyKeyArg[0], en
		// préservant le texte et les ClickableSpan déjà présents.
		final TextView[] hlApplyTvArg = {null};
		final String[] hlApplyKeyArg = {null};
		final Runnable applyHighlights = new Runnable() {
			    @Override public void run() {
				        TextView tv = hlApplyTvArg[0];
				        String key = hlApplyKeyArg[0];
				        CharSequence cs = tv.getText();
				        android.text.SpannableString ss;
				        if (cs instanceof android.text.SpannableString) {
					            ss = (android.text.SpannableString) cs;
					        } else {
					            ss = new android.text.SpannableString(cs);
					        }
				        android.text.style.BackgroundColorSpan[] old =
				            ss.getSpans(0, ss.length(), android.text.style.BackgroundColorSpan.class);
				        for (android.text.style.BackgroundColorSpan o : old) ss.removeSpan(o);
				
				        hlKeyArg[0] = key;
				        loadHighlights.run();
				        for (int[] iv : hlLoadResult) {
					            int s = Math.max(0, Math.min(iv[0], ss.length()));
					            int e = Math.max(0, Math.min(iv[1], ss.length()));
					            if (e > s) {
						                ss.setSpan(new android.text.style.BackgroundColorSpan(C_HIGHLIGHT),
						                    s, e, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						            }
					        }
				        tv.setText(ss);
				    }
		};
		
		// Crée un ActionMode.Callback pour un TextView + clé donnés (menu
		// "Lokoina" / "Ala loko"). hlCallbackTvArg[0]/hlCallbackKeyArg[0]
		// doivent être renseignés avant d'appeler buildHlCallback.run().
		final TextView[] hlCallbackTvArg = {null};
		final String[] hlCallbackKeyArg = {null};
		final android.view.ActionMode.Callback[] hlCallbackResult =
		    new android.view.ActionMode.Callback[1];
		final Runnable buildHlCallback = new Runnable() {
			    @Override public void run() {
				        final TextView tv = hlCallbackTvArg[0];
				        final String key = hlCallbackKeyArg[0];
				        hlCallbackResult[0] = new android.view.ActionMode.Callback() {
					            @Override
					            public boolean onCreateActionMode(android.view.ActionMode mode,
					                    android.view.Menu menu) {
						                android.view.MenuItem it1 = menu.add(0, 1002, 1, "Lokoina");
						                it1.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
						                android.view.MenuItem it2 = menu.add(0, 1003, 2, "Ala loko");
						                it2.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
						                return true;
						            }
					            @Override
					            public boolean onPrepareActionMode(android.view.ActionMode mode,
					                    android.view.Menu menu) {
						                // Sur certains devices/API (ex. Android 8), le menu peut
						                // être réinitialisé sans repasser par onCreateActionMode :
						                // on s'assure que nos items sont toujours présents.
						                if (menu.findItem(1002) == null) {
							                    android.view.MenuItem it1 = menu.add(0, 1002, 1, "Lokoina");
							                    it1.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
							                }
						                if (menu.findItem(1003) == null) {
							                    android.view.MenuItem it2 = menu.add(0, 1003, 2, "Ala loko");
							                    it2.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
							                }
						                return true;
						            }
					            @Override
					            public boolean onActionItemClicked(android.view.ActionMode mode,
					                    android.view.MenuItem item) {
						                int start = tv.getSelectionStart();
						                int end = tv.getSelectionEnd();
						                if (start > end) { int t = start; start = end; end = t; }
						                if (start < 0 || end < 0 || start == end) {
							                    mode.finish();
							                    return true;
							                }
						
						                if (item.getItemId() == 1002) {
							                    // ── Lokoina : surligner la sélection (fusion avec
							                    // les plages existantes qui se chevauchent) ──
							                    hlKeyArg[0] = key;
							                    loadHighlights.run();
							                    java.util.List<int[]> newList = new java.util.ArrayList<int[]>();
							                    int mergedStart = start, mergedEnd = end;
							                    for (int[] iv : hlLoadResult) {
								                        if (iv[1] < mergedStart || iv[0] > mergedEnd) {
									                            newList.add(iv);
									                        } else {
									                            mergedStart = Math.min(mergedStart, iv[0]);
									                            mergedEnd = Math.max(mergedEnd, iv[1]);
									                        }
								                    }
							                    newList.add(new int[]{mergedStart, mergedEnd});
							                    hlSaveKeyArg[0] = key;
							                    hlSaveListArg[0] = newList;
							                    saveHighlights.run();
							                    hlApplyTvArg[0] = tv;
							                    hlApplyKeyArg[0] = key;
							                    applyHighlights.run();
							                    mode.finish();
							                    return true;
							
							                } else if (item.getItemId() == 1003) {
							                    // ── Ala loko : retirer le surlignage sur la
							                    // sélection (découpe les plages si besoin) ──
							                    hlKeyArg[0] = key;
							                    loadHighlights.run();
							                    java.util.List<int[]> newList2 = new java.util.ArrayList<int[]>();
							                    for (int[] iv : hlLoadResult) {
								                        if (iv[1] <= start || iv[0] >= end) {
									                            newList2.add(iv);
									                            continue;
									                        }
								                        if (iv[0] < start) newList2.add(new int[]{iv[0], start});
								                        if (iv[1] > end) newList2.add(new int[]{end, iv[1]});
								                    }
							                    hlSaveKeyArg[0] = key;
							                    hlSaveListArg[0] = newList2;
							                    saveHighlights.run();
							                    hlApplyTvArg[0] = tv;
							                    hlApplyKeyArg[0] = key;
							                    applyHighlights.run();
							                    mode.finish();
							                    return true;
							                }
						                return false;
						            }
					            @Override
					            public void onDestroyActionMode(android.view.ActionMode mode) {}
					        };
				    }
		};
		
		// Active la sélection + surlignage sur un TextView donné (p / question
		// uniquement). À appeler juste après avoir assigné le texte final
		// (setText) du TextView. hlSetupTvArg[0] = le TextView,
		// hlSetupKeyArg[0] = clé unique.
		final TextView[] hlSetupTvArg = {null};
		final String[] hlSetupKeyArg = {null};
		final Runnable setupHighlightSelection = new Runnable() {
			    @Override public void run() {
				        final TextView tv = hlSetupTvArg[0];
				        final String key = hlSetupKeyArg[0];
				        tv.setTextIsSelectable(true);
				        hlCallbackTvArg[0] = tv;
				        hlCallbackKeyArg[0] = key;
				        buildHlCallback.run();
				        tv.setCustomSelectionActionModeCallback(hlCallbackResult[0]);
				        hlApplyTvArg[0] = tv;
				        hlApplyKeyArg[0] = key;
				        applyHighlights.run();
				    }
		};
		
		// ── Gestes de lecture ──────────────────────────────────────────
		// Toute la logique tactile (swipe de page, tap sur verset,
		// sélection / surlignage) est centralisée dans ReaderTouchHandler :
		// une machine à états décide UNE fois du geste, donc plus de conflit.
		//  - swipeForwarder4         : relais du swipe (ScrollView, cadres)
		//  - swipeAndClickForwarder4 : TextView avec versets / sélection
		final ReaderTouchHandler swipeForwarder4 =
		    new ReaderTouchHandler(LesonaActivity.this, false);
		final ReaderTouchHandler swipeAndClickForwarder4 =
		    new ReaderTouchHandler(LesonaActivity.this, true);

		final android.widget.ScrollView[] currentSv4 =
		    new android.widget.ScrollView[1];
		final android.widget.ScrollView[] buildResult4 =
		    new android.widget.ScrollView[1];
		final String[] buildContent4 = {null};
		final String[] buildFile4    = {null};
		
		final java.util.regex.Pattern TAG_RE4 = java.util.regex.Pattern
		    .compile("^<([a-zA-Z\\-]+)>(.*)</\\1>$");
		
		final Runnable buildPage4 = new Runnable() {
			    @Override public void run() {
				        String content4=buildContent4[0];
				        String fileName4=buildFile4[0];
				
				        android.widget.ScrollView sv4 =
				            new android.widget.ScrollView(LesonaActivity.this);
				        sv4.setBackgroundColor(C_BG);
				        sv4.setVerticalScrollBarEnabled(false);
				        sv4.setOnTouchListener(swipeForwarder4);
				        // FIX: FOCUS_AFTER_DESCENDANTS (au lieu de FOCUS_BLOCK_DESCENDANTS)
				        // pour permettre aux EditText à l'intérieur du ScrollView de
				        // recevoir le focus au clic. FOCUS_BLOCK_DESCENDANTS empêchait
				        // totalement le clavier de s'ouvrir sur les zones de notes.
				        sv4.setDescendantFocusability(
				            android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS);
				
				        LinearLayout cl4 = new LinearLayout(LesonaActivity.this);
				        cl4.setOrientation(LinearLayout.VERTICAL);
				        cl4.setPadding(0,0,0,(int)(40*D));
				
				        if(content4==null||content4.trim().isEmpty()) {
					            TextView empty4 = new TextView(LesonaActivity.this);
					            empty4.setText("Mbola tsy misy votoaty.");
					            empty4.setTextColor(C_MUTED); empty4.setTextSize(15);
					            empty4.setGravity(Gravity.CENTER);
					            empty4.setPadding((int)(20*D),(int)(40*D),(int)(20*D),0);
					            cl4.addView(empty4);
					            sv4.addView(cl4, new LinearLayout.LayoutParams(
					                LinearLayout.LayoutParams.MATCH_PARENT,
					                LinearLayout.LayoutParams.WRAP_CONTENT));
					            buildResult4[0]=sv4; return;
					        }
				
				        android.widget.FrameLayout heroFrame4 =
				            new android.widget.FrameLayout(LesonaActivity.this);
				        heroFrame4.setOnTouchListener(swipeForwarder4);
				        android.widget.ImageView heroImg4 =
				            new android.widget.ImageView(LesonaActivity.this);
				        heroImg4.setScaleType(
				            android.widget.ImageView.ScaleType.CENTER_CROP);
				        try{
					            java.io.File il4=new java.io.File(
					                getFilesDir(),lessonaFolder+"/sary.jpeg");
					            if(il4.exists())
					                heroImg4.setImageBitmap(
					                    android.graphics.BitmapFactory
					                        .decodeFile(il4.getAbsolutePath()));
					            else
					                heroImg4.setImageBitmap(
					                    android.graphics.BitmapFactory.decodeStream(
					                        getAssets().open(
					                            lessonaFolder+"/sary.jpeg")));
					        }catch(Exception e4){
					            try{heroImg4.setImageBitmap(
						                android.graphics.BitmapFactory.decodeStream(
						                    getAssets().open("header.jpg")));}
					            catch(Exception e5){
						                heroImg4.setBackgroundColor(0xFF1a1e27);}
					        }
				        heroFrame4.addView(heroImg4,
				            new android.widget.FrameLayout.LayoutParams(
				                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
				                (int)(240*D)));
				        View gradOv4 = new View(LesonaActivity.this);
				        int[] gc4={0x00000000,0xEE111318};
				        android.graphics.drawable.GradientDrawable gdr4 =
				            new android.graphics.drawable.GradientDrawable(
				                android.graphics.drawable.GradientDrawable
				                    .Orientation.TOP_BOTTOM,gc4);
				        gradOv4.setBackground(gdr4);
				        heroFrame4.addView(gradOv4,
				            new android.widget.FrameLayout.LayoutParams(
				                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
				                (int)(240*D)));
				
				        LinearLayout heroText4 = new LinearLayout(LesonaActivity.this);
				        heroText4.setOrientation(LinearLayout.VERTICAL);
				        heroText4.setPadding((int)(20*D),0,(int)(20*D),(int)(20*D));
				        android.widget.FrameLayout.LayoutParams htlp4 =
				            new android.widget.FrameLayout.LayoutParams(
				                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
				                android.widget.FrameLayout.LayoutParams.WRAP_CONTENT);
				        htlp4.gravity=Gravity.BOTTOM;
				
				        final TextView heroDateTv4 = new TextView(LesonaActivity.this);
				        heroDateTv4.setTextColor(C_GOLD); heroDateTv4.setTextSize(13);
				        heroDateTv4.setTypeface(Typeface.DEFAULT_BOLD);
				        heroText4.addView(heroDateTv4);
				
				        final TextView heroTitreTv4 = new TextView(LesonaActivity.this);
				        heroTitreTv4.setTextColor(C_WHITE);
				        heroTitreTv4.setTextSize(20);
				        heroTitreTv4.setTypeface(Typeface.DEFAULT_BOLD);
				        heroTitreTv4.setShadowLayer(4,0,2,0xFF000000);
				        LinearLayout.LayoutParams htTvLp4 =
				            new LinearLayout.LayoutParams(
				                LinearLayout.LayoutParams.MATCH_PARENT,
				                LinearLayout.LayoutParams.WRAP_CONTENT);
				        htTvLp4.topMargin=(int)(4*D);
				        heroText4.addView(heroTitreTv4,htTvLp4);
				        heroFrame4.addView(heroText4,htlp4);
				        cl4.addView(heroFrame4, new LinearLayout.LayoutParams(
				            LinearLayout.LayoutParams.MATCH_PARENT,
				            LinearLayout.LayoutParams.WRAP_CONTENT));
				
				        LinearLayout cz4 = new LinearLayout(LesonaActivity.this);
				        cz4.setOrientation(LinearLayout.VERTICAL);
				        cz4.setPadding((int)(18*D),(int)(16*D),(int)(18*D),0);
				
				        String[] lines4=content4.split("\n");
				        final String[] parsedTitre4={""};
				        final String[] parsedDate4={""};
				        int[] qi4={0};
				        int[] pi4={0};
				
				        for(String rawLine4:lines4) {
					            String line4=rawLine4.trim().replace("\uFEFF","")
					                .replace("\u200B","");
					            if(line4.isEmpty()) continue;
					
					            java.util.regex.Matcher tagM4 = TAG_RE4.matcher(line4);
					            if(!tagM4.matches()) continue;
					            String tag4 = tagM4.group(1);
					            String val4 = tagM4.group(2).trim();
					
					            if("titre".equals(tag4)) {
						                parsedTitre4[0]=val4;
						                heroTitreTv4.setText(val4);
						
						            } else if("date".equals(tag4)) {
						                parsedDate4[0]=val4;
						                heroDateTv4.setText(val4);
						
						            } else if("section".equals(tag4)) {
						                LinearLayout secRow4 = new LinearLayout(LesonaActivity.this);
						                secRow4.setOrientation(LinearLayout.HORIZONTAL);
						                secRow4.setGravity(Gravity.CENTER_VERTICAL);
						                secRow4.setOnTouchListener(swipeForwarder4);
						                LinearLayout.LayoutParams secLp4 =
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.MATCH_PARENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT);
						                secLp4.topMargin=(int)(24*D);
						                secLp4.bottomMargin=(int)(12*D);
						                String pillTxt4=val4.contains(" — ")
						                    ?val4.split(" — ")[0].trim():val4;
						                String h2Txt4=val4.contains(" — ")
						                    ?val4.split(" — ")[1].trim():"";
						                TextView pill4 = new TextView(LesonaActivity.this);
						                pill4.setText(pillTxt4); pill4.setTextColor(C_MUTED);
						                pill4.setTextSize(10); pill4.setTypeface(Typeface.DEFAULT_BOLD);
						                GradientDrawable pillBg4 = new GradientDrawable();
						                pillBg4.setColor(0xFF222736); pillBg4.setCornerRadius(20*D);
						                pillBg4.setStroke((int)(1*D),C_BORDER);
						                pill4.setBackground(pillBg4);
						                pill4.setPadding((int)(12*D),(int)(4*D),(int)(12*D),(int)(4*D));
						                secRow4.addView(pill4);
						                View secLine4 = new View(LesonaActivity.this);
						                secLine4.setBackgroundColor(C_BORDER);
						                LinearLayout.LayoutParams slLp4 =
						                    new LinearLayout.LayoutParams(0,(int)(1*D),1f);
						                slLp4.setMargins((int)(8*D),0,(int)(8*D),0);
						                secRow4.addView(secLine4,slLp4);
						                if(!h2Txt4.isEmpty()) {
							                    TextView h2Tv4 = new TextView(LesonaActivity.this);
							                    h2Tv4.setText(h2Txt4);
							                    h2Tv4.setTextColor(C_GOLD); h2Tv4.setTextSize(24);
							                    h2Tv4.setTypeface(Typeface.DEFAULT_BOLD);
							                    secRow4.addView(h2Tv4);
							                }
						                cz4.addView(secRow4,secLp4);
						
						            } else if("velominy".equals(tag4)) {
						                LinearLayout vCard4 = new LinearLayout(LesonaActivity.this);
						                vCard4.setOrientation(LinearLayout.VERTICAL);
						                vCard4.setPadding((int)(14*D),(int)(12*D),
						                    (int)(14*D),(int)(12*D));
						                GradientDrawable vBg4 = new GradientDrawable();
						                vBg4.setColor(C_CARD); vBg4.setCornerRadius(12*D);
						                vBg4.setStroke((int)(1*D),C_BORDER);
						                vCard4.setBackground(vBg4);
						                LinearLayout.LayoutParams vcLp4 =
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.MATCH_PARENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT);
						                vcLp4.bottomMargin=(int)(12*D);
						                TextView vLabel4 = new TextView(LesonaActivity.this);
						                vLabel4.setText("📖  Velomin'ny teniny");
						                vLabel4.setTextColor(C_GOLD); vLabel4.setTextSize(18);
						                vLabel4.setTypeface(Typeface.DEFAULT_BOLD);
						                vCard4.addView(vLabel4);
						                spanTextArg[0]=val4; buildSpan.run();
						                TextView vTv4 = new TextView(LesonaActivity.this);
						                vTv4.setText(spanResult[0]);
						                vTv4.setTextColor(C_MUTED);
						                vTv4.setTextSize(fontSize[0]);
						                if (android.os.Build.VERSION.SDK_INT >= 23) {
							                    vTv4.setBreakStrategy(android.text.Layout.BREAK_STRATEGY_SIMPLE);
							                    vTv4.setHyphenationFrequency(
							                        android.text.Layout.HYPHENATION_FREQUENCY_NONE);
							                }
						                vTv4.setHighlightColor(android.graphics.Color.TRANSPARENT);
						                vTv4.setOnTouchListener(swipeAndClickForwarder4);
						                contentViews.add(vTv4);
						                LinearLayout.LayoutParams vtLp4 =
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.MATCH_PARENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT);
						                vtLp4.topMargin=(int)(6*D);
						                vCard4.addView(vTv4,vtLp4);
						                cz4.addView(vCard4,vcLp4);
						
						            } else if("note".equals(tag4)) {
						                LinearLayout nCard4 = new LinearLayout(LesonaActivity.this);
						                nCard4.setOrientation(LinearLayout.VERTICAL);
						                nCard4.setPadding((int)(14*D),(int)(12*D),
						                    (int)(14*D),(int)(12*D));
						                GradientDrawable nBg4 = new GradientDrawable();
						                nBg4.setColor(C_CARD); nBg4.setCornerRadius(12*D);
						                nBg4.setStroke((int)(1*D),C_BORDER);
						                nCard4.setBackground(nBg4);
						                LinearLayout.LayoutParams nLp4 =
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.MATCH_PARENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT);
						                nLp4.bottomMargin=(int)(12*D);
						                TextView nLabel4 = new TextView(LesonaActivity.this);
						                nLabel4.setText("📚  Hodinihina mandritra ny herinandro");
						                nLabel4.setTextColor(C_GOLD); nLabel4.setTextSize(10);
						                nLabel4.setTypeface(Typeface.DEFAULT_BOLD);
						                nCard4.addView(nLabel4);
						                spanTextArg[0]=val4; buildSpan.run();
						                TextView nTv4 = new TextView(LesonaActivity.this);
						                nTv4.setText(spanResult[0]);
						                nTv4.setTextColor(C_MUTED);
						                nTv4.setTextSize(fontSize[0]);
						                if (android.os.Build.VERSION.SDK_INT >= 23) {
							                    nTv4.setBreakStrategy(android.text.Layout.BREAK_STRATEGY_SIMPLE);
							                    nTv4.setHyphenationFrequency(
							                        android.text.Layout.HYPHENATION_FREQUENCY_NONE);
							                }
						                nTv4.setHighlightColor(android.graphics.Color.TRANSPARENT);
						                nTv4.setLineSpacing(0,1.6f);
						                nTv4.setOnTouchListener(swipeAndClickForwarder4);
						                contentViews.add(nTv4);
						                LinearLayout.LayoutParams ntLp4 =
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.MATCH_PARENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT);
						                ntLp4.topMargin=(int)(6*D);
						                nCard4.addView(nTv4,ntLp4);
						                cz4.addView(nCard4,nLp4);
						
						            } else if("tsianjery".equals(tag4)) {
						                LinearLayout tCard4 = new LinearLayout(LesonaActivity.this);
						                tCard4.setOrientation(LinearLayout.HORIZONTAL);
						                tCard4.setPadding((int)(14*D),(int)(16*D),
						                    (int)(14*D),(int)(16*D));
						                GradientDrawable tBg4 = new GradientDrawable();
						                tBg4.setColor(0xFF161b28); tBg4.setCornerRadius(12*D);
						                tBg4.setStroke((int)(1.5f*D),C_BLUE);
						                tCard4.setBackground(tBg4);
						                LinearLayout.LayoutParams tcLp4 =
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.MATCH_PARENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT);
						                tcLp4.bottomMargin=(int)(16*D);
						                View blueLine4 = new View(LesonaActivity.this);
						                blueLine4.setBackgroundColor(C_BLUE);
						                LinearLayout.LayoutParams blLp4 =
						                    new LinearLayout.LayoutParams(
						                        (int)(3*D),
						                        LinearLayout.LayoutParams.MATCH_PARENT);
						                blLp4.setMargins(0,0,(int)(12*D),0);
						                tCard4.addView(blueLine4,blLp4);
						                LinearLayout tInner4 = new LinearLayout(LesonaActivity.this);
						                tInner4.setOrientation(LinearLayout.VERTICAL);
						                TextView tLabel4 = new TextView(LesonaActivity.this);
						                tLabel4.setText("✨  Tsianjery");
						                tLabel4.setTextColor(C_BLUE); tLabel4.setTextSize(10);
						                tLabel4.setTypeface(Typeface.DEFAULT_BOLD);
						                LinearLayout.LayoutParams tlLp4 =
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.MATCH_PARENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT);
						                tlLp4.bottomMargin=(int)(8*D);
						                tInner4.addView(tLabel4,tlLp4);
						
						                String citation4=val4,tRef4="";
						                java.util.regex.Matcher rm4 = java.util.regex.Pattern
						                    .compile("\\[([^\\]]+)\\]\\s*$").matcher(val4);
						                if(rm4.find()) {
							                    tRef4=rm4.group(1);
							                    citation4=val4.substring(0,rm4.start())
							                        .replaceAll("[\\u2014\\u2013\\-]\\s*$","").trim();
							                }
						
						                TextView citTv4 = new TextView(LesonaActivity.this);
						                if (android.os.Build.VERSION.SDK_INT >= 23) {
							                    citTv4.setBreakStrategy(android.text.Layout.BREAK_STRATEGY_SIMPLE);
							                    citTv4.setHyphenationFrequency(
							                        android.text.Layout.HYPHENATION_FREQUENCY_NONE);
							                }
						                if (android.os.Build.VERSION.SDK_INT >= 26) {
							                    citTv4.setJustificationMode(
							                        android.text.Layout.JUSTIFICATION_MODE_INTER_WORD);
							                }
						                citTv4.setTextColor(0xFFd0cce8);
						                citTv4.setTextSize(fontSize[0]);
						                citTv4.setLineSpacing(0,1.6f);
						                citTv4.setTypeface(Typeface.create("serif",Typeface.ITALIC));
						                citTv4.setText(citation4);
						                citTv4.setOnTouchListener(swipeForwarder4);
						                contentViews.add(citTv4);
						                tInner4.addView(citTv4);
						
						                if(!tRef4.isEmpty()) {
							                    spanTextArg[0]="["+tRef4+"]";
							                    buildSpan.run();
							                    TextView refTv4 = new TextView(LesonaActivity.this);
							                    refTv4.setText(spanResult[0]);
							                    refTv4.setTextColor(C_BLUE);
							                    refTv4.setTextSize(24);
							                    refTv4.setTypeface(Typeface.DEFAULT_BOLD);
							                    refTv4.setHighlightColor(
							                        android.graphics.Color.TRANSPARENT);
							                    refTv4.setOnTouchListener(swipeAndClickForwarder4);
							                    LinearLayout.LayoutParams rfLp4 =
							                        new LinearLayout.LayoutParams(
							                            LinearLayout.LayoutParams.MATCH_PARENT,
							                            LinearLayout.LayoutParams.WRAP_CONTENT);
							                    rfLp4.topMargin=(int)(8*D);
							                    tInner4.addView(refTv4,rfLp4);
							                }
						                tCard4.addView(tInner4, new LinearLayout.LayoutParams(
						                    0,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
						                cz4.addView(tCard4,tcLp4);
						
						            } else if("sous-titre".equals(tag4)) {
						                LinearLayout stRow4 = new LinearLayout(LesonaActivity.this);
						                stRow4.setOrientation(LinearLayout.HORIZONTAL);
						                stRow4.setGravity(Gravity.CENTER_VERTICAL);
						                stRow4.setOnTouchListener(swipeForwarder4);
						                LinearLayout.LayoutParams stLp4 =
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.MATCH_PARENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT);
						                stLp4.topMargin=(int)(20*D);
						                stLp4.bottomMargin=(int)(10*D);
						                View stLine4 = new View(LesonaActivity.this);
						                stLine4.setBackgroundColor(C_BLUE);
						                LinearLayout.LayoutParams stLineLp4 =
						                    new LinearLayout.LayoutParams(
						                        (int)(3*D),
						                        LinearLayout.LayoutParams.MATCH_PARENT);
						                stLineLp4.setMargins(0,0,(int)(10*D),0);
						                stRow4.addView(stLine4,stLineLp4);
						                TextView stTv4 = new TextView(LesonaActivity.this);
						                stTv4.setText(val4); stTv4.setTextColor(C_LINK);
						                stTv4.setTextSize(24);
						                stTv4.setTypeface(Typeface.DEFAULT_BOLD);
						                stRow4.addView(stTv4, new LinearLayout.LayoutParams(
						                    0,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
						                cz4.addView(stRow4,stLp4);
						
						            } else if("p".equals(tag4)) {
						                spanTextArg[0]=val4; buildSpan.run();
						                TextView pTv4 = new TextView(LesonaActivity.this);
						                if (android.os.Build.VERSION.SDK_INT >= 23) {
							                    pTv4.setBreakStrategy(android.text.Layout.BREAK_STRATEGY_SIMPLE);
							                    pTv4.setHyphenationFrequency(
							                        android.text.Layout.HYPHENATION_FREQUENCY_NONE);
							                }
						                pTv4.setTextColor(C_TEXT);
						                pTv4.setTextSize(fontSize[0]);
						                pTv4.setLineSpacing(0,1.8f);
						                pTv4.setHighlightColor(0x664e7bdb);
						                pTv4.setText(spanResult[0]);
						                pTv4.setOnTouchListener(swipeAndClickForwarder4);
						                hlSetupTvArg[0] = pTv4;
						                hlSetupKeyArg[0] = (fileName4!=null?fileName4.replace(".txt",""):"x")
						                    + "_p_" + (pi4[0]++);
						                setupHighlightSelection.run();
						                contentViews.add(pTv4);
						                LinearLayout.LayoutParams pLp4 =
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.MATCH_PARENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT);
						                pLp4.bottomMargin=(int)(14*D);
						                cz4.addView(pTv4,pLp4);
						
						            } else if("question".equals(tag4)) {
						                final int qIdx4=qi4[0]++;
						                final String noteKey4="note_"
						                    +(fileName4!=null
						                        ?fileName4.replace(".txt",""):"x")
						                    +"_q"+qIdx4;
						
						                LinearLayout qCard4 = new LinearLayout(LesonaActivity.this);
						                qCard4.setOrientation(LinearLayout.VERTICAL);
						                qCard4.setPadding((int)(16*D),(int)(14*D),
						                    (int)(16*D),(int)(14*D));
						                GradientDrawable qBg4 = new GradientDrawable();
						                qBg4.setColor(0xFF1c2740);
						                qBg4.setCornerRadius(14*D);
						                qBg4.setStroke((int)(1*D),0xFF35507a);
						                qCard4.setBackground(qBg4);
						                LinearLayout.LayoutParams qLp4 =
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.MATCH_PARENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT);
						                qLp4.topMargin=(int)(4*D);
						                qLp4.bottomMargin=(int)(14*D);
						
						                spanTextArg[0]=val4; buildSpan.run();
						                TextView qTv4 = new TextView(LesonaActivity.this);
						                if (android.os.Build.VERSION.SDK_INT >= 23) {
							                    qTv4.setBreakStrategy(android.text.Layout.BREAK_STRATEGY_SIMPLE);
							                    qTv4.setHyphenationFrequency(
							                        android.text.Layout.HYPHENATION_FREQUENCY_NONE);
							                }
						                qTv4.setTextColor(C_TEXT);
						                qTv4.setTypeface(Typeface.DEFAULT_BOLD);
						                qTv4.setTextSize(fontSize[0]);
						                qTv4.setLineSpacing(0,1.6f);
						                qTv4.setHighlightColor(0x664e7bdb);
						                qTv4.setText(spanResult[0]);
						                qTv4.setOnTouchListener(swipeAndClickForwarder4);
						                hlSetupTvArg[0] = qTv4;
						                hlSetupKeyArg[0] = (fileName4!=null?fileName4.replace(".txt",""):"x")
						                    + "_question_" + qIdx4;
						                setupHighlightSelection.run();
						                contentViews.add(qTv4);
						                LinearLayout.LayoutParams qtLp4 =
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.MATCH_PARENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT);
						                qtLp4.bottomMargin=(int)(12*D);
						                qCard4.addView(qTv4,qtLp4);
						
						                LinearLayout noteZone4 = new LinearLayout(LesonaActivity.this);
						                noteZone4.setOrientation(LinearLayout.VERTICAL);
						                GradientDrawable nzBg4 = new GradientDrawable();
						                nzBg4.setColor(0xFF141c2e);
						                nzBg4.setCornerRadius(10*D);
						                nzBg4.setStroke((int)(1*D),0xFF2c4568);
						                noteZone4.setBackground(nzBg4);
						                noteZone4.setPadding((int)(10*D),(int)(6*D),
						                    (int)(10*D),(int)(6*D));
						
						                final android.widget.EditText noteEt4 =
						                    new android.widget.EditText(LesonaActivity.this);
						                noteEt4.setHint("Naoty...");
						                noteEt4.setHintTextColor(0xFF5d7aa8);
						                noteEt4.setTextColor(C_TEXT);
						                noteEt4.setTextSize(20);
						                noteEt4.setBackground(null);
						                noteEt4.setMinLines(3);
						                noteEt4.setGravity(Gravity.TOP|Gravity.START);
						                noteEt4.setInputType(
						                    android.text.InputType.TYPE_CLASS_TEXT
						                    |android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE);
						                noteEt4.setFocusable(true);
						                noteEt4.setFocusableInTouchMode(true);
						                // NOTE: on ne fait PAS clearFocus() ici — ça n'empêche
						                // pas l'ouverture auto du clavier (aucune vue ne demande
						                // le focus tant que l'utilisateur ne clique pas). Le vrai
						                // verrou était FOCUS_BLOCK_DESCENDANTS sur le ScrollView.
						
						                SharedPreferences prefs4=getSharedPreferences(
						                    "lesona_notes",MODE_PRIVATE);
						                String saved4=prefs4.getString(noteKey4,"");
						                if(!saved4.isEmpty()) noteEt4.setText(saved4);
						
						                noteZone4.addView(noteEt4,
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.MATCH_PARENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT));
						
						                final TextView saveInd4 = new TextView(LesonaActivity.this);
						                saveInd4.setText("✓ Voatahiry");
						                saveInd4.setTextColor(C_LINK);
						                saveInd4.setTextSize(10);
						                saveInd4.setGravity(Gravity.END);
						                saveInd4.setVisibility(View.INVISIBLE);
						                noteZone4.addView(saveInd4,
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.MATCH_PARENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT));
						
						                final android.os.Handler saveH4 = new android.os.Handler();
						                final Runnable[] saveR4={null};
						                saveR4[0]=new Runnable() {
							                    @Override public void run() {
								                        getSharedPreferences("lesona_notes",
								                            MODE_PRIVATE).edit()
								                            .putString(noteKey4,
								                                noteEt4.getText().toString())
								                            .apply();
								                        saveInd4.setVisibility(View.VISIBLE);
								                        saveH4.postDelayed(new Runnable(){
									                            @Override public void run(){
										                                saveInd4.setVisibility(View.INVISIBLE);}
									                        },1800);
								                    }
							                };
						                noteEt4.addTextChangedListener(
						                    new android.text.TextWatcher(){
							                        @Override public void beforeTextChanged(
							                            CharSequence s,int a,int b,int c){}
							                        @Override public void onTextChanged(
							                            CharSequence s,int a,int b,int c){
								                            saveH4.removeCallbacks(saveR4[0]);
								                            saveH4.postDelayed(saveR4[0],600);}
							                        @Override public void afterTextChanged(
							                            android.text.Editable s){}
							                    });
						
						                qCard4.addView(noteZone4,
						                    new LinearLayout.LayoutParams(
						                        LinearLayout.LayoutParams.MATCH_PARENT,
						                        LinearLayout.LayoutParams.WRAP_CONTENT));
						                cz4.addView(qCard4,qLp4);
						            }
					        }
				
				        cl4.addView(cz4, new LinearLayout.LayoutParams(
				            LinearLayout.LayoutParams.MATCH_PARENT,
				            LinearLayout.LayoutParams.WRAP_CONTENT));
				        sv4.addView(cl4, new LinearLayout.LayoutParams(
				            LinearLayout.LayoutParams.MATCH_PARENT,
				            LinearLayout.LayoutParams.WRAP_CONTENT));
				
				        subBarTitle3.setText(parsedTitre4[0]);
				        subBarDate3.setText(parsedDate4[0]);
				        buildResult4[0]=sv4;
				    }
		};
		
		final Runnable loadPage4 = new Runnable() {
			    @Override public void run() {
				        contentViews.clear();
				        String fileName4=pageFiles4[currentIndex4[0]];
				        readPathArg4[0]=lessonaFolder+"/"+fileName4;
				        readTxtFile4[0].run();
				        buildContent4[0]=fileContent4[0];
				        buildFile4[0]=fileName4;
				        buildPage4.run();
				        android.widget.ScrollView sv4=buildResult4[0];
				        if(currentSv4[0]!=null)
				            pageContainer4.removeView(currentSv4[0]);
				        pageContainer4.addView(sv4,
				            new android.widget.FrameLayout.LayoutParams(
				                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
				                android.widget.FrameLayout.LayoutParams.MATCH_PARENT));
				        sv4.scrollTo(0,0);
				        currentSv4[0]=sv4;
				
				        // Masque le clavier s'il était ouvert (ex: après rotation ou
				        // changement de page) mais NE bloque plus le focus futur des
				        // EditText, grâce au fix FOCUS_AFTER_DESCENDANTS ci-dessus.
				        android.view.View curFocus4 = getCurrentFocus();
				        if (curFocus4 != null) {
					            android.view.inputmethod.InputMethodManager imm4 =
					                (android.view.inputmethod.InputMethodManager)
					                    getSystemService(INPUT_METHOD_SERVICE);
					            if (imm4 != null) {
						                imm4.hideSoftInputFromWindow(curFocus4.getWindowToken(), 0);
						            }
					            curFocus4.clearFocus();
					        }
				        rootRef3.requestFocus();
				
				        final android.widget.ScrollView fSv4=sv4;
				        sv4.getViewTreeObserver()
				            .addOnScrollChangedListener(
				                new android.view.ViewTreeObserver
				                        .OnScrollChangedListener(){
					                    @Override public void onScrollChanged(){
						                        if(currentSv4[0]!=fSv4) return;
						                        int sy=fSv4.getScrollY();
						                        if(sy>(int)(160*D)){
							                            if(subBar3.getVisibility()!=View.VISIBLE){
								                                subBar3.setVisibility(View.VISIBLE);
								                                subBarSep3.setVisibility(View.VISIBLE);
								                            }
							                        } else {
							                            if(subBar3.getVisibility()==View.VISIBLE){
								                                subBar3.setVisibility(View.GONE);
								                                subBarSep3.setVisibility(View.GONE);
								                            }
							                        }
						                    }
					                });
				
				        updateDots4.run();
				        subBar3.setVisibility(View.GONE);
				        subBarSep3.setVisibility(View.GONE);
				    }
		};
		
		final boolean[] isAnimating4={false};
		
		final Runnable goNext4 = new Runnable() {
			    @Override public void run() {
				        if(isAnimating4[0]) return;
				        if(currentIndex4[0]>=pageFiles4.length-1) return;
				        isAnimating4[0]=true;
				
				        int w4=pageContainer4.getWidth();
				        if(w4==0) w4=getResources().getDisplayMetrics().widthPixels;
				        final int sw4=w4;
				
				        contentViews.clear();
				        String nextFile4=pageFiles4[currentIndex4[0]+1];
				        readPathArg4[0]=lessonaFolder+"/"+nextFile4;
				        readTxtFile4[0].run();
				        buildContent4[0]=fileContent4[0];
				        buildFile4[0]=nextFile4;
				        buildPage4.run();
				        final android.widget.ScrollView nextSv4=buildResult4[0];
				
				        pageContainer4.addView(nextSv4,
				            new android.widget.FrameLayout.LayoutParams(
				                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
				                android.widget.FrameLayout.LayoutParams.MATCH_PARENT));
				        nextSv4.setTranslationX(sw4);
				
				        final android.widget.ScrollView oldSv4=currentSv4[0];
				        oldSv4.animate().translationX(-sw4).setDuration(280).start();
				        nextSv4.animate().translationX(0).setDuration(280)
				            .withEndAction(new Runnable(){
					                @Override public void run(){
						                    pageContainer4.removeView(oldSv4);
						                    currentSv4[0]=nextSv4;
						                    currentIndex4[0]++;
						                    updateDots4.run();
						                    subBar3.setVisibility(View.GONE);
						                    subBarSep3.setVisibility(View.GONE);
						
						                    android.view.View curFocus5 = getCurrentFocus();
						                    if (curFocus5 != null) {
							                        android.view.inputmethod.InputMethodManager imm5 =
							                            (android.view.inputmethod.InputMethodManager)
							                                getSystemService(INPUT_METHOD_SERVICE);
							                        if (imm5 != null) {
								                            imm5.hideSoftInputFromWindow(
								                                curFocus5.getWindowToken(), 0);
								                        }
							                        curFocus5.clearFocus();
							                    }
						                    rootRef3.requestFocus();
						
						                    final android.widget.ScrollView fSv4=nextSv4;
						                    nextSv4.getViewTreeObserver()
						                        .addOnScrollChangedListener(
						                            new android.view.ViewTreeObserver
						                                    .OnScrollChangedListener(){
							                                @Override public void onScrollChanged(){
								                                    if(currentSv4[0]!=fSv4) return;
								                                    int sy=fSv4.getScrollY();
								                                    if(sy>(int)(160*D)){
									                                        subBar3.setVisibility(View.VISIBLE);
									                                        subBarSep3.setVisibility(View.VISIBLE);
									                                    } else {
									                                        subBar3.setVisibility(View.GONE);
									                                        subBarSep3.setVisibility(View.GONE);
									                                    }
								                                }
							                            });
						                    isAnimating4[0]=false;
						                }
					            }).start();
				    }
		};
		
		final Runnable goPrev4 = new Runnable() {
			    @Override public void run() {
				        if(isAnimating4[0]) return;
				        if(currentIndex4[0]<=0) return;
				        isAnimating4[0]=true;
				
				        int w4=pageContainer4.getWidth();
				        if(w4==0) w4=getResources().getDisplayMetrics().widthPixels;
				        final int sw4=w4;
				
				        contentViews.clear();
				        String prevFile4=pageFiles4[currentIndex4[0]-1];
				        readPathArg4[0]=lessonaFolder+"/"+prevFile4;
				        readTxtFile4[0].run();
				        buildContent4[0]=fileContent4[0];
				        buildFile4[0]=prevFile4;
				        buildPage4.run();
				        final android.widget.ScrollView prevSv4=buildResult4[0];
				
				        pageContainer4.addView(prevSv4,
				            new android.widget.FrameLayout.LayoutParams(
				                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
				                android.widget.FrameLayout.LayoutParams.MATCH_PARENT));
				        prevSv4.setTranslationX(-sw4);
				
				        final android.widget.ScrollView oldSv4=currentSv4[0];
				        oldSv4.animate().translationX(sw4).setDuration(280).start();
				        prevSv4.animate().translationX(0).setDuration(280)
				            .withEndAction(new Runnable(){
					                @Override public void run(){
						                    pageContainer4.removeView(oldSv4);
						                    currentSv4[0]=prevSv4;
						                    currentIndex4[0]--;
						                    updateDots4.run();
						                    subBar3.setVisibility(View.GONE);
						                    subBarSep3.setVisibility(View.GONE);
						
						                    android.view.View curFocus6 = getCurrentFocus();
						                    if (curFocus6 != null) {
							                        android.view.inputmethod.InputMethodManager imm6 =
							                            (android.view.inputmethod.InputMethodManager)
							                                getSystemService(INPUT_METHOD_SERVICE);
							                        if (imm6 != null) {
								                            imm6.hideSoftInputFromWindow(
								                                curFocus6.getWindowToken(), 0);
								                        }
							                        curFocus6.clearFocus();
							                    }
						                    rootRef3.requestFocus();
						
						                    final android.widget.ScrollView fSv4=prevSv4;
						                    prevSv4.getViewTreeObserver()
						                        .addOnScrollChangedListener(
						                            new android.view.ViewTreeObserver
						                                    .OnScrollChangedListener(){
							                                @Override public void onScrollChanged(){
								                                    if(currentSv4[0]!=fSv4) return;
								                                    int sy=fSv4.getScrollY();
								                                    if(sy>(int)(160*D)){
									                                        subBar3.setVisibility(View.VISIBLE);
									                                        subBarSep3.setVisibility(View.VISIBLE);
									                                    } else {
									                                        subBar3.setVisibility(View.GONE);
									                                        subBarSep3.setVisibility(View.GONE);
									                                    }
								                                }
							                            });
						                    isAnimating4[0]=false;
						                }
					            }).start();
				    }
		};
		
		final ReaderTouchHandler.PageNavigator pageNav4 =
		    new ReaderTouchHandler.PageNavigator() {
			@Override public void onNextPage() { goNext4.run(); }
			@Override public void onPreviousPage() { goPrev4.run(); }
		    };
		swipeForwarder4.setNavigator(pageNav4);
		swipeAndClickForwarder4.setNavigator(pageNav4);

		pageContainer4.setOnTouchListener(swipeForwarder4);
		root3.setFocusableInTouchMode(true);
		
		loadPage4.run();
		
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		
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