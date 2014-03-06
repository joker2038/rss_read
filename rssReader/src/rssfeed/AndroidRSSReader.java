package rssfeed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import menu.main;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import com.example.rssreader.MainActivity;
import com.example.rssreader.R;

import database.feed.DatabaseContractFeed.Names;
import database.feed.DatabaseContractFeed;
import database.feed.DatabaseOpenHelperFeed;
import database.feed.ManControllerFeed;

public class AndroidRSSReader extends Activity 
{
	public static RssItem selectedRssItem = null;
	String feedUrl = "";
	ListView rssListViewUnread = null;
	ListView rssListViewRead = null;
	ListView rssListViewFavorites = null;	
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
	    // �������������
	    tabHost.setup();
	       
	    TabHost.TabSpec tabSpec;
	       
	    // ������� ������� � ��������� ���
	    tabSpec = tabHost.newTabSpec("tag1");
	    // �������� �������
	    tabSpec.setIndicator("unread");
	    // ��������� id ���������� �� FrameLayout, �� � ������ ����������
	    tabSpec.setContent(R.id.tab1);
	    // ��������� � �������� �������
	    tabHost.addTab(tabSpec);
	       
	    tabSpec = tabHost.newTabSpec("tag2");
	    // �������� �������
	    tabSpec.setIndicator("read");
	    // ��������� id ���������� �� FrameLayout, �� � ������ ����������
	    tabSpec.setContent(R.id.tab2); 
	    // ��������� � �������� �������
	    tabHost.addTab(tabSpec);
	    
	    tabSpec = tabHost.newTabSpec("tag3");
	    // �������� �������
	    tabSpec.setIndicator("" , getResources().getDrawable(R.drawable.favorites));
	    // ��������� id ���������� �� FrameLayout, �� � ������ ����������
	    tabSpec.setContent(R.id.tab3); 
	    // ��������� � �������� �������
	    tabHost.addTab(tabSpec);
	    
	    // ������ ������� ����� ������� �� ���������
        tabHost.setCurrentTabByTag("tag1");
        
        // ���������� ������������ �������
        //tabHost.setOnTabChangedListener(new OnTabChangeListener() 
        //{
        //	public void onTabChanged(String tabId) 
        //	{
        //		Toast.makeText(getBaseContext(), "tabId = " + tabId, Toast.LENGTH_SHORT).show();
        //	}
        //});

