package database.menu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import database.menu.DatabaseContractMenu.Names;

public class DatabaseOpenHelperMenu extends SQLiteOpenHelper
{    
	private static final int DATABASE_VERSION = 1;
	private static final String DEBUG_TAG = DatabaseOpenHelperMenu.class.getSimpleName();
	private static final boolean LOGV = false;

	public DatabaseOpenHelperMenu(Context context) 
	{
		super(context, "setting.db", null, DATABASE_VERSION); //<- найти путь
	}	
	
	public void dropTables(SQLiteDatabase db) 
	{
		if (LOGV)
		{
			Log.d(DEBUG_TAG, "onDropTables called");
		}
		db.execSQL("DROP TABLE IF EXISTS " + Names.TABLE_NAME);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("CREATE TABLE " + Names.TABLE_NAME + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + Names.NamesColumns._TITLE_FONT + " TEXT NOT NULL , " + Names.NamesColumns._NEWS_FONT + " TEXT NOT NULL , " + Names.NamesColumns._CHANNAL_LIST_FONT + " INTEGER NOT NULL );");
		db.execSQL("INSERT INTO " + Names.TABLE_NAME + " ( " + Names.NamesColumns._TITLE_FONT + " , " + Names.NamesColumns._NEWS_FONT + " , " + Names.NamesColumns._CHANNAL_LIST_FONT + " ) VALUES ( 40, 40, 40);");
		
		if (LOGV) 
		{
			Log.v(DEBUG_TAG, "onCreate()");
		}
		}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		Log.d(DEBUG_TAG, "onUpgrade called");
	}
}
