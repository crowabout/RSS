package world.ouer.rss.adapter;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import world.ouer.rss.R;
import world.ouer.rss.dao.SourceItem;

/**
 * Created by pc on 2019/3/7.
 */
public class SideSubscribeSourceAdapter extends AbsRssAdapter<SourceItem>
        implements AbsRssAdapter.IRssViewHolder{

    public SideSubscribeSourceAdapter(Context mCtx, List<SourceItem> source) {
        super(mCtx, source);
        setIRssViewHolder(this);
    }
    @Override
    public int childItemViewId() {
        return R.layout.side_subscribe_source_item;
    }

    @Override
    public void bindTxtToView(Object o,ViewHolder holder) {
        final SourceItem item = (SourceItem) o;
        SideSubscribeViewHolder childHolder = (SideSubscribeViewHolder) holder;
        childHolder.tv1.setText(item.getChannel());
        childHolder.tv2.setText(String.valueOf(item.getLastTimeAccess()));
        childHolder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onUpdateIconClicked(item,v);
            }
        });
    }

    @Override
    public ViewHolder creatViewHolder(View itemView) {
        return new SideSubscribeViewHolder(itemView);
    }

     class SideSubscribeViewHolder  extends ViewHolder {
        TextView tv1;
        TextView tv2;
        ImageView iv;
        public SideSubscribeViewHolder(View itemView) {
            super(itemView);
            tv1 =itemView.findViewById(R.id.side_subscribe_source_tv1);
            tv2 =itemView.findViewById(R.id.side_subscribe_source_tv2);
            iv=itemView.findViewById(R.id.side_subscribe_source_iv);

        }
    }

}
