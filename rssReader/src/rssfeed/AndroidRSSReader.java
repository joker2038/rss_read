package rssfeed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import menu.main;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.rssreader.MainActivity;
import com.example.rssreader.R;

import database.feed.DatabaseContractFeed.Names;
import database.feed.DatabaseOpenHelperFeed;
import database.feed.ManControllerFeed;

public class AndroidRSSReader extends Activity 
{
	public static RssItem selectedRssItem = null;
	String feedUrl = "";
	ListView rssListViewUnread = null;
	ListView rssListViewRead = null;
	ArrayList<RssItem> rssItems = new ArrayList<RssItem>();
	ArrayAdapter<RssItem> aa = null;
	String[][] data1;
	String[][] data2;
	String[][] data3;
	ArrayAdapter<String> adapter1 = null;
	ArrayAdapter<String> adapter2 = null;
	ArrayAdapter<String> adapter3 = null;
	
	public static final int RssItemDialog = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
	    // инициализация
	    tabHost.setup();
	       
	    TabHost.TabSpec tabSpec;
	       
	    // создаем вкладку и указываем тег
	    tabSpec = tabHost.newTabSpec("tag1");
	    // название вкладки
	    tabSpec.setIndicator("unread");
	    // указываем id компонента из FrameLayout, он и станет содержимым
	    tabSpec.setContent(R.id.tab1);
	    // добавляем в корневой элемент
	    tabHost.addTab(tabSpec);
	       
	    tabSpec = tabHost.newTabSpec("tag2");
	    // название вкладки
	    tabSpec.setIndicator("read");
	    // указываем id компонента из FrameLayout, он и станет содержимым
	    tabSpec.setContent(R.id.tab2); 
	    // добавляем в корневой элемент
	    tabHost.addTab(tabSpec);
	    
	    tabSpec = tabHost.newTabSpec("tag3");
	    // название вкладки
	    tabSpec.setIndicator("" , getResources().getDrawable(R.drawable.favorites));
	    // указываем id компонента из FrameLayout, он и станет содержимым
	    tabSpec.setContent(R.id.tab3); 
	    // добавляем в корневой элемент
	    tabHost.addTab(tabSpec);
	    
	    // первая вкладка будет выбрана по умолчанию
        tabHost.setCurrentTabByTag("tag1");
        
        // обработчик переключения вкладок
        //tabHost.setOnTabChangedListener(new OnTabChangeListener() 
        //{
        //	public void onTabChanged(String tabId) 
        //	{
        //		Toast.makeText(getBaseContext(), "tabId = " + tabId, Toast.LENGTH_SHORT).show();
        //	}
        //});

		rssListViewUnread = (ListView) findViewById(R.id.rssListViewUnread);
		rssListViewRead = (ListView) findViewById(R.id.rssListViewRead);
		
		TextView label = (TextView) findViewById(R.id.label);
		label.setTextSize(MainActivity.title_font);
		
		feedUrl = getIntent().getStringExtra("urlLink").toString();
		label.setText(getIntent().getStringExtra("name").toString());
		
		refressRssList();
		
