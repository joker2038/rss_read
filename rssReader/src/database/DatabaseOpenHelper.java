package database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import database.DatabaseContract.Names;

/**  ласс создающий, удал€ющий и редактирующий базу */
public class DatabaseOpenHelper extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "FeedList.db";
	private static final int DATABASE_VERSION = 1;
	private static final String DEBUG_TAG = DatabaseOpenHelper.class.getSimpleName();
	private static final boolean LOGV = false;

	public DatabaseOpenHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
		if (LOGV) 
		{
			Log.v(DEBUG_TAG, "onCreate()");
		}
		db.execSQL("CREATE TABLE " + Names.TABLE_NAME + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + Names.NamesColumns.URL + " TEXT NOT NULL, " + Names.NamesColumns.NAME + " TEXT NOT NULL );");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		Log.d(DEBUG_TAG, "onUpgrade called");
	}
}
