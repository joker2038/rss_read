package menu;

import rssreader.ListActivity;
import ru.joker2038.rssreader.R;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import database.menu.DatabaseContractMenu.Names;
import database.menu.DatabaseOpenHelperMenu;
import database.menu.ManControllerMenu;

public class main  extends Activity
{	
	Boolean flagD = false;
	Boolean flagU = false;
	int flagEmptyU = 0;
	int flagEmptyD = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
    	final TextView storage_news = (TextView) findViewById(R.id.storage_news);
    	storage_news.setText("" + ListActivity.storage_time);
    	   
    	Button plus = (Button) findViewById(R.id.plus);
    	plus.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{		
				if(ListActivity.storage_time < 25)
				{					
					ListActivity.storage_time++;
					ManControllerMenu.update(getBaseContext(), Names.NamesColumns._STORAGE_TIME, String.valueOf(ListActivity.storage_time), 1);				
			    	storage_news.setText("" + ListActivity.storage_time);			
				}
			}			
		});
    	
    	Button minus = (Button) findViewById(R.id.minus);
    	minus.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{		
				if(ListActivity.storage_time > 0)
				{
					ListActivity.storage_time--;
					ManControllerMenu.update(getBaseContext(), Names.NamesColumns._STORAGE_TIME, String.valueOf(ListActivity.storage_time), 1);				
			    	storage_news.setText("" + ListActivity.storage_time);		
				}
			}			
		});  
    	
    	final TextView update_time = (TextView) findViewById(R.id.update_time);
    	update_time.setText("" + ListActivity.update_time);
    	
    	Button plusNews = (Button) findViewById(R.id.pluseNews);
    	plusNews.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{		
				if(ListActivity.update_time < 24)
				{					
					ListActivity.update_time++;
					ManControllerMenu.update(getBaseContext(), Names.NamesColumns._UPDATE_TIME, String.valueOf(ListActivity.update_time), 1);						
					update_time.setText("" + ListActivity.update_time);			
				}
			}			
		});
    	
    	Button minusNews = (Button) findViewById(R.id.minusNews);
    	minusNews.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{		
				if(ListActivity.update_time > 0)
				{
					ListActivity.update_time--;
					ManControllerMenu.update(getBaseContext(), Names.NamesColumns._UPDATE_TIME, String.valueOf(ListActivity.update_time), 1);						
					update_time.setText("" + ListActivity.update_time);;		
				}
			}			
		});  
    	
        //первая группа
        RadioButton gr0_rad0 = (RadioButton)findViewById(R.id.gr0_radio0);
        RadioButton gr0_rad1 = (RadioButton)findViewById(R.id.gr0_radio1);
        RadioButton gr0_rad2 = (RadioButton)findViewById(R.id.gr0_radio2);
        
        if (ListActivity.title_font == 10)
        {
        	gr0_rad0.setChecked(true);
        }
        else if (ListActivity.title_font == 20)
        {
        	gr0_rad1.setChecked(true);
        }
        else
        {
        	gr0_rad2.setChecked(true);
        }
        
     	//вторая группа
        RadioButton gr1_rad0 = (RadioButton)findViewById(R.id.gr1_radio0);
        RadioButton gr1_rad1 = (RadioButton)findViewById(R.id.gr1_radio1);
        RadioButton gr1_rad2 = (RadioButton)findViewById(R.id.gr1_radio2);
        
        if (ListActivity.news_font == 10)
        {
        	gr1_rad0.setChecked(true);
        }
        else if (ListActivity.news_font == 20)
        {
        	gr1_rad1.setChecked(true);
        }
        else
        {
        	gr1_rad2.setChecked(true);
        }
        
        //третья группа
        RadioButton gr2_rad0 = (RadioButton)findViewById(R.id.gr2_radio0);
        RadioButton gr2_rad1 = (RadioButton)findViewById(R.id.gr2_radio1);
        RadioButton gr2_rad2 = (RadioButton)findViewById(R.id.gr2_radio2);
        
        if (ListActivity.channel_list_font == 10)
        {
        	gr2_rad0.setChecked(true);
        }
        else if (ListActivity.channel_list_font == 20)
        {
        	gr2_rad1.setChecked(true);
        }
        else
        {
        	gr2_rad2.setChecked(true);
        }
        
        //gr0 раз
        gr0_rad0.setOnClickListener(new OnClickListener() 
        {
        	@Override
			public void onClick(View arg0) 
			{          		
        		DatabaseOpenHelperMenu dbhelper = new DatabaseOpenHelperMenu(getBaseContext());
				SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
				ManControllerMenu.update(getBaseContext(), Names.NamesColumns._TITLE_FONT, "10", 1);		
				dbhelper.close();
				sqliteDB.close();
			}
        });

        //gr0 два
        gr0_rad1.setOnClickListener(new OnClickListener() 
        {
        	@Override
			public void onClick(View arg0) 
			{  
        		DatabaseOpenHelperMenu dbhelper = new DatabaseOpenHelperMenu(getBaseContext());
				SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
				ManControllerMenu.update(getBaseContext(), Names.NamesColumns._TITLE_FONT, "20", 1);		
				dbhelper.close();
				sqliteDB.close();
			}
        });
        
        //gr0 три
        gr0_rad2.setOnClickListener(new OnClickListener() 
        {
        	@Override
			public void onClick(View arg0) 
			{  
        		DatabaseOpenHelperMenu dbhelper = new DatabaseOpenHelperMenu(getBaseContext());
				SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
				ManControllerMenu.update(getBaseContext(), Names.NamesColumns._TITLE_FONT, "40", 1);		
				dbhelper.close();
				sqliteDB.close();
			}
        });
        
        // news_font
        
        //gr1 раз
        gr1_rad0.setOnClickListener(new OnClickListener() 
        {
        	@Override
			public void onClick(View arg0) 
			{  
        		DatabaseOpenHelperMenu dbhelper = new DatabaseOpenHelperMenu(getBaseContext());
				SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
				ManControllerMenu.update(getBaseContext(), Names.NamesColumns._NEWS_FONT, "10", 1);		
				dbhelper.close();
				sqliteDB.close();
			}
        });
        
        //gr1 два
        gr1_rad1.setOnClickListener(new OnClickListener() 
        {
        	@Override
			public void onClick(View arg0) 
			{  
        		DatabaseOpenHelperMenu dbhelper = new DatabaseOpenHelperMenu(getBaseContext());
				SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
				ManControllerMenu.update(getBaseContext(), Names.NamesColumns._NEWS_FONT, "20", 1);		
				dbhelper.close();
				sqliteDB.close();
			}
        });
        
        //gr1 три
        gr1_rad2.setOnClickListener(new OnClickListener() 
        {
        	@Override
			public void onClick(View arg0) 
			{  
        		DatabaseOpenHelperMenu dbhelper = new DatabaseOpenHelperMenu(getBaseContext());
				SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
				ManControllerMenu.update(getBaseContext(), Names.NamesColumns._NEWS_FONT, "40", 1);		
				dbhelper.close();
				sqliteDB.close();
			}
        });
        
        // channel_list_font
        
        //gr1 раз
        gr2_rad0.setOnClickListener(new OnClickListener() 
        {
        	@Override
			public void onClick(View arg0) 
			{  
        		DatabaseOpenHelperMenu dbhelper = new DatabaseOpenHelperMenu(getBaseContext());
				SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
				ManControllerMenu.update(getBaseContext(), Names.NamesColumns._CHANNAL_LIST_FONT, "10", 1);		
				dbhelper.close();
				sqliteDB.close();
			}
        });
        
        //gr2 два
        gr2_rad1.setOnClickListener(new OnClickListener() 
        {
        	@Override
			public void onClick(View arg0) 
			{  
        		DatabaseOpenHelperMenu dbhelper = new DatabaseOpenHelperMenu(getBaseContext());
				SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
				ManControllerMenu.update(getBaseContext(), Names.NamesColumns._CHANNAL_LIST_FONT, "20", 1);		
				dbhelper.close();
				sqliteDB.close();
			}
        });
        
        //gr2 три
        gr2_rad2.setOnClickListener(new OnClickListener() 
        {
        	@Override
			public void onClick(View arg0) 
			{  
        		DatabaseOpenHelperMenu dbhelper = new DatabaseOpenHelperMenu(getBaseContext());
				SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
				ManControllerMenu.update(getBaseContext(), Names.NamesColumns._CHANNAL_LIST_FONT, "40", 1);		
				dbhelper.close();
				sqliteDB.close();
			}
        });     
    }    
    
    @Override
    public void onBackPressed()
    {     	
    	if (ListActivity.storage_time == 0)
    	{
    		stopService(new Intent(this, DeleteService.class));       			
    	}
    	else
    	{
    		startService(new Intent(this, DeleteService.class));
    	}
    	
        if (ListActivity.update_time == 0)
    	{
        	stopService(new Intent(this, UpdateService.class));    
    	}
    	else
    	{
    		startService(new Intent(this, UpdateService.class));
    	}
        
    	Intent intent = new Intent(main.this , ListActivity.class);
	    startActivity(intent);
	    finish();
    }      
}