package database.menu;

import android.provider.BaseColumns;

public class DatabaseContractMenu 
{
	/** Describes History Table and model. */
	public static class Names
	{
		//им€ таблицы
		public static String TABLE_NAME = "settings";
				
		private String title_font;
		private String news_font;
		private String channel_list_font;
		private String storage_time;
		private long id;		
		
        // Ќиже идут сетеры и гетеры дл€ захвата данных из базы
		
		public String get_title_font() 
		{
			return title_font;
		}
		
		public String get_news_font() 
		{
			return news_font;
		}
		
		public String get_channel_list_font() 
		{
			return channel_list_font;
		}
		
		public String get_storage_time() 
		{
			return storage_time;
		}

		public long getId()
		{
			return id;
		}		

		public void set_title_font(String title_font) 
		{
			this.title_font = title_font;
		}
		
		public void set_news_font(String news_font) 
		{
			this.news_font = news_font;
		}
		
		public void set_channel_list_font(String channel_list_font) 
		{
			this.channel_list_font = channel_list_font;
		}
		
		public void set_storage_time(String storage_time) 
		{
			this.storage_time = storage_time;
		}


		public void setId(long id) 
		{
			this.id = id;
		}
		
		// ласс с именами наших полей в базе
		public class NamesColumns implements BaseColumns 
		{
			public static final String _TITLE_FONT = "title_font";
			public static final String _NEWS_FONT = "news_font";
			public static final String _CHANNAL_LIST_FONT = "channel_list_font";
			public static final String _STORAGE_TIME = "storage_time";
			public static final String _ID = "_id";
		}
	}
}
