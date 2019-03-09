/*
 * Copyright (C) 2011 Mats Hofman <http://matshofman.nl/contact/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package world.ouer.rss.dao;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * this table store the node info in RSS xml files.
 */
@Entity(nameInDb = "RSS")
public class RssItem implements Parcelable {

	@Id(autoincrement = true)
	Long id;
	String title;
	/**
	 * the link to html page
	 */
	String link;
	String pubDate;
	String description;
	String content;
	String channel;
	/**
	 * the link to attachment
	 */
	String enclosure;
	String guid;

	public RssItem() {
		
	}
	
	public RssItem(Parcel source) {

		Bundle data = source.readBundle();
		title = data.getString("title");
		link = data.getString("link");
		pubDate =data.getString("pubDate");
		description = data.getString("description");
		content = data.getString("content");
		enclosure = data.getString("enclosure");
		guid= data.getString("guid");

	}

	@Generated(hash = 1950487654)
	public RssItem(Long id, String title, String link, String pubDate, String description,
									String content, String channel, String enclosure, String guid) {
					this.id = id;
					this.title = title;
					this.link = link;
					this.pubDate = pubDate;
					this.description = description;
					this.content = content;
					this.channel = channel;
					this.enclosure = enclosure;
					this.guid = guid;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		Bundle data = new Bundle();
		data.putString("title", title);
		data.putString("link", link);
		data.putString("pubDate", pubDate);
		data.putString("description", description);
		data.putString("content", content);
		data.putString("enclosure", enclosure);
		data.putString("guid", guid);

		dest.writeBundle(data);
	}
	
	public static final Creator<RssItem> CREATOR = new Creator<RssItem>() {
		public RssItem createFromParcel(Parcel data) {
			return new RssItem(data);
		}
		public RssItem[] newArray(int size) {
			return new RssItem[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setPubDate(String pubDate) {
	    this.pubDate=pubDate;
//		try {
//			Log.i("PUBDATE", "o: "+pubDate);
//			pubDate=pubDate.replaceAll("[A-Z]{3}$","-0000");
//			Log.i("PUBDATE", "r1: "+pubDate);
//		    pubDate=pubDate.replaceAll("-[0-9]{4}$","");
//			Log.i("PUBDATE", "r2: "+pubDate);
//			SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
//			this.pubDate = dateFormat.parse(pubDate);
//		} catch (ParseException e) {
//			Log.e("RSSITEM", "setPubDate: "+pubDate );
//			e.printStackTrace();
//		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

//	@Override
//	public int compareTo(RssItem another) {
//		if(getPubDate() != null && another.getPubDate() != null) {
//			return getPubDate().compareTo(another.getPubDate());
//		} else {
//			return 0;
//		}
//	}

	public Long getId() {
					return this.id;
	}

	public void setId(Long id) {
					this.id = id;
	}

	public String getChannel() {
					return this.channel;
	}

	public void setChannel(String channel) {
					this.channel = channel;
	}

	public String getEnclosure() {
					return this.enclosure;
	}

	public void setEnclosure(String enclosure) {
					this.enclosure = enclosure;
	}

	public String getGuid() {
					return this.guid;
	}

	public void setGuid(String guid) {
					this.guid = guid;
	}

	public String getPubDate() {
					return this.pubDate;
	}


}
