package com.kovacsbk.ujegy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sf.andpdf.pdfviewer.PdfViewerActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kovacsbk.ujegy.R;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CikkActivity extends Activity {
	
	HttpClient client;
	String jsonresp;
	String UjsagLink = "http://ujegyenlito.softit.hu/Egyenlito/WCF/DataProviderService.svc/GetArticles";
	ProgressBar tolt;
    String fileName;
    
    List<Cikk> LekertCikk;

//    public int getPreviousPageImageResource() { return R.drawable.left_arrow; }
//    public int getNextPageImageResource() { return R.drawable.right_arrow; }
//    public int getZoomInImageResource() { return R.drawable.zoom_in; }
//    public int getZoomOutImageResource() { return R.drawable.zoom_out; }
//    public int getPdfPasswordLayoutResource() { return R.layout.pdf_file_password; }
//    public int getPdfPageNumberResource() { return R.layout.dialog_pagenumber; }
//    public int getPdfPasswordEditField() { return R.id.etPassword; }
//    public int getPdfPasswordOkButton() { return R.id.btOK; }
//    public int getPdfPasswordExitButton() { return R.id.btExit; }
//    public int getPdfPageNumberEditField() { return R.id.pagenum_edit; }

    String[] pdflist;
    File[] imagelist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {

        }
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cikk);
		
		//ProgressBar tolt = (ProgressBar) findViewById(R.id.Tolt);
		
		Intent intent = getIntent();
		int ujsID = intent.getIntExtra("ujsagid", 0);
		UjsagLink += "?key=" + Integer.toString(ujsID);
		
		LekertCikk = new ArrayList<Cikk>();
		
		//TextView tw = (TextView) findViewById(R.id.asd);
		//tw.setText(Integer.toString(ujsID));
		
		new HttpAsyncTask().execute(UjsagLink);
		
		
		
		
		
		
		
		
//		Button pdfgomb = (Button) findViewById(R.id.gombpdf);
//	    pdfgomb.setOnClickListener(new View.OnClickListener() {
// 			@Override
// 		    public void onClick(View v) {
// 				//Intent intent = new Intent(CikkActivity.this, PdfActivity.class);
// 				//startActivity(intent);
// 				Intent intentt = new Intent(CikkActivity.this, PdfViewer.class);
// 			    intentt.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME, Environment.getExternalStorageDirectory() + "/ÚjEgyenlítõ/kiszelly1.pdf");
// 			    startActivity(intentt);
// 		    }
// 		});
	}
	


