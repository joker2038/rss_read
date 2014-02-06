package database.feed;

import com.example.rssreader.ListActivity;

import database.DatabaseContract.Names.NamesColumns;

import android.provider.BaseColumns;

public class DatabaseContractFeed {

	/** Describes History Table and model. */
	public static class Names {

		/** Default "ORDER BY" clause. */
		//сортируем в убывающем порядке
		public static final String DEFAULT_SORT = NamesColumns.PUPDATE + " DESC";
		//имя таблицы
		public static String TABLE_NAME = "feed";
				
		private String title;
		private String description;
		private String pubDate;
		private String link;
		private long id;		
		
        // Ниже идут сетеры и гетеры для захвата данных из базы
		
		public String getTITLE() 
		{
			return title;
		}
		
		public String getDESCRIPTION() 
		{
			return description;
		}
		
		public String getPUPDATE() 
		{
			return pubDate;
		}
		
		public String getLINK() 
		{
			return link;
		}

		public long getId()
		{
			return id;
		}		

		public void setDESCRIPTION(String description) 
		{
			this.description = description;
		}
		
		public void setTITLE(String title) 
		{
			this.title = title;
		}
		
		public void setPUPDATE(String pubDate) 
		{
			this.pubDate = pubDate;
		}
		
		public void setLINK(String link) 
		{
			this.link = link;
		}

		public void setId(long id) 
		{
			this.id = id;
		}
		
		//Класс с именами наших полей в базе
		public class NamesColumns implements BaseColumns 
		{
			public static final String TITLE = "title";
			public static final String DESCRIPTION = "description";
			public static final String PUPDATE = "pub_date";
			public static final String LINK = "link";
			public static final String READ = "read";
			public static final String _ID = "_id";
		}
	}
}
