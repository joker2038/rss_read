package database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import database.DatabaseContract.NamesFeed;
import database.DatabaseContract.NamesFeedList;

/** Класс создающий, удаляющий и редактирующий базу */
public class DatabaseOpenHelper extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "FeedList.db";
	private static final int DATABASE_VERSION = 1;

	public DatabaseOpenHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/** Удаление всех таблиц из базы
	 * 
	 * @param db
	 *            - object of SQLiteDatabase */
	public void dropTables(SQLiteDatabase db) 
	{
		db.execSQL("DROP TABLE IF EXISTS " + NamesFeedList.TABLE_NAME); //список		
		db.execSQL("DROP TABLE IF EXISTS " + NamesFeed.TABLE_NAME); //новости
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("CREATE TABLE " + NamesFeedList.TABLE_NAME + " (" + NamesFeedList.NamesColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + NamesFeedList.NamesColumns.URL + " TEXT NOT NULL, " + NamesFeedList.NamesColumns.NAME + " TEXT NOT NULL );"); //список
		db.execSQL("CREATE TABLE " + NamesFeed.TABLE_NAME + " (" + NamesFeed.NamesColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + NamesFeed.NamesColumns.TITLE + " TEXT NOT NULL , " + NamesFeed.NamesColumns.DESCRIPTION + " TEXT NOT NULL , " + NamesFeed.NamesColumns.PUPDATE + " INTEGER NOT NULL , " + NamesFeed.NamesColumns.LINK + " TEXT NOT NULL , " + NamesFeed.NamesColumns.READ + " TEXT NOT NULL , " + NamesFeed.NamesColumns.FAVORITES + " TEXT NOT NULL , " + NamesFeed.NamesColumns.NAMBER + " INTEGER NOT NULL );"); //новости
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
	}
}