		// here we specify what to execute when individual list items clicked
		rssListViewUnread.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> av, View view, int index, long arg3) 
			{
				//обновление прочитого
				DatabaseOpenHelperFeed dbhelper = new DatabaseOpenHelperFeed(getBaseContext());
				SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
				//final Cursor c = sqliteDB.query(Names.TABLE_NAME, null, null, null, null, null, Names.DEFAULT_SORT);				
				final Cursor cursor1 = sqliteDB.query(Names.TABLE_NAME, null, "title=?", new String[] { "" + data1[0][index] }, null, null, null);
				String str0 = null;
				String str1 = null;
		        String str2 = null;
		        String str3 = null;
				if (cursor1 != null)
				{
				      if (cursor1.moveToFirst())
				      {
				    	  str0 = cursor1.getString(cursor1.getColumnIndex("_id"));
				    	  str1 = cursor1.getString(cursor1.getColumnIndex("title"));
				    	  str2 = cursor1.getString(cursor1.getColumnIndex("description"));
				    	  str3 = cursor1.getString(cursor1.getColumnIndex("link"));
				      }
				      cursor1.close();
				}
				ManControllerFeed.update(getBaseContext(), "read", Long.parseLong(str0));	
				dbhelper.close();
				sqliteDB.close();
				refressRssList();
				
				Intent intent = new Intent(AndroidRSSReader.this, RssItemDisplayer.class);	
				intent.putExtra("title", str1);
				intent.putExtra("description", str2);
				intent.putExtra("link", str3);
				startActivity(intent);
			}
		});		
		// here we specify what to execute when individual list items clicked
		rssListViewRead.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> av, View view, int index, long arg3) 
			{
				//обновление прочитого
				DatabaseOpenHelperFeed dbhelper = new DatabaseOpenHelperFeed(getBaseContext());
				SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();	
				final Cursor cursor1 = sqliteDB.query(Names.TABLE_NAME, null, "title=?", new String[] { "" + data2[0][index] }, null, null, null);
				String str1 = null;
		        String str2 = null;
		        String str3 = null;
				if (cursor1 != null)
				{
					if (cursor1.moveToFirst())
					{
						str1 = cursor1.getString(cursor1.getColumnIndex("title"));
						str2 = cursor1.getString(cursor1.getColumnIndex("description"));
						str3 = cursor1.getString(cursor1.getColumnIndex("link"));
					}
					cursor1.close();
				}
				dbhelper.close();
				sqliteDB.close();
				refressRssList();
							
				Intent intent = new Intent(AndroidRSSReader.this, RssItemDisplayer.class);	
				intent.putExtra("title", str1);
				intent.putExtra("description", str2);
				intent.putExtra("link", str3);
				startActivity(intent);
			}
		});				
	}

	private void refressRssList() 
	{		
		ArrayList<RssItem> newItems = RssItem.getRssItems(feedUrl);
		
		DatabaseOpenHelperFeed dbhelper = new DatabaseOpenHelperFeed(getBaseContext());
		SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();	
		Cursor cursor1 = null;
		cursor1 = sqliteDB.query(Names.TABLE_NAME, new String[] {"max(" + Names.NamesColumns.PUPDATE + ")"}, null, null, null, null, null);
        String str = "0";
		if (cursor1 != null)
		{
		      if (cursor1.moveToFirst())
		      {
		        for (String cn : cursor1.getColumnNames())
		        {
		        	if (cursor1.getString(cursor1.getColumnIndex(cn)) !=null )
				      {
				    	  str = cursor1.getString(cursor1.getColumnIndex(cn));
				      }
		        }
		      }
		      cursor1.close();
		}

		//rssItems.clear();
		//rssItems.addAll(newItems);
		//SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		
		for (int i = 0; i < newItems.size(); i++)
		{
			ManControllerFeed.write(getBaseContext(), '"' + newItems.get(i).getTitle().toString() + '"', '"' + newItems.get(i).getDescription().toString() + '"', sdf.format(newItems.get(i).getPubDate()), '"' + newItems.get(i).getLink().toString() + '"', '"' +"unread" + '"', str);
		}

		final Cursor c1 = sqliteDB.query(Names.TABLE_NAME, null, Names.NamesColumns.READ + " = ?", new String[] { "unread" }, null, null, Names.DEFAULT_SORT);
		
		if (c1 != null)
		{
			int iNew = -1;
			data1 = new String[1][c1.getCount()];
		    while(c1.moveToNext()) 
		    {		  
		    	iNew++;
		        data1[0][iNew] = c1.getString(1);	        
		    }
		    c1.close();
		}
		
		final Cursor c2 = sqliteDB.query(Names.TABLE_NAME, null, Names.NamesColumns.READ + " = ?", new String[] { "read" }, null, null, Names.DEFAULT_SORT);
		
		if (c2 != null)
		{
			int iNew = -1;
			data2 = new String[1][c2.getCount()];
		    while(c2.moveToNext()) 
		    {		  
		    	iNew++;
		        data2[0][iNew] = c2.getString(1);		        
		    }
		    c2.close();
		}
		
		final Cursor c3 = sqliteDB.query(Names.TABLE_NAME, null, Names.NamesColumns.READ + " = ?", new String[] { "favorites" }, null, null, Names.DEFAULT_SORT);
		
		if (c3 != null)
		{
			int iNew = -1;
			data3 = new String[1][c3.getCount()];
		    while(c3.moveToNext()) 
		    {		  
		    	iNew++;
		        data3[0][iNew] = c3.getString(1);	        
		    }
		    c3.close();
		}
		
		if (MainActivity.channel_list_font == 10)
        {
			adapter1 = new ArrayAdapter<String>(this, R.layout.my_list_feed_small_size, data1[0]);
			adapter2 = new ArrayAdapter<String>(this, R.layout.my_list_feed_small_size, data2[0]);
			adapter3 = new ArrayAdapter<String>(this, R.layout.my_list_feed_small_size, data3[0]);
        }
        else if (MainActivity.channel_list_font == 20)
        {
        	adapter1 = new ArrayAdapter<String>(this, R.layout.my_list_feed_average_size, data1[0]);
        	adapter2 = new ArrayAdapter<String>(this, R.layout.my_list_feed_average_size, data2[0]);
        	adapter3 = new ArrayAdapter<String>(this, R.layout.my_list_feed_average_size, data3[0]);
        }
        else
        {
        	adapter1 = new ArrayAdapter<String>(this, R.layout.my_list_feed_large_size, data1[0]);
        	adapter2 = new ArrayAdapter<String>(this, R.layout.my_list_feed_large_size, data2[0]);
        	adapter3 = new ArrayAdapter<String>(this, R.layout.my_list_feed_large_size, data3[0]);
        }
				
		final ListView lv1 = (ListView) findViewById(R.id.rssListViewUnread);		
		lv1.setAdapter(adapter1);
		
		final ListView lv2 = (ListView) findViewById(R.id.rssListViewRead);		
		lv2.setAdapter(adapter2);
		
		final ListView lv3 = (ListView) findViewById(R.id.rssListViewFavorites);		
		lv3.setAdapter(adapter3);
		
		//c.close();	
		//cursor1.close();
		//sqliteDB.close();
		//dbhelper.close();
		
		//ListActivity.BAZA_NAME = "";
	}

	@Override
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