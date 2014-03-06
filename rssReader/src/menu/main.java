package menu;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import rssfeed.RssItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

import database.feed.DatabaseContractFeed;
import database.feed.DatabaseOpenHelperFeed;
import database.feed.ManControllerFeed;
import database.menu.DatabaseContractMenu.Names;
import database.menu.DatabaseOpenHelperMenu;
import database.menu.ManControllerMenu;

public class main  extends Activity
{	
	Timer timer1 = new Timer();
	boolean flagTimer1 = false;
	Timer timer2 = new Timer();
	boolean flagTimer2 = false;
	
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
					ManControllerMenu.update(getBaseContext(), Names.NamesColumns._STORAGE_TIME, String.valueOf(MainActivity.storage_time), 1);				
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
					ManControllerMenu.update(getBaseContext(), Names.NamesColumns._STORAGE_TIME, String.valueOf(MainActivity.storage_time), 1);				
			    	storage_news.setText("" + MainActivity.storage_time);		
				}
			}			
		});  
    	
    	final TextView update_time = (TextView) findViewById(R.id.update_time);
    	update_time.setText("" + MainActivity.update_time);
    	
    	Button plusNews = (Button) findViewById(R.id.pluseNews);
    	plusNews.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{		
				if(MainActivity.update_time < 24)
				{					
					MainActivity.update_time++;
					ManControllerMenu.update(getBaseContext(), Names.NamesColumns._UPDATE_TIME, String.valueOf(MainActivity.update_time), 1);						
					update_time.setText("" + MainActivity.update_time);			
				}
			}			
		});
    	
    	Button minusNews = (Button) findViewById(R.id.minusNews);
    	minusNews.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{		
				if(MainActivity.update_time > 0)
				{
					MainActivity.update_time--;
					ManControllerMenu.update(getBaseContext(), Names.NamesColumns._UPDATE_TIME, String.valueOf(MainActivity.update_time), 1);						
					update_time.setText("" + MainActivity.update_time);;		
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
        		     if (fList[i].isFile() && !fList[i].getName().equals("setting.db") && !fList[i].getName().equals("FeedList.db") && !fList[i].getName().matches(".*-journal"))
        		     { 
        		    	 fList[i].delete();  
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
        			if (fList[i].isFile() && !fList[i].getName().equals("setting.db") && !fList[i].getName().equals("FeedList.db") && !fList[i].getName().matches(".*-journal"))
       		     	{        		           	 
        				massiv.add(fList[i].getName());  
        				nomer[temp123] = i;
        				temp123++;
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
		     if (fList[i].isFile() && !fList[i].getName().equals("setting.db") && !fList[i].getName().equals("FeedList.db") && !fList[i].getName().matches(".*-journal") )
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
		     }
		}
		flagTimer1 = false;
	}
     
    public void update() 
	{    		
    	for (int j = 0; j < MainActivity.data[0].length; j++)
    	{
    		ListActivity.BAZA_NAME = "";
    		ListActivity.BAZA_NAME = MainActivity.data[2][j].replace(" ", "");
    		
    		ArrayList<RssItem> newItems = new ArrayList<RssItem>();
    		newItems.clear();
    		newItems = RssItem.getRssItems(MainActivity.data[1][j]);
    	
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
    	}
    	flagTimer2 = false;
	}     
    
    @Override
    public void onBackPressed()
    {  
    	if (flagTimer2 == false)
    	{
    		flag2:
    		if (flagTimer1 == true)
    		{
    			break flag2;  	   	
    		}
    		
	    	if (MainActivity.update_time == 1)
	    	{
	    		timer2.schedule( new TimerTask()
	    		{          
	    			@Override
	    			public void run() 
	    			{
	    				update();
	    			}
	    		}
	    		, 0, MainActivity.update_time * 3600000);
	    		flagTimer2 = true;
	    	}
	    	else if (MainActivity.update_time > 1)
	    	{
	    		timer2.schedule( new TimerTask()
	    		{          
	    			@Override
	    			public void run() 
	    			{
	    				update();
	    			}
	    		}
	    		, MainActivity.update_time/2 * 1800000, MainActivity.update_time/2 * 1800000);
	    		flagTimer2 = true;
	    	}
    	}
    	
    	if (flagTimer1 == false)
    	{
    		flag1:
    		if (flagTimer2 == true)
    		{
    			break flag1;  	
    		}
    		
	    	if (MainActivity.storage_time == 1)
	    	{
	    		timer1.schedule( new TimerTask()
	    		{          
	    			@Override
	    			public void run() 
	    			{
	    				delete();
	    			}
	    		}
	    		, 0, MainActivity.storage_time * 86400000);
	    		flagTimer1 = true;
	    	}
	    	else if (MainActivity.storage_time > 1)
	    	{
	    		timer1.schedule( new TimerTask()
	    		{          
	    			@Override
	    			public void run() 
	    			{
	    				delete();
	    			}
	    		}
	    		, MainActivity.storage_time/2 * 43200000, MainActivity.storage_time/2 * 43200000);
	    		flagTimer1 = true;
	    	}
    	}
    	
    	Intent intent = new Intent(main.this , MainActivity.class);
	    startActivity(intent);
    }
}