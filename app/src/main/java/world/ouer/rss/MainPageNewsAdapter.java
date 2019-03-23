package world.ouer.rss;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import world.ouer.rss.dao.RssItem;

/**
 * Created by pc on 2019/3/22.
 */

public class MainPageNewsAdapter extends AbsRssAdapter<RssItem> implements AbsRssAdapter.IRssViewHolder{

    private static final String TAG ="MainPageNewsAdapter" ;

    public MainPageNewsAdapter(Context mCtx, List<RssItem> source) {
        super(mCtx, source);
        setIRssViewHolder(this);
    }

    @Override
    public int childItemViewId() {
        return R.layout.main_page_news_adapter;
    }

    @Override
    public void bindTxtToView(Object o, ViewHolder holder) {
        RssItem item = (RssItem) o;
        Log.d(TAG, String.format("_id:%d",item.getId()));
        NewsViewHolder newsHolder = (NewsViewHolder) holder;
        newsHolder.title.setText(item.getTitle());
        newsHolder.desc.setText(RssUtils.dropHtmlTagFromStr(item.getDescription()));
        newsHolder.state.setText(stat(item));
    }

    @Override
    public ViewHolder creatViewHolder(View itemView) {
        return new NewsViewHolder(itemView);
    }

    class NewsViewHolder extends  ViewHolder{
        TextView title;
        TextView state;
        TextView desc;
        public NewsViewHolder(View itemView) {
            super(itemView);
            title =itemView.findViewById(R.id.title);
            state =itemView.findViewById(R.id.state);
            desc = itemView.findViewById(R.id.desc);
        }
    }

    public void update(List<RssItem> newData){
        source.addAll(newData);
        notifyDataSetChanged();
    }

    // 是否下载|是否阅读|(mp3|mp4|txt)|日期|channel
    private String stat(RssItem item){

        return String.format("%b|%b|%s|%s|%s",
                item.getIsDownloaded(),
                item.getIsRead(),
                RssUtils.guessTypeFromUrl(item.getEnclosure())
               ,RssUtils.splitTime(item.getPubDate(),null)
                ,RssUtils.channelSimplify(item.getChannel())
                );

    }
}
