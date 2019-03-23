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

package world.ouer.rss;

import android.text.TextUtils;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import world.ouer.rss.dao.RssItem;

public class RssHandler extends DefaultHandler {
    private static final String TAG = "RssHandler";
    private RssFeed rssFeed;
    private RssItem rssItem;
    private StringBuilder stringBuilder;
    private PositionInXml where = PositionInXml.POSITION_IN_CHANNEL;

    @Override
    public void startDocument() {
        rssFeed = new RssFeed();
        stringBuilder = new StringBuilder();
    }

    /**
     * Return the parsed RssFeed with it's RssItems
     *
     * @return
     */
    public RssFeed getResult() {
        return rssFeed;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
//        Log.i(TAG, String.format("startEle %s\t%s\t%s",uri,localName,qName));
        if (localName.equalsIgnoreCase("item")) {
            where = PositionInXml.POSITION_IN_ITEM;
        }

        if (where == PositionInXml.POSITION_IN_ITEM) {
            if (localName.equalsIgnoreCase("item")) {
                rssItem = new RssItem();
                rssFeed.addRssItem(rssItem);
            }
            if(attributes!=null && qName.equalsIgnoreCase("enclosure")){
                int index=attributes.getIndex("url");
                if(index!=-1){
                    String value =attributes.getValue(index);
                    rssItem.setEnclosure(value);
                }
            }
        }


    }

    @Override
    public void characters(char[] ch, int start, int length) {
        stringBuilder.append(ch, start, length);
//        Log.i("CHAR_U", ">>>>_: "+stringBuilder.toString());
//        Log.i("CHAR_D","<<<<:"+String.valueOf(ch,start,length));

    }

    @Override
    public void endElement(String uri, String localName, String qName) {
//        Log.i(TAG, String.format("endEle  %s\t%s\t%s",uri,localName,qName));
        if (localName.equalsIgnoreCase("item")) {
            where = PositionInXml.POSITION_IN_CHANNEL;
        }

        String methodName = "set" + localName.substring(0, 1).toUpperCase() + localName.substring(1);
        Method method = null;
        try {

            switch (where) {
                case POSITION_IN_ITEM:
                    method = rssItem.getClass().getMethod(methodName, String.class);
                    String value =stringBuilder.toString().trim();
                    if (!TextUtils.isEmpty(value)) {
                        method.invoke(rssItem, value);
                    }
                    rssItem.setChannel(rssFeed.getTitle());
                    break;
                case POSITION_IN_CHANNEL:
                    method = rssFeed.getClass().getMethod(methodName, String.class);
                    method.invoke(rssFeed, stringBuilder.toString().trim());
                    break;
            }
        } catch (NoSuchMethodException e) {
            Log.w(TAG, "endElement: " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        stringBuilder.setLength(0);
    }

    private enum PositionInXml {
        /**
         * below channel node and above item node.
         */
        POSITION_IN_CHANNEL,
        /**
         * in item node.
         */
        POSITION_IN_ITEM;

    }

}
