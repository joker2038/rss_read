package database.menu;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.BaseColumns;
import android.util.Log;

import database.menu.DatabaseContractMenu.Names;
import database.menu.DatabaseContractMenu.Names.NamesColumns;

public class ManControllerMenu 
{
	private static final boolean LOGV = false;
	private static int maxRowsInNames = -1;
	private static final String TAG = ManControllerMenu.class.getSimpleName();
	
	private ManControllerMenu()
	{
	}
	
	public static int getMaxRowsInNames()
	{
		return maxRowsInNames;
	}
	
	public static ArrayList<Names> readNames(Context context) 
	{
		ArrayList<Names> list = null;
		try 
		{
			DatabaseOpenHelperMenu dbhelper = new DatabaseOpenHelperMenu(context);
			SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
			String[] columnsToTake = { BaseColumns._ID, NamesColumns._TITLE_FONT, NamesColumns._NEWS_FONT, NamesColumns._CHANNAL_LIST_FONT };
			Cursor cursor = sqliteDB.query(Names.TABLE_NAME, columnsToTake, null, null, null, null, null);
			
			if (cursor.moveToFirst())
			{
				list = new ArrayList<Names>();
			}
			
			while (cursor.moveToNext())
			{
				Names oneRow = new Names();
				oneRow.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
				oneRow.set_title_font(cursor.getString(cursor.getColumnIndexOrThrow(NamesColumns._TITLE_FONT)));
				oneRow.set_news_font(cursor.getString(cursor.getColumnIndexOrThrow(NamesColumns._NEWS_FONT)));
				oneRow.set_channel_list_font(cursor.getString(cursor.getColumnIndexOrThrow(NamesColumns._CHANNAL_LIST_FONT)));
				list.add(oneRow);
			}
			cursor.close();
			dbhelper.close();
		} 
		catch (Exception e)
		{
			Log.e(TAG, "Failed to select Names.", e);
		}
		return list;
	}
	
	public static void setMaxRowsInNames(int maxRowsInNames) 
	{
		ManControllerMenu.maxRowsInNames = maxRowsInNames;
	}
	
	public static void update_title_font(Context context, String comment, long l) 
	{
		try 
		{
			DatabaseOpenHelperMenu dbhelper = new DatabaseOpenHelperMenu(context);
			SQLiteDatabase sqliteDB = dbhelper.getWritableDatabase();
			String quer = null;
			int countRows = -1;
			Cursor cursor = sqliteDB.query(Names.TABLE_NAME, new String[] { "count(*)" }, null, null, null, null, null);
			
			if (cursor.moveToFirst()) 
			{
				countRows = cursor.getInt(0);
				if (LOGV) 
				{
					Log.v(TAG, "Count in Names table" + String.valueOf(countRows));
				}
			}
			cursor.close();
			quer = String.format("UPDATE " + Names.TABLE_NAME + " SET " + Names.NamesColumns._TITLE_FONT	+ " = '" + comment + "' WHERE " + BaseColumns._ID + " = " + l);
			Log.d("", "" + quer);
			sqliteDB.execSQL(quer);
			sqliteDB.close();
			dbhelper.close();
		} 
		catch (SQLiteException e)
		{
			Log.e(TAG, "Failed open database. ", e);
		} 
		catch (SQLException e) 
		{
			Log.e(TAG, "Failed to update Names. ", e);
		}
	}

	public static void update_news_font(Context context, String comment, long l) 
	{
		try 
		{
			DatabaseOpenHelperMenu dbhelper = new DatabaseOpenHelperMenu(context);
			SQLiteDatabase sqliteDB = dbhelper.getWritableDatabase();
			String quer = null;
			int countRows = -1;
			Cursor cursor = sqliteDB.query(Names.TABLE_NAME, new String[] { "count(*)" }, null, null, null, null, null);
			
			if (cursor.moveToFirst()) 
			{
				countRows = cursor.getInt(0);
				if (LOGV) 
				{
					Log.v(TAG, "Count in Names table" + String.valueOf(countRows));
				}
			}
			cursor.close();
			quer = String.format("UPDATE " + Names.TABLE_NAME + " SET " + Names.NamesColumns._NEWS_FONT	+ " = '" + comment + "' WHERE " + BaseColumns._ID + " = " + l);
			Log.d("", "" + quer);
			sqliteDB.execSQL(quer);
			sqliteDB.close();
			dbhelper.close();
		} 
		catch (SQLiteException e)
		{
			Log.e(TAG, "Failed open database. ", e);
		} 
		catch (SQLException e) 
		{
			Log.e(TAG, "Failed to update Names. ", e);
		}
	}

	public static void update_channel_list_font(Context context, String comment, long l) 
	{
		try 
		{
			DatabaseOpenHelperMenu dbhelper = new DatabaseOpenHelperMenu(context);
			SQLiteDatabase sqliteDB = dbhelper.getWritableDatabase();
			String quer = null;
			int countRows = -1;
			Cursor cursor = sqliteDB.query(Names.TABLE_NAME, new String[] { "count(*)" }, null, null, null, null, null);
			
			if (cursor.moveToFirst()) 
			{
				countRows = cursor.getInt(0);
				if (LOGV) 
				{
					Log.v(TAG, "Count in Names table" + String.valueOf(countRows));
				}
			}
			cursor.close();
			quer = String.format("UPDATE " + Names.TABLE_NAME + " SET " + Names.NamesColumns._CHANNAL_LIST_FONT	+ " = '" + comment + "' WHERE " + BaseColumns._ID + " = " + l);
			Log.d("", "" + quer);
			sqliteDB.execSQL(quer);
			sqliteDB.close();
			dbhelper.close();
		} 
		catch (SQLiteException e)
		{
			Log.e(TAG, "Failed open database. ", e);
		} 
		catch (SQLException e) 
		{
			Log.e(TAG, "Failed to update Names. ", e);
		}
	}

	public static void write(Context context, String title_font, String news_font, String channel_list_font)
	{
		try 
		{
			//создали нашу базу и открыли для записи
			DatabaseOpenHelperMenu dbhelper = new DatabaseOpenHelperMenu(context);
			SQLiteDatabase sqliteDB = dbhelper.getWritableDatabase();
			String quer = null;
			int countRows = -1;
			//Открыли курсор для записи
			Cursor cursor = sqliteDB.query(Names.TABLE_NAME, new String[] { "count(*)" }, null, null, null, null, null);	
			
			if (cursor.moveToFirst()) 
			{
				countRows = cursor.getInt(0);				
				if (LOGV)
				{
					Log.v(TAG, "Count in Names table" + String.valueOf(countRows));
				}
			}
			if ((maxRowsInNames == -1) || (maxRowsInNames >= countRows)) 
			{
				countRows = cursor.getInt(0);
				//дальще наш запрос в базу для записи полученных дынных из функции
				quer = String.format("INSERT INTO %s (%s, %s, %s) VALUES (%s, %s, %s);",
						// таблица
						Names.TABLE_NAME,
						// колонки
						Names.NamesColumns._NEWS_FONT,
						Names.NamesColumns._TITLE_FONT,
						Names.NamesColumns._CHANNAL_LIST_FONT,
						// поля
						title_font,
						news_font,
						channel_list_font
						);
			}	
			sqliteDB.execSQL(quer);
			//закрыли всю базу
			cursor.close();
			sqliteDB.close();
			dbhelper.close();
		} 
		catch (SQLiteException e) 
		{
			Log.e(TAG, "Failed open rimes database. ", e);
		} 
		catch (SQLException e) 
		{
			Log.e(TAG, "Failed to insert Names. ", e);
		}
	}
}
