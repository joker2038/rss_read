package menu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import database.DatabaseOpenHelper;
import database.feed.ManControllerFeed;

public class DeleteService extends Service 
{
	// run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer myTimerD = null;
    //
	public static final String APP_PREFERENCES = "ru.joker2038.rssreader_preferences"; 	
	SharedPreferences mSettings;
	String storage_news;
	long storage_news_int;
	myStorage ms;
	
	@Override 
    public IBinder onBind(Intent intent) 
	{ 
        return null; 
    }
	
	@Override
	public void onCreate() 
	{ 
		// recreate new
    	myTimerD = new Timer();
    }
    
    public int onStartCommand(Intent intent, int flags, int startId) 
    {   
    	mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    	storage_news = mSettings.getString("storage_news", "0");
    	
    	storage_news_int = Long.parseLong(storage_news);
    	
		if (storage_news_int * 86400000 < 9223372036854775807L)
		{
			myTimerD.scheduleAtFixedRate(new Delete(), 0, storage_news_int * 86400000);
		}
		else
		{
			myTimerD.scheduleAtFixedRate(new Delete(), (storage_news_int * 86400000)/2 , (storage_news_int * 86400000)/2 );
		}
		
        return super.onStartCommand(intent, flags, startId);
     }
    
    class Delete extends TimerTask 
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
                	ms = new myStorage();
        		  	ms.execute();                	
                } 
            });
        }
	}
    
    class myStorage extends AsyncTask<Void, Void, Void> 
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
		    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
  		  		Calendar Current_Calendar = Calendar.getInstance();
  		  		Date Current_Date = Current_Calendar.getTime();
  		  		long dateTemp = 0;
  		  		String dateNow = sdf.format(Current_Date);
  		  		DatabaseOpenHelper dbhelper = new DatabaseOpenHelper(getBaseContext());
  		  		SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();		      		 
  		  		dateTemp = Long.parseLong(dateNow) - storage_news_int * 1000000 ;
  		  		ManControllerFeed.delete(getBaseContext(), dateTemp);	
  		  		dbhelper.close();
  		  		sqliteDB.close();
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
    	if(myTimerD != null) 
        {
        	myTimerD.cancel();
        }
    	super.onDestroy();
    } 
}
