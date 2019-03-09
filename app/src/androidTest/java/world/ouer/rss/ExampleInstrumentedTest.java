package world.ouer.rss;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import world.ouer.rss.channel.IEmbedRss;
import world.ouer.rss.dao.RssItem;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = "ExampleInstrumentedTest";

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("world.ouer.rss", appContext.getPackageName());
    }

    @Test
    public void testRss() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        try {
            RssFeed feed = RssReader.read(new URL(IEmbedRss.RSS_NPR));
            ArrayList<RssItem> rssItems = feed.getRssItems();
            for (RssItem rssItem : rssItems) {
                Log.i("RSS Reader", rssItem.getTitle());
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        Response response = null;
//        try {
//            response = NetClient.newInstance().synRun(IEmbedRss.RSS_NPR);
//            InputStream in = response.body().byteStream();
//            RssHandler handler = new RssHandler();
//            XMLReader read = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
//            read.setContentHandler(handler);
//            read.parse(new InputSource(in));
//            int size = handler.getResult().getRssItems().size();
//            Log.d(TAG, "onResponse: " + size);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        }

    }


    @Test
    public void testParseDate() throws Exception {

        String pubDate = "Tue, 05 Mar 2019 05:00:00 -0500";
        String r2 = "Tue, 05 Mar 2019 05:00:00 ";
        Log.i("PUBDATE", "o: " + pubDate);
//        pubDate.replaceAll("[A-Z]{3}$", "-0000");


        Log.i("PUBDATE", "r1: " + pubDate);
//			boolean match =Pattern.matches("^[A-Z][a-z]{2},\\s[0-9]{2}\\s[a-zA-Z]{2}\\s[0-9]{4}\\s[0-9]*\\-[0-9]{4}",pubDate);
//		    if(match){
        pubDate =pubDate.replaceAll("-[0-9]{4}", "");

//        Matcher match = Pattern.compile("-[0-9]{4}",Pattern.DOTALL|Pattern.CASE_INSENSITIVE).matcher(pubDate);
//        int start =match.start();
//        int end =match.end();
//        String s=match.group();
//
//        boolean b =match.find();

//        Pattern p = Pattern.compile("cat");
//        Matcher m = p.matcher("one cat two cats in the yard");
//        StringBuffer sb = new StringBuffer();
//        while (m.find()) {
//            m.appendReplacement(sb, "dog");
//        }
////        m.appendTail(sb);
//        System.out.println(sb.toString());
//        assertEquals(sb.toString(),"abc");

        assertEquals(pubDate,r2);

    }

}
