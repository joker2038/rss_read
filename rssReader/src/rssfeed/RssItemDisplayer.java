package rssfeed;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import menu.PrefActivity;
import ru.joker2038.rssreader.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RssItemDisplayer extends Activity implements OnTouchListener
{	
	final Context context = this;
	ImageView image;
	float xNew;
	float xOld;
	float x;
	String title = "";
	String description = "";
	String link = "";
	String name = "";
	String id = "";
	String urlLink = "";
	String Flag = "";
	String state = "";
	int index;
	Intent intent;
	
	public static final int IDM_PREF = 101;
	public static final int IDM_PR = 102;
	public static final String APP_PREFERENCES = "ru.joker2038.rssreader_preferences"; 	
	SharedPreferences mSettings;
	String headline_font_size;
	String font_size_News;
	String headline_color_size;
	String color_size_News;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.current_news);
		
		mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		headline_font_size = mSettings.getString("font_size_of_the_channel_list", "20");
		font_size_News = mSettings.getString("font_size_News", "20");
		//
		headline_color_size = mSettings.getString("headline_color_size", "#000000");
		color_size_News = mSettings.getString("color_size_News", "#000000");
		
		
		title = getIntent().getStringExtra("title").toString();
		description = getIntent().getStringExtra("description").toString();
		link = getIntent().getStringExtra("link").toString();
		name = getIntent().getStringExtra("name").toString();
		id = getIntent().getStringExtra("id").toString();
		urlLink = getIntent().getStringExtra("urlLink").toString();
		index = getIntent().getIntExtra("index", 0);
		state = getIntent().getStringExtra("state").toString();
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout); 
		
		TextView titleText = new TextView(this); 
		titleText.setTextSize(Integer.parseInt(headline_font_size));
		titleText.setTextColor(Color.parseColor(headline_color_size));
		
		TextView descriptionText = new TextView(this); 
		descriptionText.setTextSize(Integer.parseInt(font_size_News));
		descriptionText.setTextColor(Color.parseColor(color_size_News));
		
		TextView url_linkText = new TextView(this); 
		url_linkText.setTextSize(Integer.parseInt(font_size_News));
		url_linkText.setTextColor(Color.parseColor(color_size_News));	
						
		Pattern pattern = Pattern.compile("http\\S*");
		Matcher matcher = pattern.matcher(description);		

		description = description.replaceAll("http\\S*", "");
		
		titleText.setText(title + "\n");
		titleText.setOnTouchListener(this);
		layout.addView(titleText);
		descriptionText.setText(description + "\n");	
		descriptionText.setOnTouchListener(this);
		layout.addView(descriptionText);

		while (matcher.find())
		{
			image = new ImageView(this);      
	        String text = matcher.group();
	        //image.setImageURI(Uri.parse(text));
	        
	        // Create an object for subclass of AsyncTask
	        GetXMLTask task = new GetXMLTask();
	        // Execute the task
	        task.execute(new String[] { text });
	        image.setOnTouchListener(this);
	        layout.addView(image);
		}	
		
		url_linkText.setText("\n" + link);		
		Linkify.addLinks(url_linkText, Linkify.ALL);
		layout.setOnTouchListener(this);
		layout.addView(url_linkText);	
	}
	
	public boolean onTouch(View v, MotionEvent event) 
	{		
	    x = event.getX();
	    
	    switch (event.getAction()) 
	    {
	    case MotionEvent.ACTION_DOWN: // нажатие
	      xOld = x;
	      //break;
	    case MotionEvent.ACTION_MOVE: // движение
	      //break;
	    case MotionEvent.ACTION_UP: // отпускание
	    	
	    case MotionEvent.ACTION_CANCEL:  
	      xNew = x;
	      break;
	    }
	    if (xNew > xOld && xNew - xOld >= 100 && Flag.equals(""))
	    {	
	    	Flag = "next";
	    	if (state.equals("read") || state.equals("favorites"))
	    	{
	    		index++;		
	    	}
	    	intent = new Intent(this, AndroidRSSReader.class);
	    	intent.putExtra("name", name);	
			intent.putExtra("id", id);
			intent.putExtra("urlLink", urlLink);
			//
			intent.putExtra("index", index);
			intent.putExtra("Flag", Flag);
			intent.putExtra("state", state);
			startActivity(intent);
			finish();
	    }
	    else if (xNew < xOld && xOld - xNew >= 100 && Flag.equals(""))
	    {	
	    	Flag = "last";
	    	index--;		
	    	intent = new Intent(this, AndroidRSSReader.class);
	    	intent.putExtra("name", name);	
			intent.putExtra("id", id);
			intent.putExtra("urlLink", urlLink);
			//
			intent.putExtra("index", index);
			intent.putExtra("Flag", Flag);
			intent.putExtra("state", state);
			startActivity(intent);
			finish();
	    }
	    return true;
	  }
	
	private class GetXMLTask extends AsyncTask<String, Void, Bitmap> 
	{
        @Override
        protected Bitmap doInBackground(String... urls) 
        {
            Bitmap map = null;
            for (String url : urls) 
            {
                map = downloadImage(url);
            }
            return map;
        }
 
        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) 
        {
            image.setImageBitmap(result);
        }
 
        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) 
        {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;
 
            try 
            {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } 
            catch (IOException e1) 
            {
                e1.printStackTrace();
            }
            return bitmap;
        }
 
        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString) throws IOException 
        {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection(); 
            try 
            {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();
 
                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) 
                {
                    stream = httpConnection.getInputStream();
                }
            } 
            catch (Exception ex) 
            {
                ex.printStackTrace();
            }
            return stream;
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0,  IDM_PREF, 0, "Настройки");
		menu.add(0,  IDM_PR, 0, "О программе");
		return super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		/*getMenuInflater().inflate(R.menu.main, menu);
		return true;*/
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent = new Intent();
		switch (item.getItemId()) 
		{
	    case IDM_PREF:
	    	intent.setClass(this, PrefActivity.class); 
	    	startActivity(intent);
	        return true;
	    case IDM_PR:
	    	// подключаем наш кастомный диалог лайаут
	    	LayoutInflater li = LayoutInflater.from(context);
	    	View promptsView = li.inflate(R.layout.about, null);
	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
	    	// делаем его диалогом
	    	alertDialogBuilder.setView(promptsView);
	    	// создаем диалог
	    	AlertDialog alertDialog = alertDialogBuilder.create();
	    	// показываем его
	    	alertDialog.show();
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	public void onBackPressed()
    {     	
		intent = new Intent(RssItemDisplayer.this, AndroidRSSReader.class);
		intent.putExtra("name", name);	
		intent.putExtra("id", id);
		intent.putExtra("urlLink", urlLink);
		//
		intent.putExtra("index", index);
		intent.putExtra("Flag", Flag);
		intent.putExtra("state", state);
		startActivity(intent);
		finish();
    }  
}