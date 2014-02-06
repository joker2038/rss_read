package database;

import android.provider.BaseColumns;

public class DatabaseContract 
{
	/** Describes History Table and model. */
	public static class Names 
	{
		/** Default "ORDER BY" clause. */
		//��������� �� name � ��������� �������
		public static final String DEFAULT_SORT = NamesColumns.NAME + " DESC";
		//��� �������
		public static final String TABLE_NAME = "feedList";
		//���� URL
		private String url;
		//��� ��������
		private long id;
		//���� ���
		private String name;
		
        // ���� ���� ������ � ������ ��� ������� ������ �� ����
		
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
		
		//����� � ������� ����� ����� � ����
		public class NamesColumns implements BaseColumns 
		{
			public static final String URL = "url";
			public static final String NAME = "name";
		}
	}
}
