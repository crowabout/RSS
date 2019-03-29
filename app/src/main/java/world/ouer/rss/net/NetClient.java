package world.ouer.rss.net;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pc on 2019/3/8.
 */

public class NetClient {

    private OkHttpClient okClient;
    private static NetClient mClient;
    private NetClient() {
        okClient=new OkHttpClient();
    }

    public static NetClient newInstance(){

        if(mClient==null){
            mClient=new NetClient();
        }
        return mClient;
    }

    public void asynRun(String url, Callback callback) throws IOException{
        Request request =new Request.Builder()
                .url(url)
                .build();
        okClient.newCall(request).enqueue(callback);
    }


    public Response synRun(String url) throws IOException{
        Request request =new Request.Builder()
                .url(url)
                .build();
        return okClient.newCall(request).execute();
    }




}
