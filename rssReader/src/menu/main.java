package menu;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.rssreader.ListActivity;
import com.example.rssreader.MainActivity;
import com.example.rssreader.R;

import database.feed.DatabaseOpenHelperFeed;
import database.feed.ManControllerFeed;
import database.menu.DatabaseOpenHelperMenu;
import database.menu.ManControllerMenu;

public class main  extends Activity
{	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
    	final TextView storage_news = (TextView) findViewById(R.id.storage_news);
    	
    	storage_news.setText("" + MainActivity.storage_time);
    	
    	Button plus = (Button) findViewById(R.id.plus);
    	
    	plus.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{		
				if(MainActivity.storage_time < 25)
				{
					MainActivity.storage_time++;
					ManControllerMenu.update_storage_time(getBaseContext(), String.valueOf(MainActivity.storage_time), 1);
					storage_news.setText("" + MainActivity.storage_time);		
				}
			}			
		});
    	
    	Button minus = (Button) findViewById(R.id.minus);
    	
    	minus.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{		
				if(MainActivity.storage_time > 0)
				{
					MainActivity.storage_time--;
					ManControllerMenu.update_storage_time(getBaseContext(), String.valueOf(MainActivity.storage_time), 1);				
					storage_news.setText("" + MainActivity.storage_time);	
				}
			}			
		});   
    	
        //первая группа
        RadioButton gr0_rad0 = (RadioButton)findViewById(R.id.gr0_radio0);
        RadioButton gr0_rad1 = (RadioButton)findViewById(R.id.gr0_radio1);
        RadioButton gr0_rad2 = (RadioButton)findViewById(R.id.gr0_radio2);
        
        if (MainActivity.title_font == 10)
        {
        	gr0_rad0.setChecked(true);
        }
        else if (MainActivity.title_font == 20)
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
        
        if (MainActivity.news_font == 10)
        {
        	gr1_rad0.setChecked(true);
        }
        else if (MainActivity.news_font == 20)
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
        
        if (MainActivity.channel_list_font == 10)
        {
        	gr2_rad0.setChecked(true);
        }
        else if (MainActivity.channel_list_font == 20)
        {
        	gr2_rad1.setChecked(true);
        }
        else
        {
        	gr2_rad2.setChecked(true);
        }
        
        //удалить все базы
        Button all = (Button) findViewById(R.id.all);
        //удалить одну базу
        Button one = (Button) findViewById(R.id.one);
              
        // title_font
        
        //gr0 раз
        gr0_rad0.setOnClickListener(new OnClickListener() 
        {
        	@Override
			public void onClick(View arg0) 
			{          		
        		DatabaseOpenHelperMenu dbhelper = new DatabaseOpenHelperMenu(getBaseContext());
				SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
				ManControllerMenu.update_title_font(getBaseContext(), "10", 1);		
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
				ManControllerMenu.update_title_font(getBaseContext(), "20", 1);		
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
				ManControllerMenu.update_title_font(getBaseContext(), "40", 1);		
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
				ManControllerMenu.update_news_font(getBaseContext(), "10", 1);		
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
				ManControllerMenu.update_news_font(getBaseContext(), "20", 1);		
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
				ManControllerMenu.update_news_font(getBaseContext(), "40", 1);		
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
				ManControllerMenu.update_channel_list_font(getBaseContext(), "10", 1);		
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
				ManControllerMenu.update_channel_list_font(getBaseContext(), "20", 1);		
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
				ManControllerMenu.update_channel_list_font(getBaseContext(), "40", 1);		
				dbhelper.close();
				sqliteDB.close();
			}
        });
    
        // удалить все базы
        all.setOnClickListener(new OnClickListener() 
        {
        	@Override
			public void onClick(View arg0) 
			{  
        		File []fList;        
        		File F = new File("data/data/com.example.rssreader/databases/");
        		        
        		fList = F.listFiles();
        		                
        		for(int i=0; i<fList.length; i++)           
        		{
        		     //Нужны только папки в место isFile() пишим isDirectory()
        		     if (fList[i].isFile())
        		     {
        		         if(fList[i].getName().equals("setting.db") || fList[i].getName().equals("setting.db-journal") || fList[i].getName().equals("FeedList.db-journal") || fList[i].getName().equals("FeedList.db"))
        		         {        		           		        	 
        		         }
        		         else 
        		         {        		           	 
        		        	 fList[i].delete();  
        		         }
        		     }
        		}
			}
        });
    
        // удалить одну базу
        one.setOnClickListener(new OnClickListener() 
        {
    		File []fList;        
    		File F = new File("data/data/com.example.rssreader/databases/");
    		int[] nomer;
        	@Override
			public void onClick(View arg0) 
			{          		        
        		fList = F.listFiles();
        		nomer = new int[fList.length];
        		int temp123 = 0;
        		//final CharSequence[] items = { };
        		ArrayList<String> massiv = new ArrayList<String>();
        		
        		for(int i=0; i<fList.length; i++)           
        		{
        		     //Нужны только папки в место isFile() пишим isDirectory()
        		     if (fList[i].isFile())
        		     {
        		         if(fList[i].getName().equals("setting.db") || fList[i].getName().equals("FeedList.db") || fList[i].getName().matches(".*-journal") )
        		         {        		           		        	 
        		         }
        		         else 
        		         {        		           	 
        		        	 massiv.add(fList[i].getName());  
        		        	 nomer[temp123] = i;
        		        	 temp123++;
        		         }
        		     }
        		}
        		
        		final CharSequence[] items = massiv.toArray(new CharSequence[massiv.size()]);;
        		
				AlertDialog.Builder builder3 = new AlertDialog.Builder(main.this);
				builder3.setTitle("Выберите базу").setItems(items, new DialogInterface.OnClickListener() 
				{
							@Override
							public void onClick(DialogInterface dialog, int item) 
							{
								fList[nomer[item]].delete();
							}
						});
				builder3.show();
			}
        });    
    }
   
    public void delete() 
	{
    	File []fList;        
		File F = new File("data/data/com.example.rssreader/databases/");
    	fList = F.listFiles();
		for(int i=0; i<fList.length; i++)           
		{
		     //Нужны только папки в место isFile() пишим isDirectory()
		     if (fList[i].isFile())
		     {
		         if(!fList[i].getName().equals("setting.db") && !fList[i].getName().equals("FeedList.db") && !fList[i].getName().matches(".*-journal") )
		         {   
		        	 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		        	 Calendar Current_Calendar = Calendar.getInstance();
		        	 Date Current_Date = Current_Calendar.getTime();
		        	 long dateTemp = 0;
		      		 String dateNow = sdf.format(Current_Date);
		         	 ListActivity.BAZA_NAME = fList[i].getName().replace(".db", "");  
		          	 DatabaseOpenHelperFeed dbhelper = new DatabaseOpenHelperFeed(getBaseContext());
		     		 SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();		      		 
		     		 dateTemp = Long.parseLong(dateNow) - MainActivity.storage_time * 1000000 ;
		     		 ManControllerFeed.delete(getBaseContext(), dateTemp);	
		     		 dbhelper.close();
		     		 sqliteDB.close();
		        	 //main.delete(fList[i].getName().toString());
		         }
		     }
		}

		
	}
  
    @Override
    public void onBackPressed()
    {        
        Timer     timer = new Timer();
    	
    	if (MainActivity.storage_time > 0)
    	{
	    	timer.schedule( new TimerTask()
	    						{          
	    							@Override
	    							public void run() 
	    							{
	    								delete();
	    							}
	    						}
	    					, 0, MainActivity.storage_time );
    	}
    	
    	Intent intent = new Intent(main.this , MainActivity.class);
	    startActivity(intent);
    }
}