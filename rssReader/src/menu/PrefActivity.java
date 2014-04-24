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
		Intent intentStorage = null;	  	
	  	Intent intentUpdate = null;	  	

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
			
			intentStorage = new Intent(this, DeleteService.class);		  	
		  	intentUpdate = new Intent(this, UpdateService.class);		
		  	
		  	if (Integer.parseInt(storage_news) == 0)
		  	{
		  		stopService(intentStorage);       			
		  	}
		  	else
		  	{
		  		startService(intentStorage);
		  	}
		  	
		  	if (Integer.parseInt(update_news) == 0)
			{
		  		stopService(intentUpdate);    
			}
			else
			{
				startService(intentUpdate);
			}		    
		    
		  	Intent intent = new Intent(PrefActivity.this , ListActivity.class);
			startActivity(intent);
			finish();
	  } 
}