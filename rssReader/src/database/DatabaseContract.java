package database;

import android.provider.BaseColumns;

public class DatabaseContract 
{
	/** Describes History Table and model. */
	public static class Names 
	{
		/** Default "ORDER BY" clause. */
		//сортируем по name в убывающем порядке
		public static final String DEFAULT_SORT = NamesColumns.NAME + " DESC";
		//имя таблицы
		public static final String TABLE_NAME = "feedList";
		//поле URL
		private String url;
		//наш айдишник
		private long id;
		//наше имя
		private String name;
		
        // Ниже идут сетеры и гетеры для захвата данных из базы
		
		public String getURL() 
		{
			return url;
		}

		public long getId()
		{
			return id;
		}	
		
		public String getName()
		{
			return name;
		}

		public void setURL(String url) 
		{
			this.url = url;
		}

		public void setId(long id) 
		{
			this.id = id;
		}
		
		public void setName(String name) 
		{
			this.name = name;
		}
		
		//Класс с именами наших полей в базе
		public class NamesColumns implements BaseColumns 
		{
			public static final String URL = "url";
			public static final String NAME = "name";
		}
	}
}
