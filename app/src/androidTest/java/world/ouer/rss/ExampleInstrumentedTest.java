package world.ouer.rss;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("world.ouer.rss", appContext.getPackageName());
    }

    @Test
    public void testRss() throws Exception{

        URL url = new URL("http://rss.cnn.com/services/podcasting/studentnews/rss.xml");
        RssFeed feed = RssReader.read(url);

        ArrayList<RssItem> rssItems = feed.getRssItems();
        for(RssItem rssItem : rssItems) {
            Log.i("RSS Reader", rssItem.getTitle());
        }

    }
}
