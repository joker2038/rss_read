package rssreader;

import menu.PrefActivity;
import rssfeed.AndroidRSSReader;
import ru.joker2038.rssreader.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import database.DatabaseContract.NamesFeed;
import database.DatabaseContract.NamesFeedList;
import database.DatabaseOpenHelper;
import database.ManController;
import database.menu.DatabaseContractMenu;
import database.menu.DatabaseOpenHelperMenu;

public class ListActivity extends Activity 
{	
	final Context context = this;
	String[] urllink;
	int rowId = 0;	
	ArrayAdapter<String> adapter = null;
	public static String[][] data = null;
	public static int title_font = 0;
	public static int news_font = 0;
	public static int channel_list_font = 0;
	public static int storage_time = 0;
	public static int update_time = 0;
	//
	public static final int IDM_PREF = 101;
		
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_list);
		
		Button plus = (Button) findViewById(R.id.button1);
		
		plus.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(ListActivity.this , MainActivity.class);
			    startActivity(intent);
			}			
		});
		
		DatabaseOpenHelper dbhelper1 = new DatabaseOpenHelper(getBaseContext());
		SQLiteDatabase sqliteDB1 = dbhelper1.getReadableDatabase();
		final Cursor c1 = sqliteDB1.query(NamesFeedList.TABLE_NAME, null, null, null, null, null, NamesFeedList.DEFAULT_SORT);
				
		if (c1 != null)
		{
			int iNew = -1;
			data = new String[3][c1.getCount()];
		    while(c1.moveToNext()) 
		    {		  
		    	iNew++;
		    	data[0][iNew] = c1.getString(0);
		        data[1][iNew] = c1.getString(1);
		        data[2][iNew] = c1.getString(2);
		    }
		    c1.close();
		}
		dbhelper1.close();
		sqliteDB1.close();
        
        DatabaseOpenHelperMenu dbhelper = new DatabaseOpenHelperMenu(getBaseContext());
		SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
		final Cursor c = sqliteDB.query(DatabaseContractMenu.Names.TABLE_NAME, null, null, null, null, null, null);
		if (c != null)
		{
		      if (c.moveToFirst())
		      {
		    	  title_font = Integer.parseInt(c.getString(c.getColumnIndex("title_font")));
		    	  news_font = Integer.parseInt(c.getString(c.getColumnIndex("news_font")));
		    	  channel_list_font = Integer.parseInt(c.getString(c.getColumnIndex("channel_list_font")));
		    	  storage_time = Integer.parseInt(c.getString(c.getColumnIndex("storage_time")));
		    	  update_time = Integer.parseInt(c.getString(c.getColumnIndex("update_time")));
		      }
		      c.close();
		}
		dbhelper.close();
		sqliteDB.close();
								
		if (channel_list_font == 10)
        {
			adapter = new ArrayAdapter<String>(this, R.layout.my_list_feed_small_size, data[2]);
        }
        else if (channel_list_font == 20)
        {
        	adapter = new ArrayAdapter<String>(this, R.layout.my_list_feed_average_size, data[2]);
        }
        else
        {
        	adapter = new ArrayAdapter<String>(this, R.layout.my_list_feed_large_size, data[2]);
        }
		
		final ListView lv = (ListView) findViewById(R.id.listView1);				
		
		lv.setAdapter(adapter);
			
		lv.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) 
			{
				Intent intent = new Intent(ListActivity.this, AndroidRSSReader.class);	
				intent.putExtra("id", data[0][position]);
				intent.putExtra("urlLink", data[1][position]);
				intent.putExtra("name", data[2][position]);
				startActivity(intent);
			}
		});
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() 
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) 
			{
				final CharSequence[] items = { "Удалить", "Сменить ссылку" };
				AlertDialog.Builder builder3 = new AlertDialog.Builder(ListActivity.this);
				builder3.setTitle("Выберите действие").setItems(items, new DialogInterface.OnClickListener() 
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
										sqliteDB.delete(NamesFeed.TABLE_NAME, NamesFeed.NamesColumns.NAMBER + " = " + data[0][pos], null);
										ManController.delete(getBaseContext(), Long.parseLong(data[0][pos]));											
										dbhelper.close();
										sqliteDB.close();
										Intent intent = getIntent();
								        finish();
								        startActivity(intent);
									}
										break;
									case 1: 
									{															
										// подключаем наш кастомный диалог лайаут
										LayoutInflater li = LayoutInflater.from(context);
										View promptsView = li.inflate(R.layout.new_name, null);
										AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
										// делаем его диалогом
										alertDialogBuilder.setView(promptsView);
										final EditText userInput = (EditText) promptsView.findViewById(R.id.newNameEditText);
										userInput.setText(data[1][pos]);
										// вешаем на него событие
										alertDialogBuilder.setCancelable(false).setPositiveButton("OK",	new DialogInterface.OnClickListener() 
												{
													@Override
													public void onClick(DialogInterface dialog, int id) 
													{
														DatabaseOpenHelper dbhelper = new DatabaseOpenHelper(getBaseContext());
														SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
														ManController.update(getBaseContext(), userInput.getText().toString(), Long.parseLong(data[0][pos]));		
														dbhelper.close();
														sqliteDB.close();
														Intent intent = getIntent();
												        finish();
												        startActivity(intent);
													}
												})
												.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
												{
													@Override
													public void onClick(DialogInterface dialog, int id) 
													{
														dialog.cancel();
													}
												});
										 // создаем диалог
										AlertDialog alertDialog = alertDialogBuilder.create();
										// показываем его
										alertDialog.show();
									}
										break;
								}
							}
						});
				builder3.show();
				return true;
			}
		});
		dbhelper.close();
		sqliteDB.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0,  IDM_PREF, 0, "Настройки");
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
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	public void onBackPressed()
    {    
    }
}

