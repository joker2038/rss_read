package rssfeed;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class RssItem {

	private String title;
	private String description;
	private Date pubDate;
	private String link;

	public RssItem(String title, String description, Date pubDate, String link) 
	{
		this.title = title;
		this.description = description;
		this.pubDate = pubDate;
		this.link = link;
	}

	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title) 
	{
		this.title = title;
	}
	public String getDescription() 
	{
		return description;
	}
	public void setDescription(String description) 
	{
		this.description = description;
	}
	public Date getPubDate() 
	{
		return pubDate;
	}
	public void setPubDate(Date pubDate) 
	{
		this.pubDate = pubDate;
	}
	public String getLink() 
	{
		return link;
	}
	public void setLink(String link) 
	{
		this.link = link;
	}
	

	@Override
	public String toString() 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy/ hh:mm:ss");
		String result = getTitle() + "   ( " + sdf.format(this.getPubDate()) + " )";
		return result;
	}

	public static ArrayList<RssItem> getRssItems(String feedUrl) 
	{
		ArrayList<RssItem> rssItems = new ArrayList<RssItem>();
		try 
		{
			//open an URL connection make GET to the server and 
			//take xml RSS data
			URL url = new URL(feedUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) 
			{
				InputStream is = conn.getInputStream();
				
				//DocumentBuilderFactory, DocumentBuilder are used for 
				//xml parsing
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();

				//using db (Document Builder) parse xml data and assign
				//it to Element
				Document document = db.parse(is);
				Element element = document.getDocumentElement();

				//take rss nodes to NodeList
				NodeList nodeList = element.getElementsByTagName("item");

				if (nodeList.getLength() > 0) 
				{
					for (int i = 0; i < nodeList.getLength(); i++) 
					{						
						//take each entry (corresponds to <item></item> tags in 
						//xml data
						
						Element entry = (Element) nodeList.item(i);
						
						Element _titleE = (Element) entry.getElementsByTagName("title").item(0);
						Element _descriptionE = (Element) entry.getElementsByTagName("description").item(0);
						Element _pubDateE = (Element) entry.getElementsByTagName("pubDate").item(0);
						Element _linkE = (Element) entry.getElementsByTagName("link").item(0);

						String _title = _titleE.getFirstChild().getNodeValue();
						String _description = _descriptionE.getFirstChild().getNodeValue();
						@SuppressWarnings("deprecation")
						Date _pubDate = new Date(_pubDateE.getFirstChild().getNodeValue());
						String _link = _linkE.getFirstChild().getNodeValue();

						//create RssItemObject and add it to the ArrayList
						RssItem rssItem = new RssItem(_title, _description,	_pubDate, _link);
						//ManControllerFeed.write(getBaseContext(), _title,  _description, _pubDate, _link);
						//ManControllerFeed.write(null, _title, _description, _pubDate.toString(), _link);

						rssItems.add(rssItem);
					}
				}
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return rssItems;
	}
}
