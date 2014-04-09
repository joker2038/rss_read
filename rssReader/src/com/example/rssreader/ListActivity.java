package com.example.rssreader;

import menu.main;
import rssfeed.AndroidRSSReader;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import database.DatabaseContract.NamesFeed;
import database.DatabaseContract.NamesFeedList;
import database.DatabaseOpenHelper;
import database.ManController;

public class ListActivity extends Activity 
{	
	final Context context = this;
	String[] urllink;
	int rowId = 0;	
	ArrayAdapter<String> adapter = null;
		
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_list);
				
		DatabaseOpenHelper dbhelper = new DatabaseOpenHelper(getBaseContext());
		SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
		final Cursor c = sqliteDB.query(NamesFeedList.TABLE_NAME, null, null, null, null, null, NamesFeedList.DEFAULT_SORT);
				
		if (c != null)
		{
			int iNew = -1;
			MainActivity.data = new String[3][c.getCount()];
		    while(c.moveToNext()) 
		    {		  
		    	iNew++;
		    	MainActivity.data[0][iNew] = c.getString(0);
		        MainActivity.data[1][iNew] = c.getString(1);
		        MainActivity.data[2][iNew] = c.getString(2);
		    }
		    c.close();
		}
				
		if (MainActivity.channel_list_font == 10)
        {
			adapter = new ArrayAdapter<String>(this, R.layout.my_list_feed_small_size, MainActivity.data[2]);
        }
        else if (MainActivity.channel_list_font == 20)
        {
        	adapter = new ArrayAdapter<String>(this, R.layout.my_list_feed_average_size, MainActivity.data[2]);
        }
        else
        {
        	adapter = new ArrayAdapter<String>(this, R.layout.my_list_feed_large_size, MainActivity.data[2]);
        }
		
		final ListView lv = (ListView) findViewById(R.id.listView1);				
		
		lv.setAdapter(adapter);
			
		lv.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) 
			{
				Intent intent = new Intent(ListActivity.this, AndroidRSSReader.class);	
				intent.putExtra("id", MainActivity.data[0][position]);
				intent.putExtra("urlLink", MainActivity.data[1][position]);
				intent.putExtra("name", MainActivity.data[2][position]);
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
										sqliteDB.delete(NamesFeed.TABLE_NAME, NamesFeed.NamesColumns.NAMBER + " = " + MainActivity.data[0][pos], null);
										ManController.delete(getBaseContext(), Long.parseLong(MainActivity.data[0][pos]));											
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
										userInput.setText(MainActivity.data[1][pos]);
										// вешаем на него событие
										alertDialogBuilder.setCancelable(false).setPositiveButton("OK",	new DialogInterface.OnClickListener() 
												{
													@Override
													public void onClick(DialogInterface dialog, int id) 
													{
														DatabaseOpenHelper dbhelper = new DatabaseOpenHelper(getBaseContext());
														SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
														ManController.update(getBaseContext(), userInput.getText().toString(), Long.parseLong(MainActivity.data[0][pos]));		
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

