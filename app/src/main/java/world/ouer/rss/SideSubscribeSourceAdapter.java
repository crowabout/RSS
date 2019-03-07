package world.ouer.rss;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import world.ouer.rss.Channel.SubscribeMeta;

/**
 * Created by pc on 2019/3/7.
 */

public class SideSubscribeSourceAdapter extends AbsRssAdapter<SubscribeMeta>
        implements AbsRssAdapter.IRssViewHolder{

    public SideSubscribeSourceAdapter(Context mCtx, List<SubscribeMeta> source) {
        super(mCtx, source);
        setIRssViewHolder(this);
    }

    @Override
    public int childItemViewId() {
        return R.layout.side_subscribe_source_item;
    }

    @Override
    public void bindTxtToView(Object o,ViewHolder holder) {
        SubscribeMeta item = (SubscribeMeta) o;
        SideSubscribeViewHolder childHolder = (SideSubscribeViewHolder) holder;
        childHolder.tv1.setText(item.title);
        childHolder.tv2.setText(item.updateItems);

    }

    @Override
    public ViewHolder creatViewHolder(View itemView) {
        return new SideSubscribeViewHolder(itemView);
    }

     class SideSubscribeViewHolder  extends ViewHolder {
        TextView tv1;
        TextView tv2;
        public SideSubscribeViewHolder(View itemView) {
            super(itemView);
            tv1 =itemView.findViewById(R.id.side_subscribe_source_tv1);
            tv2 =itemView.findViewById(R.id.side_subscribe_source_tv2);
        }
    }

}
