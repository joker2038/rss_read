package menu;

import ru.joker2038.rssreader.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PrefActivity extends PreferenceActivity 
{

  @SuppressWarnings("deprecation")
@Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.settings);
  }
}