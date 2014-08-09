package com.kovacsbk.ujegy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kovacsbk.ujegy.R;

public class MainActivity extends Activity {

	TextView jsonn;
	HttpClient client;
	final static String UjsagLink = "http://ujegyenlito.softit.hu/Egyenlito/WCF/DataProviderService.svc/GetNewspapers";
	String jsonresp;
	int utolso;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /* tesztlista
        List<Ujsag> MindenUjsag = new ArrayList<Ujsag>();
        MindenUjsag.add(new Ujsag(123, "1.szám", 54, "2013.május", "2014.január.3", "http://ujegyenlito.hu/wp-content/uploads/2013/10/elso-187x300.jpg"));
        MindenUjsag.add(new Ujsag(124, "2.szám", 54, "2013.június", "2014.január.5", "http://img4.wikia.nocookie.net/__cb20110804045428/uncyclopedia/images/8/81/You%27re_adopted.jpg"));
        Ujsagok(MindenUjsag);
        */
        
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        new HttpAsyncTask().execute(UjsagLink);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      case R.id.fb_share:
        Toast.makeText(this, "Action refresh selected", Toast.LENGTH_SHORT).show();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sharer/sharer.php?u=http%3A%2F%2Fujegyenlito.hu%2F"));
    	startActivity(browserIntent);
        break;
      case R.id.email:
    	Intent emailIntent = new Intent(MainActivity.this, EmailActivity.class);
    	startActivity(emailIntent);
        break;

