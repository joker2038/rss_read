package rssreader;

import menu.PrefActivity;
import ru.joker2038.rssreader.R;
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
	public static final int IDM_PREF = 101;
	public static final int IDM_PR = 102;
		
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final EditText nameEdit = (EditText) findViewById(R.id.nameEdit);
        final EditText urlLinkEdit = (EditText) findViewById(R.id.LinkEdit);
          
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
		menu.add(0,  IDM_PREF, 0, "���������");
		menu.add(0,  IDM_PR, 0, "� ���������");
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
	    	// ���������� ��� ��������� ������ ������
	    	LayoutInflater li = LayoutInflater.from(context);
	    	View promptsView = li.inflate(R.layout.about, null);
	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
	    	// ������ ��� ��������
	    	alertDialogBuilder.setView(promptsView);
	    	// ������� ������
	    	AlertDialog alertDialog = alertDialogBuilder.create();
	    	// ���������� ���
	    	alertDialog.show();
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	public void onBackPressed()
    {     	
		Intent intent = new Intent(MainActivity.this , ListActivity.class);;
		startActivity(intent);
		finish();
    } 
}
