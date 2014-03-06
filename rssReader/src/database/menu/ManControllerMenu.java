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
			String[] columnsToTake = { BaseColumns._ID, NamesColumns._TITLE_FONT, NamesColumns._NEWS_FONT, NamesColumns._CHANNAL_LIST_FONT, NamesColumns._STORAGE_TIME };
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
				oneRow.set_channel_list_font(cursor.getString(cursor.getColumnIndexOrThrow(NamesColumns._STORAGE_TIME)));
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
	
	public static void update(Context context, String NamesColumns, String comment, long l) 
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
			quer = String.format("UPDATE " + Names.TABLE_NAME + " SET " + NamesColumns	+ " = '" + comment + "' WHERE " + BaseColumns._ID + " = " + l);
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
}