		rssListViewUnread = (ListView) findViewById(R.id.rssListViewUnread);
		rssListViewRead = (ListView) findViewById(R.id.rssListViewRead);
		rssListViewFavorites = (ListView) findViewById(R.id.rssListViewFavorites);
		
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
				final CharSequence[] items = { "������� �� ����������" };
				AlertDialog.Builder builder3 = new AlertDialog.Builder(AndroidRSSReader.this);
				builder3.setTitle("�������� ��������").setItems(items, new DialogInterface.OnClickListener() 
				{
							@Override
							public void onClick(DialogInterface dialog, int item) 
							{
								switch (item) 
								{
									case 0: 
									{										
										DatabaseOpenHelperFeed dbhelper = new DatabaseOpenHelperFeed(getBaseContext());
										SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
										Cursor cursor1 = sqliteDB.query(Names.TABLE_NAME, null, "title=?", new String[] { "" + data3[0][pos] }, null, null, null);
										String str0 = null;
										if (cursor1 != null)
										{
											if (cursor1.moveToFirst())
											{
												str0 = cursor1.getString(cursor1.getColumnIndex("_id"));
											}
											cursor1.close();
										}
										ManControllerFeed.update(getBaseContext(), " ", Long.parseLong(str0));												
										dbhelper.close();
										sqliteDB.close();
										Intent intent = getIntent();
								        finish();
								        startActivity(intent);
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
				final CharSequence[] items = { "�������� � ���������" };
				AlertDialog.Builder builder3 = new AlertDialog.Builder(AndroidRSSReader.this);
				builder3.setTitle("�������� ��������").setItems(items, new DialogInterface.OnClickListener() 
				{
							@Override
							public void onClick(DialogInterface dialog, int item) 
							{
								switch (item) 
								{
									case 0: 
									{										
										addToFavorites(pos, "unread");
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
				final CharSequence[] items = { "�������� � ���������" };
				AlertDialog.Builder builder3 = new AlertDialog.Builder(AndroidRSSReader.this);
				builder3.setTitle("�������� ��������").setItems(items, new DialogInterface.OnClickListener() 
				{
							@Override
							public void onClick(DialogInterface dialog, int item) 
							{
								switch (item) 
								{
									case 0: 
									{										
										addToFavorites(pos, "read");
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
		    	ListActivity.BAZA_NAME = getIntent().getStringExtra("name").toString();
	    		
	    		ArrayList<RssItem> newItems = new ArrayList<RssItem>();
	    		newItems.clear();
	    		newItems = RssItem.getRssItems(feedUrl);
	    	
	    		DatabaseOpenHelperFeed dbhelper = new DatabaseOpenHelperFeed(getBaseContext());
	    		SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();	
	    		Cursor cursor1 = null;
	    		cursor1 = sqliteDB.query(DatabaseContractFeed.Names.TABLE_NAME, new String[] {"max(" + DatabaseContractFeed.Names.NamesColumns.PUPDATE + ")"}, null, null, null, null, null);
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
	    			ManControllerFeed.write(getBaseContext(), '"' + newItems.get(i).getTitle().toString() + '"', '"' + newItems.get(i).getDescription().toString() + '"', sdf.format(newItems.get(i).getPubDate()), '"' + newItems.get(i).getLink().toString() + '"', '"' +"unread" + '"', str);
	    		}
	    		refressRssList();
		    }
		});
		
	}

	public void refressRssList() 
	{		
		
		DatabaseOpenHelperFeed dbhelper = new DatabaseOpenHelperFeed(getBaseContext());
		SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();	

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
		
		final Cursor c3 = sqliteDB.query(Names.TABLE_NAME, null, Names.NamesColumns.FAVORITES + " = ?", new String[] { "favorites" }, null, null, Names.DEFAULT_SORT);
		
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
						
		rssListViewUnread.setAdapter(adapter1);			
		rssListViewRead.setAdapter(adapter2);		
		rssListViewFavorites.setAdapter(adapter3);
		
		//c.close();	
		//cursor1.close();
		//sqliteDB.close();
		//dbhelper.close();
		
		//ListActivity.BAZA_NAME = "";
	}

	private void open(int index, String temp)
	{
		//���������� ���������
		DatabaseOpenHelperFeed dbhelper = new DatabaseOpenHelperFeed(getBaseContext());
		SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();	
		Cursor cursor1 = null;
		if(temp.equals("read"))
		{
			cursor1 = sqliteDB.query(Names.TABLE_NAME, null, "title=?", new String[] { "" + data2[0][index] }, null, null, null);
		}
		else if (temp.equals("favorites"))
		{
			cursor1 = sqliteDB.query(Names.TABLE_NAME, null, "title=?", new String[] { "" + data3[0][index] }, null, null, null);
		}
		else if (temp.equals("unread"))
		{
			cursor1 = sqliteDB.query(Names.TABLE_NAME, null, "title=?", new String[] { "" + data1[0][index] }, null, null, null);
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
		refressRssList();
					
		Intent intent = new Intent(AndroidRSSReader.this, RssItemDisplayer.class);	
		intent.putExtra("title", str1);
		intent.putExtra("description", str2);
		intent.putExtra("link", str3);
		startActivity(intent);
	}
	
	private void addToFavorites(int pos,String temp)
	{
		DatabaseOpenHelperFeed dbhelper = new DatabaseOpenHelperFeed(getBaseContext());
		SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
		Cursor cursor1 = null;
		if (temp.equals("unread"))
		{
			cursor1 = sqliteDB.query(Names.TABLE_NAME, null, "title=?", new String[] { "" + data1[0][pos] }, null, null, null);
		}
		else if (temp.equals("read"))
		{
			cursor1 = sqliteDB.query(Names.TABLE_NAME, null, "title=?", new String[] { "" + data2[0][pos] }, null, null, null);
		}
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
		Intent intent = new Intent(this, main.class);
		startActivity(intent);
		return true;
	}
}