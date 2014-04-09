package rssfeed;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import menu.main;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.rssreader.ListActivity;
import com.example.rssreader.R;

public class RssItemDisplayer extends Activity implements OnTouchListener
{	
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.current_news);
		
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
		titleText.setTextSize(ListActivity.title_font);

		TextView descriptionText = new TextView(this); 
		descriptionText.setTextSize(ListActivity.news_font);
		
		TextView url_linkText = new TextView(this); 
		url_linkText.setTextSize(ListActivity.news_font);
			
						
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
	    	Intent intent = new Intent(this, AndroidRSSReader.class);
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
	    	Intent intent = new Intent(this, AndroidRSSReader.class);
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
	
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Операции для выбранного пункта меню
	    switch (item.getItemId()) 
		{
	    case R.id.action_settings:
	    	Intent intent_settings = new Intent(this, main.class);
			startActivity(intent_settings);
	        return true;
	    case R.id.action_about:
	    	// подключаем наш кастомный диалог лайаут
	    	LayoutInflater li = LayoutInflater.from(this);
	    	View promptsView = li.inflate(R.layout.about, null);
	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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
}