package world.ouer.rss;
import android.os.AsyncTask;
import java.net.URL;

public class RssAsyncService extends AsyncTask<URL,Integer,String> {

    public RssAsyncService() {

    }
    @Override
    protected String doInBackground(URL... urls) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }


}
