package menu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import database.DatabaseOpenHelper;
import database.feed.ManControllerFeed;
import database.menu.DatabaseContractMenu;
import database.menu.DatabaseOpenHelperMenu;

public class DeleteService extends Service 
{
	// run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer myTimerD = null;
    public static int storage_time = 0;
	
	@Override 
    public IBinder onBind(Intent intent) 
	{ 
        return null; 
    }
	
	@Override
	public void onCreate() 
	{ 
		// cancel if already existed
        if(myTimerD != null) 
        {
        	myTimerD.cancel();
        } else 
        {
            // recreate new
        	myTimerD = new Timer();
        }
    }
    
    public int onStartCommand(Intent intent, int flags, int startId) 
    {   
    	DatabaseOpenHelperMenu dbhelper1 = new DatabaseOpenHelperMenu(getBaseContext());
		SQLiteDatabase sqliteDB1 = dbhelper1.getReadableDatabase();
		final Cursor c = sqliteDB1.query(DatabaseContractMenu.Names.TABLE_NAME, null, null, null, null, null, null);
		if (c != null)
		{
		      if (c.moveToFirst())
		      {
		    	  storage_time = Integer.parseInt(c.getString(c.getColumnIndex("storage_time")));
		      }
		      c.close();
		}
		dbhelper1.close();
		sqliteDB1.close();
    	
    	myTimerD.scheduleAtFixedRate(new Delete(), 0, storage_time * 86400000);   	
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
                	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
    		  		Calendar Current_Calendar = Calendar.getInstance();
    		  		Date Current_Date = Current_Calendar.getTime();
    		  		long dateTemp = 0;
    		  		String dateNow = sdf.format(Current_Date);
    		  		DatabaseOpenHelper dbhelper = new DatabaseOpenHelper(getBaseContext());
    		  		SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();		      		 
    		  		dateTemp = Long.parseLong(dateNow) - storage_time * 1000000 ;
    		  		ManControllerFeed.delete(getBaseContext(), dateTemp);	
    		  		dbhelper.close();
    		  		sqliteDB.close();
                } 
            });
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
