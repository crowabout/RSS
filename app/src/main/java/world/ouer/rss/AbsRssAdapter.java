package world.ouer.rss;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by pc on 2019/3/7.
 */

public abstract class AbsRssAdapter<T>  extends RecyclerView.Adapter<AbsRssAdapter.ViewHolder>
    {

     Context mCtx;
     List<T> source;
    private IRssViewHolder mRssHolder;
    public AbsRssAdapter(Context mCtx, List<T> source) {
        this.mCtx=mCtx;
        this.source=source;
    }

    public void setIRssViewHolder(IRssViewHolder holder){
        this.mRssHolder=holder;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int childItemId=mRssHolder.childItemViewId();
       View view = View.inflate(mCtx,childItemId,null);
       return mRssHolder.creatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        T item =source.get(position);
        mRssHolder.bindTxtToView(item,holder);
    }

    @Override
    public int getItemCount() {
        return source.size();
    }

    abstract static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


    public interface IRssViewHolder<T>{
        int childItemViewId();
        void bindTxtToView(T t,ViewHolder holder);
        ViewHolder creatViewHolder(View itemView);
    }
}