@Override
public void onRestart() {
 super.onRestart();

 //View root = findViewById(R.id.cikklista);
 //root.removeView(yourViewToRemove);
 //new HttpAsyncTask().execute(UjsagLink);
 finish();
 startActivity(getIntent());
 
}


	
	public void Cikkek(List<Cikk> lista){
		
		for (final Cikk c : lista){
			LinearLayout szetvalaszto = new LinearLayout(this);
    		szetvalaszto.setId(c.ID);
//    		szetvalaszto.setOnClickListener(new View.OnClickListener() {
//    			@Override
//    		    public void onClick(View v) {
//    				new DownloadTask().execute(c.PDFUri);
//    		    }
//    		});
    		MarginLayoutParams param1 = new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    		//param1.setMargins(5,5,5,5);
    		szetvalaszto.setOrientation(LinearLayout.HORIZONTAL);
    		szetvalaszto.setMinimumHeight(dpToPx(45));
    		szetvalaszto.setLayoutParams(param1);
    		szetvalaszto.setGravity(Gravity.CENTER_VERTICAL);
    		LinearLayout my_root = (LinearLayout) findViewById(R.id.cikklista);
            my_root.addView(szetvalaszto);
            
            
            View separator = new View(this);
            separator.setBackgroundResource(android.R.color.darker_gray);
            MarginLayoutParams param5 = new MarginLayoutParams(LayoutParams.MATCH_PARENT, dpToPx(1));
            //LayoutParams param5 = new LayoutParams(LayoutParams.MATCH_PARENT, dpTpPx(2));
            separator.setPadding(5,0,5,0);
            param5.setMargins(5,0,5,0);
            separator.setLayoutParams(param5);
            my_root.addView(separator);
            
            
            TextView author = new TextView(this);
            android.widget.LinearLayout.LayoutParams param2 = new android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0.55f);
            //param4.setMargins(5, 5, 0, 5);
            author.setOnClickListener(new View.OnClickListener() {
    			@Override
    		    public void onClick(View v) {
//    				int torolid = 1000 + c.ID;
//    	    		ImageView torol = (ImageView) findViewById(torolid);
//    	    	    torol.setVisibility(View.VISIBLE);
    				
    				new DownloadTask().execute(c.PDFUri);
    		    }
    		});
            author.setLayoutParams(param2);
            author.setText(c.Author);
            author.setTextSize(16);
            //author.setMinHeight(dpToPx(10));
            //author.setGravity(Gravity.CENTER_VERTICAL);
            LinearLayout my_root2 = (LinearLayout) findViewById(c.ID);
            my_root2.addView(author);
            
            TextView title = new TextView(this);
            android.widget.LinearLayout.LayoutParams param3 = new android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0.45f);
            title.setOnClickListener(new View.OnClickListener() {
    			@Override
    		    public void onClick(View v) {
//    				int torolid = 1000 + c.ID;
//    	    		ImageView torol = (ImageView) findViewById(torolid);
//    	    	    torol.setVisibility(View.VISIBLE);
    				
    				new DownloadTask().execute(c.PDFUri);
    		    }
    		});
            title.setLayoutParams(param3);
            title.setText(c.Title);
            title.setTextSize(16);
            //title.setMinHeight(dpToPx(45));
            //title.setGravity(Gravity.CENTER_VERTICAL);
            my_root2.addView(title);
            
            ImageView torles = new ImageView(this);
            torles.setId(1000 + c.ID);
            android.widget.LinearLayout.LayoutParams param6 = new android.widget.LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            torles.setLayoutParams(param6);
            
            String PATH = Environment.getExternalStorageDirectory() + "/ÚjEgyenlítõ/";
            String fajl = pdfFajlnev(c.PDFUri);
            File file = new File(PATH + fajl);
            torles.setBackgroundResource(android.R.drawable.ic_menu_delete);
            torles.setScaleType(ScaleType.CENTER_INSIDE);
            if(!file.exists()) {
            	torles.setVisibility(View.INVISIBLE);
            }
            else {
            torles.setOnClickListener(new View.OnClickListener() {
    			@Override
    		    public void onClick(View v) {
    				String PATH = Environment.getExternalStorageDirectory() + "/ÚjEgyenlítõ/";
    	            String fajl = pdfFajlnev(c.PDFUri);
    				File file2 = new File(PATH+fajl);
    				boolean deleted = file2.delete();
    				v.setVisibility(View.INVISIBLE);
    				Toast.makeText(getApplicationContext(), "Fájl törölve!", Toast.LENGTH_LONG).show();
    		    }
    		});
            }
            my_root2.addView(torles);
            
		}
	}
	
	public static int dpToPx(int dp)
	{
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
	
	public static String GET(String url){
	    InputStream inputStream = null;
	    String result = "";
	    try {

	        HttpClient httpclient = new DefaultHttpClient();

	        HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

	        inputStream = httpResponse.getEntity().getContent();

	        if(inputStream != null)
	            result = convertInputStreamToString(inputStream);
	        else
	            result = "Did not work!";

	    } catch (Exception e) {
	        Log.d("InputStream", e.getLocalizedMessage());
	    }

	    String ujres = result.replace("<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">", " ");
	    result = ujres.replace("</string>", " ");
	    return result;
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
	    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
	    String line = "";
	    String result = "";
	    while((line = bufferedReader.readLine()) != null)
	        result += line;

	    inputStream.close();
	    return result;

	}

	public boolean isConnected(){
	    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
	        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	        if (networkInfo != null && networkInfo.isConnected())
	            return true;
	        else
	            return false;  
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
	    	@Override
	        protected String doInBackground(String... urls) {

	        	return GET(urls[0]);
	        }

	        @Override
	        protected void onPostExecute(String result) {
	            Toast.makeText(getBaseContext(), "Cikkek lekérdezve!", Toast.LENGTH_LONG).show();
	            jsonresp = result;
	            
	            //TextView asd = (TextView) findViewById(R.id.asd);
	            //asd.setText(jsonresp);
	            
	            //List<Cikk> LekertCikk = new ArrayList<Cikk>();
	            LekertCikk = parseCikkJSON();
	            Cikkek(LekertCikk);
	            //asd.setText(LekertCikk.get(0).Author);
	        }
	    }
	    
	public List<Cikk> parseCikkJSON() {

	    	List<Cikk> eredm = new ArrayList<Cikk>();
	    	
	    	try {
				JSONArray jsonarr = new JSONArray(jsonresp);
				for (int i = 0; i < jsonarr.length(); i++) {
	              JSONObject c = jsonarr.getJSONObject(i);
	               
	              int id = c.getInt("ArticleId");
	              String title = c.getString("Title");
	              String szerzo = c.getString("Author");
	              String pdf = c.getString("PdfUri");
	              int ujsag = c.getInt("NewspaperId");
	              
	              eredm.add(new Cikk(id, title, szerzo, pdf, ujsag));
				}
			} catch (JSONException e){
				e.printStackTrace();
			}
	    	return eredm;
		}
	    
	private class DownloadTask extends AsyncTask<String, Integer, String> {
	    	
	    
	    	@Override
	        protected String doInBackground(String... fUrl) {
	    		
	    		fileName = pdfFajlnev(fUrl[0]);
	    		
	    		return PDFletolt(fUrl[0], fileName);
	    	}
	    	
	    	@Override
	        protected void onPostExecute(String result) {
	    		super.onPostExecute(result);
	    		
	    		final String result2 = result;
	    		runOnUiThread(new Runnable() {
	    		     @Override
	    		     public void run() {
	    		int torolid;
	    		for (Cikk c : LekertCikk){
	    			if (pdfFajlnev(c.PDFUri) == result2){
	    				torolid = 1000 + c.ID;
	    				ImageView torol = (ImageView) findViewById(torolid);
	    	    		torol.setVisibility(View.VISIBLE);
	    	    		Toast.makeText(getApplicationContext(), c.PDFUri + result2 + Integer.toString(c.ID), Toast.LENGTH_LONG).show();
	    			}
	    		}
	    		     }
	    		});
	    		
	    		ProgressBar tolt = (ProgressBar) findViewById(R.id.Tolt);
	    		
	    		if (result!=null){
	    		if (tolt.getVisibility() == View.VISIBLE){
	    		tolt.setVisibility(View.INVISIBLE);}
	    		
	    		PDFmegny(result);
	    		}
	    	}
	    	
	    	@Override
	        protected void onProgressUpdate(Integer... progress) {
	            super.onProgressUpdate(progress);

//	            ProgressDialog mProgressDialog;
//	            mProgressDialog = new ProgressDialog(CikkActivity.this);
//	            mProgressDialog.setMessage("A message");
//	            mProgressDialog.setIndeterminate(true);
//	            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//	            mProgressDialog.setCancelable(true);
//	            mProgressDialog.setIndeterminate(false);
//	            mProgressDialog.setMax(100);
//	            mProgressDialog.setProgress(progress[0]);
	            
	            ProgressBar tolt = (ProgressBar) findViewById(R.id.Tolt);
	            
	            if (tolt.getVisibility() == View.INVISIBLE){
	            tolt.setVisibility(View.VISIBLE);}
	        }
	    	
	    }
	    
	public void PDFmegny(String filename){
		
		final String result2 = filename;
		runOnUiThread(new Runnable() {
		     @Override
		     public void run() {
		int torolid;
		for (Cikk c : LekertCikk){
			if (pdfFajlnev(c.PDFUri) == result2){
				torolid = 1000 + c.ID;
				ImageView torol = (ImageView) findViewById(torolid);
	    		torol.setVisibility(View.VISIBLE);
	    		Toast.makeText(getApplicationContext(), c.PDFUri + result2 + Integer.toString(c.ID), Toast.LENGTH_LONG).show();
			}
		}
		     }
		});
		
		
		
	    	
	    	String PATH = Environment.getExternalStorageDirectory() + "/ÚjEgyenlítõ/" + filename;
            //File pdfFile = new File( PATH + filename); 
	    	File pdfFile = new File( PATH ); 
            if(pdfFile.exists())
            {
//                Uri path = Uri.fromFile(pdfFile); 
//                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
//                pdfIntent.setDataAndType(path, "application/pdf");
//                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	
            	
         				Intent pdfIntent = new Intent(CikkActivity.this, PdfViewer.class);
         			    pdfIntent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME, Environment.getExternalStorageDirectory() + "/ÚjEgyenlítõ/kiszelly1.pdf");
         			    //startActivity(pdfIntent);

                try
                {
                    startActivity(pdfIntent);
                }
                catch(ActivityNotFoundException e)
                {
                    Toast.makeText(CikkActivity.this, "Hiba", Toast.LENGTH_LONG).show(); 
                }
            }
            else {
            	Toast.makeText(CikkActivity.this, "File not found" + filename, Toast.LENGTH_LONG).show();
            }
	    }
	    
	public String PDFletolt(String PdfUrl, String fileName){
		
	    	try{
	    		URL url = new URL(PdfUrl);
	            HttpURLConnection c = (HttpURLConnection) url.openConnection();
	            c.setRequestMethod("GET");
	            c.setDoOutput(true);
	            c.connect();

	            String PATH = Environment.getExternalStorageDirectory() + "/ÚjEgyenlítõ/";
	            Log.d("Abhan", "PATH: " + PATH);
	            File file = new File(PATH);
	            if(!file.exists()) {
	               file.mkdirs();
	            }
	            File outputFile = new File(file, fileName);
	            FileOutputStream fos = new FileOutputStream(outputFile);
	            InputStream is = c.getInputStream();
	            byte[] buffer = new byte[1024];
	            int len1 = 0;
	            while ((len1 = is.read(buffer)) != -1) {
	                   fos.write(buffer, 0, len1);
	            }
	            fos.flush();
	            fos.close();
	            is.close();
	            
	        } catch (IOException e) {
	               Log.e("Abhan", "Error: " + e);
	               return null;
	           }
	           Log.i("Abhan", "Check Your File.");
	           return fileName;
	    }
	
		public String pdfFajlnev(String kapott){
			
			String[] tomb = kapott.split("/");
    		String fileName = tomb[tomb.length-1];
    		return fileName;
		}
		
		public void fajlTorles(String uri){
		
			File file = new File(uri);
			boolean deleted = file.delete();
		}
	    
}