      default:
        break;
      }

      return true;
    }
    
    public List<Ujsag> parseUjsagJSON() {
    	
    	List<Ujsag> eredm = new ArrayList<Ujsag>();
    	
    	try {
			JSONArray jsonarr = new JSONArray(jsonresp);
			for (int i = 0; i < jsonarr.length(); i++) {
              JSONObject c = jsonarr.getJSONObject(i);
               
              int id = c.getInt("NewspaperId");
              String title = c.getString("Title");
              int pages = c.getInt("Pages");
              String release = c.getString("ReleaseDate");
              String upload = c.getString("UploadDate");
              String uri = c.getString("CoverUri");
              
              eredm.add(new Ujsag(id, title, pages, release, upload, uri));
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
    	return eredm;
	}

	public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
     
    public void Ujsagok(List<Ujsag> lista){
    	
    	
    	for (Ujsag u : lista){
    		//utolso = u.ID;
    		LinearLayout szetvalaszto = new LinearLayout(this);
    		szetvalaszto.setId(u.ID);
    		szetvalaszto.setOnClickListener(new View.OnClickListener() {

    		    @Override
    		    public void onClick(View v) {
    		    	Intent intent = new Intent(MainActivity.this, CikkActivity.class);
    		        int uid = v.getId();
    		        //String value = Integer.toString(uid);
    		        intent.putExtra("ujsagid", uid);
    		        startActivity(intent);
    		    }
    		});
    		MarginLayoutParams param1 = new MarginLayoutParams(LayoutParams.MATCH_PARENT, dpToPx(120));
    		param1.setMargins(15,5,5,5);
    		szetvalaszto.setOrientation(LinearLayout.HORIZONTAL);
    		szetvalaszto.setLayoutParams(param1);
    		LinearLayout my_root = (LinearLayout) findViewById(R.id.lista);
            my_root.addView(szetvalaszto);
            
            View separator = new View(this);
            separator.setBackgroundResource(android.R.color.darker_gray);
            MarginLayoutParams param5 = new MarginLayoutParams(LayoutParams.MATCH_PARENT, dpToPx(1));
            //LayoutParams param5 = new LayoutParams(LayoutParams.MATCH_PARENT, dpTpPx(2));
            separator.setPadding(5,0,5,0);
            param5.setMargins(5,0,5,0);
            separator.setLayoutParams(param5);
            my_root.addView(separator);
            
            
            int loader = R.drawable.ujegy;
            ImageView image = new ImageView(this);
            android.widget.LinearLayout.LayoutParams param2 = new android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(110), 0.7f);
            param2.gravity = Gravity.CENTER_VERTICAL;
            image.setAdjustViewBounds(false);
            image.setLayoutParams(param2);
            ImageLoader imgLoader = new ImageLoader(getApplicationContext());
            imgLoader.DisplayImage(u.CoverUri, loader, image);
            //android:scaleType="fitCenter"
            LinearLayout my_root2 = (LinearLayout) findViewById(u.ID);
            my_root2.addView(image);
            
            
            
            LinearLayout adatok = new LinearLayout(this);
            adatok.setOrientation(LinearLayout.VERTICAL);
    		adatok.setId(u.ID+1000);
    		//MarginLayoutParams param3 = new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    		android.widget.LinearLayout.LayoutParams param3 = new android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0.3f);
    		param3.setMargins(10,15,5,5);
    		adatok.setLayoutParams(param3);
    		//android:scrollbars="vertical" >
            my_root2.addView(adatok);
            
            
            TextView cim = new TextView(this);
            MarginLayoutParams param4 = new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            param4.setMargins(5, 5, 0, 5);
            cim.setLayoutParams(param4);
            cim.setText(u.Title);
            cim.setTextSize(16);
            LinearLayout my_root3 = (LinearLayout) findViewById(u.ID+1000);
            my_root3.addView(cim);
            
            TextView kiad = new TextView(this);
            kiad.setLayoutParams(param4);
            kiad.setText(u.ReleaseDate);
            kiad.setTextSize(16);
            my_root3.addView(kiad);
            
            TextView oldal = new TextView(this);
            oldal.setLayoutParams(param4);
            oldal.setText(Integer.toString(u.Pages)+" oldal");
            oldal.setTextSize(16);
            my_root3.addView(oldal);
    	}
    	
    	
//    	LinearLayout soc = (LinearLayout) findViewById(R.id.social);
//    	
//    	RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//    	        ViewGroup.LayoutParams.WRAP_CONTENT);
//
//    	p.addRule(RelativeLayout.BELOW, utolso);
//
//    	soc.setLayoutParams(p);
    	
    	
    	LinearLayout soc = new LinearLayout(this);
    	
    	MarginLayoutParams param7 = new MarginLayoutParams(LayoutParams.MATCH_PARENT, dpToPx(30));
		param7.setMargins(5,5,5,5);
		soc.setOrientation(LinearLayout.HORIZONTAL);
		soc.setLayoutParams(param7);
		LinearLayout my_root = (LinearLayout) findViewById(R.id.lista);
        
        
        ImageView fb = new ImageView(this);
        LayoutParams param8 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        fb.setLayoutParams(param8);
        fb.setImageResource(R.drawable.facebook);
        
        fb.setOnClickListener(new View.OnClickListener() {

		    @Override
		    public void onClick(View v) {
		    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sharer/sharer.php?u=http%3A%2F%2Fujegyenlito.hu%2F"));
		    	startActivity(browserIntent);
		    }
		});
        soc.addView(fb);
        
        ImageView twit = new ImageView(this);
        LayoutParams param9 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        twit.setLayoutParams(param9);
        twit.setImageResource(R.drawable.twitter);
        soc.addView(twit);
    	
        my_root.addView(soc);
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
        Toast.makeText(getBaseContext(), "Újságok lekérdezve!", Toast.LENGTH_LONG).show();
        jsonresp = result;
        
        List<Ujsag> LekertUjsag = new ArrayList<Ujsag>();
        LekertUjsag = parseUjsagJSON();
        Ujsagok(LekertUjsag);
   }
}

    public void Klikk0(View view) 
    {
        Intent intent = new Intent(MainActivity.this, CikkActivity.class);
        int uid = view.getId();
        String value = Integer.toString(uid);
        intent.putExtra("ujsagid", uid);
        startActivity(intent);
    }
}
    

