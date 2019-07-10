package priv.ky2.sparetime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import priv.ky2.sparetime.R;
import priv.ky2.sparetime.bean.ZhihuDailyNews;

/**
 * Created by wangkaiyan on 2017/4/19.
 */

public class ZHDailyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context mContext;
    private final LayoutInflater inflater;
    private List<ZhihuDailyNews.Question> list = new ArrayList<ZhihuDailyNews.Question>();
    private OnRecyclerViewOnClickListener mListener;

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = 1;

    public ZHDailyAdapter(Context context, List<ZhihuDailyNews.Question> list){
        mContext = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_NORMAL:
                return new NormalViewHolder(inflater.inflate(R.layout.home_list_item_layout, parent, false), mListener);
            case TYPE_FOOTER:
                return new FooterViewHolder(inflater.inflate(R.layout.list_footer, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof NormalViewHolder) {

            ZhihuDailyNews.Question item = list.get(position);

            if (item.getImages().get(0) == null){
                ((NormalViewHolder)holder).itemImg.setImageResource(R.drawable.placeholder);
            } else {
                Glide.with(mContext)
                        .load(item.getImages().get(0))
                        .asBitmap()
                        .placeholder(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .error(R.drawable.placeholder)
                        .centerCrop()
                        .into(((NormalViewHolder)holder).itemImg);
            }
            ((NormalViewHolder)holder).tvLatestNewsTitle.setText(item.getTitle());
        }
        // handle the footer view holder
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) {
            return ZHDailyAdapter.TYPE_FOOTER;
        }
        return ZHDailyAdapter.TYPE_NORMAL;
    }

    public void setItemClickListener(OnRecyclerViewOnClickListener listener){
        this.mListener = listener;
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView itemImg;
        private TextView tvLatestNewsTitle;
        private OnRecyclerViewOnClickListener listener;

        public NormalViewHolder(View itemView, OnRecyclerViewOnClickListener listener) {
            super(itemView);
            itemImg = (ImageView) itemView.findViewById(R.id.imageViewCover);
            tvLatestNewsTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.OnItemClick(v,getLayoutPosition());
            }
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }

    }
}
