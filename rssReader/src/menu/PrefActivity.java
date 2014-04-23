package menu;

import rssreader.ListActivity;
import ru.joker2038.rssreader.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PrefActivity extends PreferenceActivity 
{
		public static final String APP_PREFERENCES = "ru.joker2038.rssreader_preferences"; 
		SharedPreferences mSettings;
		String storage_news;
		String update_news;

	  @SuppressWarnings("deprecation")
	  @Override
	  protected void onCreate(Bundle savedInstanceState) 
	  {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.settings);
	  }
	  
	  public void onBackPressed()
	  {     	
		  	mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		  	storage_news = mSettings.getString("storage_news", "0");
		  	update_news = mSettings.getString("update_news", "0");
			
		  	myThreadStorage.start();
		    
		  	myThreadUpdate.start();
		  	 
		  	Intent intent = new Intent(PrefActivity.this , ListActivity.class);
			startActivity(intent);
			finish();
	  } 
	  
	  Thread myThreadStorage = new Thread(new Runnable() 
	  {
		    @Override
		    public void run() 
		    {
		    	TaskStorage();
		    }
	  });
	  
	  Thread myThreadUpdate = new Thread(new Runnable() 
	  {
		    @Override
		    public void run() 
		    {
		    	TaskUpdate();
		    }
	  });
	  
	  public void TaskStorage()
	  {
		  if (Integer.parseInt(storage_news) == 0)
		  	{
		  		stopService(new Intent(this, DeleteService.class));       			
		  	}
		  	else
		  	{
		  		startService(new Intent(this, DeleteService.class));
		  	}
	  }
	  
	  public void TaskUpdate()
	  {
		  if (Integer.parseInt(update_news) == 0)
		  	{
		      	stopService(new Intent(this, UpdateService.class));    
		  	}
		  	else
		  	{
		  		startService(new Intent(this, UpdateService.class));
		  	}
	  }
}