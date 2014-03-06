package database.feed;


import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.BaseColumns;
import android.util.Log;
import database.feed.DatabaseContractFeed.Names;
import database.feed.DatabaseContractFeed.Names.NamesColumns;

public class ManControllerFeed {

	private static final boolean LOGV = false;
	private static int maxRowsInNames = -1;
	private static final String TAG = ManControllerFeed.class.getSimpleName();

	private ManControllerFeed() 
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
			DatabaseOpenHelperFeed dbhelper = new DatabaseOpenHelperFeed(context);
			SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
			String[] columnsToTake = { BaseColumns._ID, NamesColumns.TITLE, NamesColumns.DESCRIPTION, NamesColumns.LINK, NamesColumns.PUPDATE };
			Cursor cursor = sqliteDB.query(Names.TABLE_NAME, columnsToTake, null, null, null, null, Names.DEFAULT_SORT);
			//sqliteDB.execSQL("SELECT * FROM feed ORDER BY _id DESC");
			
			if (cursor.moveToFirst())
			{
				list = new ArrayList<Names>();
			}
			
			while (cursor.moveToNext())
			{
				Names oneRow = new Names();
				oneRow.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
				oneRow.setTITLE(cursor.getString(cursor.getColumnIndexOrThrow(NamesColumns.TITLE)));
				oneRow.setDESCRIPTION(cursor.getString(cursor.getColumnIndexOrThrow(NamesColumns.DESCRIPTION)));
				oneRow.setLINK(cursor.getString(cursor.getColumnIndexOrThrow(NamesColumns.LINK)));
				oneRow.setPUPDATE(cursor.getString(cursor.getColumnIndexOrThrow(NamesColumns.PUPDATE)));
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
		ManControllerFeed.maxRowsInNames = maxRowsInNames;
	}
	
	public static void update(Context context, String comment, long l) 
	{
		try 
		{
			DatabaseOpenHelperFeed dbhelper = new DatabaseOpenHelperFeed(context);
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
			if (comment.equals(" "))
			{
				quer = String.format("UPDATE " + Names.TABLE_NAME + " SET " + Names.NamesColumns.FAVORITES	+ " = '" + comment + "' WHERE " + BaseColumns._ID + " = " + l);
			}
			else if (comment.equals("favorites"))
			{
				quer = String.format("UPDATE " + Names.TABLE_NAME + " SET " + Names.NamesColumns.FAVORITES	+ " = '" + comment + "' WHERE " + BaseColumns._ID + " = " + l);
			}
			else if (comment.equals("read"))
			{
				quer = String.format("UPDATE " + Names.TABLE_NAME + " SET " + Names.NamesColumns.READ	+ " = '" + comment + "' WHERE " + BaseColumns._ID + " = " + l);
			}
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
	
	public static void write(Context context, String title, String description, String pubDate, String link, String read, String str)
	{
		try 
		{
			//создали нашу базу и открыли для записи
			DatabaseOpenHelperFeed dbhelper = new DatabaseOpenHelperFeed(context);
			SQLiteDatabase sqliteDB = dbhelper.getWritableDatabase();
			String quer = null;
			boolean temp = false;
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
			long i1 = Long.parseLong(str);
			long i2 = Long.parseLong(pubDate);
			title = title.replace("\"", "");
			
			description = description.replace("\"", "");
			description = description.replace("<br>", "\r\n\t");
			description = description.replace("<p>", "\r\n\t");
			description = description.replace("<br />", "\r\n\t");

			description = description.replaceAll("<.*>", "");
			description = description.replaceAll("&quot;", "");
			description = description.replaceAll("&\\S*;", "");
			if (i1 < i2)
			{
				if ((maxRowsInNames == -1) || (maxRowsInNames >= countRows)) 
				{
					countRows = cursor.getInt(0);
					//дальще наш запрос в базу для записи полученных дынных из функции
					quer = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (%s, %s, %s, %s, %s);",
							// таблица
							Names.TABLE_NAME,
							// колонки
							Names.NamesColumns.TITLE,
							Names.NamesColumns.DESCRIPTION,
							Names.NamesColumns.PUPDATE,
							Names.NamesColumns.LINK,
							Names.NamesColumns.READ,
							// поля
							"\"" + title + "\"",
							"\"" + description + "\"",
							pubDate,
							link,
							read);
					temp = true;
				}		
			}
			if (temp == true)
			{
				sqliteDB.execSQL(quer);
			}
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

	public static void delete(Context context, long l) 
	{
			DatabaseOpenHelperFeed dbhelper = new DatabaseOpenHelperFeed(context);
			SQLiteDatabase sqliteDB = dbhelper.getWritableDatabase();
			sqliteDB.delete(Names.TABLE_NAME, Names.NamesColumns.PUPDATE  + " <= " + l, null);
			sqliteDB.close();
			dbhelper.close();
	}
}