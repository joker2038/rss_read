package rssfeed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import menu.main;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.rssreader.ListActivity;
import com.example.rssreader.R;

import database.DatabaseContract.NamesFeed;
import database.DatabaseOpenHelper;
import database.feed.ManControllerFeed;


public class AndroidRSSReader extends Activity 
{
	final Context context = this;
	public static RssItem selectedRssItem = null;
	String urlLink = "";
	String id = "";	
	String name = "";
	ListView rssListViewUnread = null;
	ListView rssListViewRead = null;
	ListView rssListViewFavorites = null;
	ArrayAdapter<RssItem> aa = null;
	String[][] data1;
	String[][] data2;
	String[][] data3;
	ArrayAdapter<String> adapter1 = null;
	ArrayAdapter<String> adapter2 = null;
	ArrayAdapter<String> adapter3 = null;
	MyTask mt;
	int index = 0;
	String temp = "";
	String Flag = "";
	String state = "";
		
	public static final int RssItemDialog = 1;	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
	    // инициализаци€
	    tabHost.setup();
	       
	    TabHost.TabSpec tabSpec;
	       
	    // создаем вкладку и указываем тег
	    tabSpec = tabHost.newTabSpec("tag1");
	    // название вкладки
	    tabSpec.setIndicator("unread");
	    // указываем id компонента из FrameLayout, он и станет содержимым
	    tabSpec.setContent(R.id.tab1);
	    // добавл€ем в корневой элемент
	    tabHost.addTab(tabSpec);
	       
	    tabSpec = tabHost.newTabSpec("tag2");
	    // название вкладки
	    tabSpec.setIndicator("read");
	    // указываем id компонента из FrameLayout, он и станет содержимым
	    tabSpec.setContent(R.id.tab2); 
	    // добавл€ем в корневой элемент
	    tabHost.addTab(tabSpec);
	    
	    tabSpec = tabHost.newTabSpec("tag3");
	    // название вкладки
	    tabSpec.setIndicator("" , getResources().getDrawable(R.drawable.favorites));
	    // указываем id компонента из FrameLayout, он и станет содержимым
	    tabSpec.setContent(R.id.tab3); 
	    // добавл€ем в корневой элемент
	    tabHost.addTab(tabSpec);
	    
	    // перва€ вкладка будет выбрана по умолчанию
        tabHost.setCurrentTabByTag("tag1");
           
		rssListViewUnread = (ListView) findViewById(R.id.rssListViewUnread);
		rssListViewRead = (ListView) findViewById(R.id.rssListViewRead);
		rssListViewFavorites = (ListView) findViewById(R.id.rssListViewFavorites);
		
		TextView label = (TextView) findViewById(R.id.label);
		label.setTextSize(ListActivity.title_font);
		
		id = getIntent().getStringExtra("id").toString();
		urlLink = getIntent().getStringExtra("urlLink").toString();
		name = getIntent().getStringExtra("name").toString();		
		
		try
		{
			Flag = getIntent().getStringExtra("Flag").toString();
		}
		catch (Exception e)
		{
			Flag = "";
		}

		
		label.setText(name);
		
		refressRssList();
		
		if(Flag.equals("next") || Flag.equals("last"))
		{
			index = getIntent().getIntExtra("index", 0);
			state = getIntent().getStringExtra("state").toString();
			open(index, state);
		}
				
