package database;


import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.BaseColumns;
import android.util.Log;
import database.DatabaseContract.Names;
import database.DatabaseContract.Names.NamesColumns;

public class ManController {

	private static final boolean LOGV = false;
	private static int maxRowsInNames = -1;
	private static final String TAG = ManController.class.getSimpleName();

	private ManController() {

	}

	public static int getMaxRowsInNames() {

		return maxRowsInNames;
	}
	
	public static ArrayList<Names> readNames(Context context) 
	{
		ArrayList<Names> list = null;
		try 
		{
			DatabaseOpenHelper dbhelper = new DatabaseOpenHelper(context);
			SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
			String[] columnsToTake = { BaseColumns._ID, NamesColumns.URL, NamesColumns.NAME };
			Cursor cursor = sqliteDB.query(Names.TABLE_NAME, columnsToTake, null, null, null, null, Names.DEFAULT_SORT);
			
			if (cursor.moveToFirst())
			{
				list = new ArrayList<Names>();
			}
			
			while (cursor.moveToNext())
			{
				Names oneRow = new Names();
				oneRow.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
				oneRow.setURL(cursor.getString(cursor.getColumnIndexOrThrow(NamesColumns.URL)));
				oneRow.setName(cursor.getString(cursor.getColumnIndexOrThrow(NamesColumns.NAME)));
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
		ManController.maxRowsInNames = maxRowsInNames;
	}

	public static void update(Context context, String comment, long l) 
	{
		try 
		{
			DatabaseOpenHelper dbhelper = new DatabaseOpenHelper(context);
			SQLiteDatabase sqliteDB = dbhelper.getWritableDatabase();
			String quer = null;
			int countRows = -1;
			Cursor cursor = sqliteDB.query(Names.TABLE_NAME, new String[] { "count(*)" }, null, null, null, null, Names.DEFAULT_SORT);
			
			if (cursor.moveToFirst()) 
			{
				countRows = cursor.getInt(0);
				if (LOGV) 
				{
					Log.v(TAG, "Count in Names table" + String.valueOf(countRows));
				}
			}
			cursor.close();
			quer = String.format("UPDATE " + Names.TABLE_NAME + " SET " + Names.NamesColumns.URL	+ " = '" + comment + "' WHERE " + BaseColumns._ID + " = " + l);
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
	
	public static void delete(Context context, long l) 
	{
			DatabaseOpenHelper dbhelper = new DatabaseOpenHelper(context);
			SQLiteDatabase sqliteDB = dbhelper.getWritableDatabase();
			sqliteDB.delete(Names.TABLE_NAME, BaseColumns._ID  + " = " + l, null);
			sqliteDB.close();
			dbhelper.close();
	}
	
	public static void write(Context context, String urlLinkEdit, String nameEdit)
	{
		try 
		{
			//создали нашу базу и открыли для записи
			DatabaseOpenHelper dbhelper = new DatabaseOpenHelper(context);
			SQLiteDatabase sqliteDB = dbhelper.getWritableDatabase();
			String quer = null;
			int countRows = -1;
			//Открыли курсор для записи
			Cursor cursor = sqliteDB.query(Names.TABLE_NAME, new String[] { "count(*)" }, null, null, null, null, Names.DEFAULT_SORT);
			
			if (cursor.moveToFirst()) 
			{
				countRows = cursor.getInt(0);
				if (LOGV)
				{
					Log.v(TAG, "Count in Names table" + String.valueOf(countRows));
				}
			}
			cursor.close();
			if ((maxRowsInNames == -1) || (maxRowsInNames >= countRows)) 
			{
				//дальще наш запрос в базу для записи полученных дынных из функции
				quer = String.format("INSERT INTO %s (%s, %s) VALUES (%s, %s);",
						// таблица
						Names.TABLE_NAME,
						// колонки
						Names.NamesColumns.URL,
						Names.NamesColumns.NAME,
						// поля
						urlLinkEdit,
						nameEdit);
			}
			//закрыли всю базу
			sqliteDB.execSQL(quer);
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