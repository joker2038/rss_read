package rssfeed;

import menu.main;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.rssreader.MainActivity;
import com.example.rssreader.R;

public class RssItemDisplayer extends Activity
{
	String title = "";
	String description = "";
	String link = "";
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.current_news);
		
		title = getIntent().getStringExtra("title").toString();
		description = getIntent().getStringExtra("description").toString();
		link = getIntent().getStringExtra("link").toString();
		
		TextView titleText = (TextView) findViewById(R.id.title);
		titleText.setTextSize(MainActivity.title_font);

		TextView descriptionText = (TextView) findViewById(R.id.description);
		descriptionText.setTextSize(MainActivity.news_font);
		
		TextView url_linkText = (TextView) findViewById(R.id.url_link);
		url_linkText.setTextSize(MainActivity.news_font);
						
		titleText.setText(title + "\n");
		descriptionText.setText(description + "\n");
		url_linkText.setText(link);
		
		//RssItem selectedRssItem = AndroidRSSReader.selectedRssItem;
		//Bundle extras = getIntent().getExtras();
		//TextView titleTv = (TextView)findViewById(R.id.titleTextView);	
		//TextView contentTv = (TextView)findViewById(R.id.contentTextView);	
				
		//String title = "";
		//SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy/ hh:mm:ss");
		//title = "\n" + selectedRssItem.getTitle() + "   ( "	+ sdf.format(selectedRssItem.getPubDate()) + " )\n\n";
		
		//String content = "";
		//content += selectedRssItem.getDescription() + "\n" + selectedRssItem.getLink();
		
		//titleTv.setText(title);
		//contentTv.setText(content);
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
		Intent intent = new Intent(this, main.class);
		startActivity(intent);
		return true;
	}
}