package rssfeed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import menu.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.rssreader.ListActivity;
import com.example.rssreader.MainActivity;
import com.example.rssreader.R;

import database.feed.DatabaseContractFeed.Names;
import database.feed.DatabaseContractFeed.Names.NamesColumns;
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
		
		aa = new ArrayAdapter<RssItem>(this, R.layout.list_item, rssItems);
		rssListViewUnread.setAdapter(aa);
		rssListViewRead.setAdapter(aa);

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
				ManControllerFeed.update(getBaseContext(), "read", arg3);	
				final Cursor cursor1 = sqliteDB.query(Names.TABLE_NAME, null, "_id=?", new String[] { "" + arg3 }, null, null, null);
		        String str1 = "";
		        String str2 = "";
		        String str3 = "";
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
		// here we specify what to execute when individual list items clicked
		rssListViewRead.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> av, View view, int index, long arg3) 
			{
				//обновление прочитого
				DatabaseOpenHelperFeed dbhelper = new DatabaseOpenHelperFeed(getBaseContext());
				SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();	
				final Cursor cursor1 = sqliteDB.query(Names.TABLE_NAME, null, "_id=?", new String[] { "" + arg3 }, null, null, null);
				String str1 = "";
				String str2 = "";
				String str3 = "";
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

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) 
	{
		switch (id) 
		{
			case RssItemDialog: 
			{
				LayoutInflater li = LayoutInflater.from(this);
				View rssDetails = li.inflate(R.layout.rss_details, null);
	
				AlertDialog.Builder rssDialog = new AlertDialog.Builder(this);
				rssDialog.setTitle("Rss Item");
				rssDialog.setView(rssDetails);
	
				return rssDialog.create();
			}
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) 
	{
		switch (id) 
		{
			case RssItemDialog: 
			{
				AlertDialog rssDialog = (AlertDialog) dialog;
	
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy/ hh:mm:ss");
				rssDialog.setTitle(selectedRssItem.getTitle() + " : " + sdf.format(selectedRssItem.getPubDate()));
	
				String text = selectedRssItem.getDescription() + " : " + selectedRssItem.getLink();
	
				TextView tv = (TextView) rssDialog.findViewById(R.id.rssDetailsTextView);
				tv.setText(text);	
			}
		}
	}

	private void refressRssList() 
	{		
		ArrayList<RssItem> newItems = RssItem.getRssItems(feedUrl);

		aa.notifyDataSetChanged();
		
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
				
		final String[] from = { NamesColumns.TITLE };
		final Cursor c1 = sqliteDB.query(Names.TABLE_NAME, null, Names.NamesColumns.READ + " = ?", new String[] { "unread" }, null, null, Names.DEFAULT_SORT);
		final Cursor c2 = sqliteDB.query(Names.TABLE_NAME, null, Names.NamesColumns.READ + " = ?", new String[] { "read" }, null, null, Names.DEFAULT_SORT);
		
		final int[] to = new int[] { R.id.LinkNew };
		
		final SimpleCursorAdapter adapter1 = new SimpleCursorAdapter(getApplicationContext(), R.layout.list_news, c1, from, to);
		final SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(getApplicationContext(), R.layout.list_news, c2, from, to);
		
		final ListView lv1 = (ListView) findViewById(R.id.rssListViewUnread);		
		lv1.setAdapter(adapter1);		
		final ListView lv2 = (ListView) findViewById(R.id.rssListViewRead);		
		lv2.setAdapter(adapter2);
		
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