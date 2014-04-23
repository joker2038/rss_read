package rssreader;

import menu.PrefActivity;
import rssfeed.AndroidRSSReader;
import ru.joker2038.rssreader.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ListActivity extends Activity 
{	
	final Context context = this;
	String[] urllink;
	int rowId = 0;	
	ArrayAdapter<String> adapter = null;
	public static String[][] data = null;
	//
	public static final int IDM_PREF = 101;
	public static final int IDM_PR = 102;
	public static final String APP_PREFERENCES = "ru.joker2038.rssreader_preferences"; 	
	SharedPreferences mSettings;
	String font_size_of_the_channel_list;
		
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_list);
		
		mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		font_size_of_the_channel_list = mSettings.getString("font_size_of_the_channel_list", "20");
		
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
		
        
		if (Integer.parseInt(font_size_of_the_channel_list) == 10)
        {
			adapter = new ArrayAdapter<String>(this, R.layout.my_list_feed_small_size, data[2]);
        }
        else if (Integer.parseInt(font_size_of_the_channel_list) == 20)
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
    }
}

