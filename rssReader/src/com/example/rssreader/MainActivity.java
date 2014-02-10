package com.example.rssreader;

import menu.main;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import database.ManController;
import database.menu.DatabaseContractMenu;
import database.menu.DatabaseOpenHelperMenu;


public class MainActivity  extends Activity
{	
	public static int title_font = 0;
	public static int news_font = 0;
	public static int channel_list_font = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        		
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
		      }
		      c.close();
		}
		dbhelper.close();
		sqliteDB.close();
		
        final EditText nameEdit = (EditText) findViewById(R.id.nameEdit);
        final EditText urlLinkEdit = (EditText) findViewById(R.id.urlLinkEdit);
          
        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{				
				if (nameEdit.getText().toString().equals("") || nameEdit.getText().toString().equals("enter name"))
				{
					nameEdit.setText("enter name");
				}
				else if (urlLinkEdit.getText().toString().equals("") || urlLinkEdit.getText().toString().equals("enter url"))
				{
					urlLinkEdit.setText("enter url");
				}
				else
				{
				ManController.write(getBaseContext(), '"' + urlLinkEdit.getText().toString() + '"' , '"' + nameEdit.getText().toString() + '"');
				urlLinkEdit.setText("");
				nameEdit.setText("");
				}
				
			}			
		});
        
        Button feedList = (Button) findViewById(R.id.feedList);
        feedList.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MainActivity.this , ListActivity.class);
			    startActivity(intent);
			}			
		});
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
