package com.example.rssreader;

import menu.main;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import database.ManController;


public class MainActivity  extends Activity
{	
	final Context context = this;
		
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
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
			    finish();
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
		// Операции для выбранного пункта меню
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