		// here we specify what to execute when individual list items clicked
		rssListViewUnread.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> av, View view, int index, long arg3) 
			{
				open(index, "unread");
			}
		});		
		// here we specify what to execute when individual list items clicked
		rssListViewRead.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> av, View view, int index, long arg3) 
			{
				open(index, "read");
			}
		});			
		
		rssListViewFavorites.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> av, View view, int index, long arg3) 
			{
				open(index, "favorites");
			}
		});	
		
		rssListViewFavorites.setOnItemLongClickListener(new OnItemLongClickListener() 
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) 
			{
				final CharSequence[] items = { "”далить из избранного" };
				AlertDialog.Builder builder3 = new AlertDialog.Builder(AndroidRSSReader.this);
				builder3.setTitle("¬ыберите действие").setItems(items, new DialogInterface.OnClickListener() 
				{
							@Override
							public void onClick(DialogInterface dialog, int item) 
							{
								switch (item) 
								{
									case 0: 
									{										
										DatabaseOpenHelper dbhelper = new DatabaseOpenHelper(getBaseContext());
										SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
										Cursor cursor1 = sqliteDB.query(NamesFeed.TABLE_NAME, null, "title=?", new String[] { "" + data3[0][pos] }, null, null, null);
										String str0 = null;
										if (cursor1 != null)
										{
											if (cursor1.moveToFirst())
											{
												str0 = cursor1.getString(cursor1.getColumnIndex("_id"));
											}
											cursor1.close();
										}
										ManControllerFeed.update(getBaseContext(), "no_favorites", Long.parseLong(str0));												
										dbhelper.close();
										sqliteDB.close();
										Intent intent = getIntent();								        
								        startActivity(intent);
								        finish();
									}
										break;
								}
							}
						});
				builder3.show();
				return true;
			}
		});
		
		rssListViewUnread.setOnItemLongClickListener(new OnItemLongClickListener() 
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) 
			{
				final CharSequence[] items = { "ƒобавить в избранное" };
				AlertDialog.Builder builder3 = new AlertDialog.Builder(AndroidRSSReader.this);
				builder3.setTitle("¬ыберите действие").setItems(items, new DialogInterface.OnClickListener() 
				{
							@Override
							public void onClick(DialogInterface dialog, int item) 
							{
								switch (item) 
								{
									case 0: 
									{										
										addToFavorites(data1[0][pos], "favorites");
									}
										break;
								}
							}
						});
				builder3.show();
				return true;
			}
		});
		
		rssListViewRead.setOnItemLongClickListener(new OnItemLongClickListener() 
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) 
			{
				final CharSequence[] items = { "ƒобавить в избранное" };
				AlertDialog.Builder builder3 = new AlertDialog.Builder(AndroidRSSReader.this);
				builder3.setTitle("¬ыберите действие").setItems(items, new DialogInterface.OnClickListener() 
				{
							@Override
							public void onClick(DialogInterface dialog, int item) 
							{
								switch (item) 
								{
									case 0: 
									{										
										addToFavorites(data2[0][pos], "favorites");
									}
										break;
								}
							}
						});
				builder3.show();
				return true;
			}
		});
		
		ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);

		imageButton1.setOnClickListener(new View.OnClickListener() 
		{
		    public void onClick(View v) 
		    {	  
		    	mt = new MyTask();
		        mt.execute();
		    }
		});
	}

	class MyTask extends AsyncTask<Void, Void, Void> 
	{
		@Override
	    protected void onPreExecute() 
	    {
	      super.onPreExecute();
	    }

	    @Override
	    protected Void doInBackground(Void... params) 
	    {
	    	ArrayList<RssItem> newItems = new ArrayList<RssItem>();
	    	newItems = RssItem.getRssItems(urlLink);
	    	
	    	DatabaseOpenHelper dbhelper = new DatabaseOpenHelper(getBaseContext());
	    	SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();	
	    	Cursor cursor1 = null;
	    	cursor1 = sqliteDB.query(NamesFeed.TABLE_NAME, new String[] {"max(" + NamesFeed.NamesColumns.PUPDATE + ")"}, NamesFeed.NamesColumns.NAMBER + " = ?",  new String[] { id }, null, null, null);
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
	    	
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
	    		    		for (int i = 0; i < newItems.size(); i++)
	    	{
	    			ManControllerFeed.write(getBaseContext(), id ,'"' + newItems.get(i).getTitle().toString() + '"', '"' + newItems.get(i).getDescription().toString() + '"', sdf.format(newItems.get(i).getPubDate()), '"' + newItems.get(i).getLink().toString() + '"', '"' +"unread" + '"', str, "no_favorites");
	    	}
	    	return null;
	    }

	    @Override
	    protected void onPostExecute(Void result) 
	    {
	    	super.onPostExecute(result);
	    	refressRssList();
	    }
	  }
	
	public void refressRssList() 
	{			
		DatabaseOpenHelper dbhelper = new DatabaseOpenHelper(getBaseContext());
		SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();	
		
		String unread = "unread";
		String read = "read";
		String favorites = "favorites";
		String noFavorites = "no_favorites";

		final Cursor CursorUnread = sqliteDB.query(NamesFeed.TABLE_NAME, null, NamesFeed.NamesColumns.READ + " = ? and " + NamesFeed.NamesColumns.NAMBER + " = ? and " + NamesFeed.NamesColumns.FAVORITES + " = ?", new String[] { unread, id, noFavorites }, null, null, NamesFeed.DEFAULT_SORT);
		final Cursor CursorRead = sqliteDB.query(NamesFeed.TABLE_NAME, null, NamesFeed.NamesColumns.READ + " = ? and " + NamesFeed.NamesColumns.NAMBER + " = ? and " + NamesFeed.NamesColumns.FAVORITES + " = ?", new String[] { read, id, noFavorites }, null, null, NamesFeed.DEFAULT_SORT);
		final Cursor CursorFavorites = sqliteDB.query(NamesFeed.TABLE_NAME, null, NamesFeed.NamesColumns.FAVORITES + " = ? and " + NamesFeed.NamesColumns.NAMBER + " = ?", new String[] { favorites, id }, null, null, NamesFeed.DEFAULT_SORT);
		
		if (CursorUnread != null)
		{
			int iNew1 = -1;
			data1 = new String[1][CursorUnread.getCount()];
		    while(CursorUnread.moveToNext()) 
		    {
		    	iNew1++;
		    	data1[0][iNew1] = CursorUnread.getString(1);	    
		    }
		}		

		CursorUnread.close();
		
		if (CursorRead != null)
		{
			int iNew2 = -1;
			data2 = new String[1][CursorRead.getCount()];
		    while(CursorRead.moveToNext()) 
		    {
		    	iNew2++;
		    	data2[0][iNew2] = CursorRead.getString(1);	    
		    }
		}
		
		CursorRead.close();
		
		if (CursorFavorites != null)
		{
			int iNew3 = -1;
			data3 = new String[1][CursorFavorites.getCount()];
		    while(CursorFavorites.moveToNext()) 
		    {
		    	iNew3++;
		    	data3[0][iNew3] = CursorFavorites.getString(1);	    
		    }
		}	
		
		CursorFavorites.close();
		
		if (ListActivity.channel_list_font == 10)
        {
			adapter1 = new ArrayAdapter<String>(this, R.layout.my_list_feed_small_size, data1[0]);
			adapter2 = new ArrayAdapter<String>(this, R.layout.my_list_feed_small_size, data2[0]);
			adapter3 = new ArrayAdapter<String>(this, R.layout.my_list_feed_small_size, data3[0]);
        }
        else if (ListActivity.channel_list_font == 20)
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
						
		rssListViewUnread.setAdapter(adapter1);			
		rssListViewRead.setAdapter(adapter2);		
		rssListViewFavorites.setAdapter(adapter3);
	}

	public void open(int index, String temp)
	{
		try
		{
			//обновление прочитого
			DatabaseOpenHelper dbhelper = new DatabaseOpenHelper(getBaseContext());
			SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();	
			Cursor cursor1 = null;
			if(temp.equals("read"))
			{
				cursor1 = sqliteDB.query(NamesFeed.TABLE_NAME, null, "title=?", new String[] { "" + data2[0][index] }, null, null, NamesFeed.DEFAULT_SORT);				
			}
			else if (temp.equals("favorites"))
			{
				cursor1 = sqliteDB.query(NamesFeed.TABLE_NAME, null, "title=?", new String[] { "" + data3[0][index] }, null, null, NamesFeed.DEFAULT_SORT);				
			}
			else if (temp.equals("unread"))
			{
				cursor1 = sqliteDB.query(NamesFeed.TABLE_NAME, null, "title=?", new String[] { "" + data1[0][index] }, null, null, NamesFeed.DEFAULT_SORT);				
			}
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
			if (temp.equals("unread"))
			{
				ManControllerFeed.update(getBaseContext(), "read", Long.parseLong(str0));
			}
			dbhelper.close();
			sqliteDB.close();
			//refressRssList();
						
			Intent intent = new Intent(this, RssItemDisplayer.class);	
			intent.putExtra("title", str1);
			intent.putExtra("description", str2);
			intent.putExtra("link", str3);
			//
			intent.putExtra("state", temp);
			intent.putExtra("index", index);
			//
			intent.putExtra("name", name);	
			intent.putExtra("id", id);
			intent.putExtra("urlLink", urlLink);
			
			startActivity(intent);
			finish();
		}
		catch (Exception e)
		{
		}
		
	}
	
	private void addToFavorites(String pos,String temp)
	{
		DatabaseOpenHelper dbhelper = new DatabaseOpenHelper(getBaseContext());
		SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
		Cursor cursor1 = null;
		
		cursor1 = sqliteDB.query(NamesFeed.TABLE_NAME, null, "title=?", new String[] { pos }, null, null, null);
		
		String str0 = null;
		if (cursor1 != null)
		{
			if (cursor1.moveToFirst())
			{
				str0 = cursor1.getString(cursor1.getColumnIndex("_id"));
			}
			cursor1.close();
		}
		ManControllerFeed.update(getBaseContext(), "favorites", Long.parseLong(str0));												
		dbhelper.close();
		sqliteDB.close();
		Intent intent = getIntent();
        finish();
        startActivity(intent);
		
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
		switch (item.getItemId()) 
		{
	    case R.id.action_settings:
	    	Intent intent_settings = new Intent(this, main.class);
			startActivity(intent_settings);
	        return true;
	    case R.id.action_about:
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
}