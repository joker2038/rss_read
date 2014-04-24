package menu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import rssfeed.RssItem;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import database.DatabaseContract.NamesFeed;
import database.DatabaseContract.NamesFeedList;
import database.DatabaseOpenHelper;
import database.feed.ManControllerFeed;

public class UpdateService extends Service
{
	// run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer myTimerU = null;
    public static int update_time = 0;
    public static String[][] data = null;
    //
	public static final String APP_PREFERENCES = "ru.joker2038.rssreader_preferences"; 	
	SharedPreferences mSettings;
	String update_news;
	long update_news_int;
  	myUpdate mu;
	
	@Override 
    public IBinder onBind(Intent intent) 
	{ 
        return null; 
    }
	
	@Override
	public void onCreate() 
	{ 
		// recreate new
    	myTimerU = new Timer();
    }	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
    {  
		mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		update_news = mSettings.getString("update_news", "0");
    	
		update_news_int = Long.parseLong(update_news);
		
		if (update_news_int * 3600000 < 9223372036854775807L)
		{
			myTimerU.scheduleAtFixedRate(new Update(), 0, update_news_int * 3600000);
		}
		else
		{
			myTimerU.scheduleAtFixedRate(new Update(), (update_news_int * 3600000)/2 , (update_news_int * 3600000)/2);
		}
                
        return super.onStartCommand(intent, flags, startId);
     }
	
	class Update extends TimerTask 
	{		 
        @Override
        public void run() 
        {
            // run on another thread
            mHandler.post(new Runnable()
            { 
                @Override
                public void run() 
                {
                	mu = new myUpdate();
        		    mu.execute(); 
                }
 
            });
        }
	}
	
	class myUpdate extends AsyncTask<Void, Void, Void> 
	  {
		    @Override
		    protected void onPreExecute() 
		    {
		      super.onPreExecute();
		    }

		    @Override
		    protected Void doInBackground(Void... params) 
		    {
		      try 
		      {
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
		    	  
			    	for (int j = 0; j < data[0].length; j++)
	      	    	{    		
	      	    		ArrayList<RssItem> newItems = new ArrayList<RssItem>();
	      	    		newItems.clear();
	      	    		newItems = RssItem.getRssItems(data[1][j]);
	      	    	
	      	    		DatabaseOpenHelper dbhelper = new DatabaseOpenHelper(getBaseContext());
	      	    		SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();	
	      	    		Cursor cursor1 = null;
	      	    		cursor1 = sqliteDB.query(NamesFeed.TABLE_NAME, new String[] {"max(" + NamesFeed.NamesColumns.PUPDATE + ")"}, NamesFeed.NamesColumns.NAMBER + " = ?",  new String[] { data[0][j] }, null, null, null);
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
	      	    			ManControllerFeed.write(getBaseContext(), data[0][j],'"' + newItems.get(i).getTitle().toString() + '"', '"' + newItems.get(i).getDescription().toString() + '"', sdf.format(newItems.get(i).getPubDate()), '"' + newItems.get(i).getLink().toString() + '"', '"' +"unread" + '"', str, "no_favorites");
	      		    	}
	      	    	}
		      } 
		      catch (Exception e) 
		      {
		      }
		      return null;
		    }

		    @Override
		    protected void onPostExecute(Void result)
		    {
		      super.onPostExecute(result);
		    }
	  }
      
    @Override
    public void onDestroy() 
	{ 
    	if(myTimerU != null) 
        {
    		myTimerU.cancel();
        }
    	super.onDestroy();
    }
}