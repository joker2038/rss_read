package database.menu;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import database.menu.DatabaseContractMenu.Names;

public class DatabaseOpenHelperMenu extends SQLiteOpenHelper
{    
	private static final int DATABASE_VERSION = 3;
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
		db.execSQL("CREATE TABLE " + Names.TABLE_NAME + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + Names.NamesColumns._TITLE_FONT + " TEXT NOT NULL , " + Names.NamesColumns._NEWS_FONT + " TEXT NOT NULL , " + Names.NamesColumns._CHANNAL_LIST_FONT + " TEXT NOT NULL , " + Names.NamesColumns._STORAGE_TIME + " TEXT NOT NULL ," + Names.NamesColumns._UPDATE_TIME + " TEXT NOT NULL );");
		db.execSQL("INSERT INTO " + Names.TABLE_NAME + " ( " + Names.NamesColumns._TITLE_FONT + " , " + Names.NamesColumns._NEWS_FONT + " , " + Names.NamesColumns._CHANNAL_LIST_FONT + " , " +  Names.NamesColumns._STORAGE_TIME + " , " +  Names.NamesColumns._UPDATE_TIME + " ) VALUES ( 20, 20, 20, 0, 0);");
		
		if (LOGV) 
		{
			Log.v(DEBUG_TAG, "onCreate()");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		List<String> listVersionALTER = Arrays.asList( 
														"ALTER TABLE " + Names.TABLE_NAME + " ADD COLUMN " + Names.NamesColumns._STORAGE_TIME + " TEXT;",
														"ALTER TABLE " + Names.TABLE_NAME + " ADD COLUMN " + Names.NamesColumns._UPDATE_TIME + " TEXT;"
													 );
		
		List<String> listVersionINSERT = Arrays.asList( 
														"INSERT INTO " + Names.TABLE_NAME + " ( " +  Names.NamesColumns._STORAGE_TIME + " ) VALUES (0);",
														"INSERT INTO " + Names.TABLE_NAME + " ( " +  Names.NamesColumns._UPDATE_TIME + " ) VALUES (0);"
													  );

		for (int i = oldVersion - 1; i < newVersion - 1; i++)
		{
			db.execSQL(listVersionALTER.get(i));
			db.execSQL(listVersionINSERT.get(i));
					
		}
		Log.d(DEBUG_TAG, "onUpgrade called");
	}
}
