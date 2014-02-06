package database.feed;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.rssreader.ListActivity;

import database.feed.DatabaseContractFeed.Names;

/**  ласс создающий, удал€ющий и редактирующий базу */
public class DatabaseOpenHelperFeed extends SQLiteOpenHelper
{
	private static final int DATABASE_VERSION = 1;
	private static final String DEBUG_TAG = DatabaseOpenHelperFeed.class.getSimpleName();
	private static final boolean LOGV = false;

	public DatabaseOpenHelperFeed(Context context) 
	{
		super(context, ListActivity.BAZA_NAME + ".db", null, DATABASE_VERSION);
	}

	/** ”даление всех таблиц из базы
	 * 
	 * @param db
	 *            - object of SQLiteDatabase */
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
		db.execSQL("CREATE TABLE " + Names.TABLE_NAME + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + Names.NamesColumns.TITLE + " TEXT NOT NULL , " + Names.NamesColumns.DESCRIPTION + " TEXT NOT NULL , " + Names.NamesColumns.PUPDATE + " INTEGER NOT NULL , " + Names.NamesColumns.LINK + " TEXT NOT NULL , " + Names.NamesColumns.READ + " TEXT NOT NULL );");
		
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
