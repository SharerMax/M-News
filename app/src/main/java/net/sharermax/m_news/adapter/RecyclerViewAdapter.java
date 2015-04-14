package net.sharermax.m_news.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sharermax.m_news.R;
import net.sharermax.m_news.activity.EditWeiboActivity;
import net.sharermax.m_news.adapter.viewholder.RecyclerHeaderHolderView;
import net.sharermax.m_news.adapter.viewholder.RecyclerItemViewHolder;

import java.util.HashMap;
import java.util.List;

/**
 * Author: SharerMax
 * Time  : 2015/3/5
 * E-Mail: mdcw1103@gmail.com
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String CLASS_NAME = "RecyclerViewAdapter";
    public static final int FLAG_HEADER = 0;
    public static final int FLAG_ITEM = 1;
    private List<HashMap<String, String>> data;
    private boolean mUseCardView;
    public RecyclerViewAdapter(List<HashMap<String, String>> data, boolean useCardView) {
        this.data = data;
        this.mUseCardView = useCardView;
    }

//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        public TextView mTextView;
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    Intent sendIntent = new Intent();
//                    String sendData = RecyclerViewAdapter.this.data.get(getAdapterPosition()).get("title")
//                                        + RecyclerViewAdapter.this.data.get(getAdapterPosition()).get("url");
//                    sendIntent.putExtra(EditWeiboActivity.EXTRA_FLAG, sendData);
//                    sendIntent.setClass(v.getContext(), EditWeiboActivity.class);
//                    v.getContext().startActivity(sendIntent);
//                    return true;
//                }
//            });
//            mTextView = (TextView)itemView.findViewById(R.id.main_item_textview);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String url = RecyclerViewAdapter.this.data.get(getAdapterPosition()).get("url");
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(url));
//                    v.getContext().startActivity(intent);
//                }
//            });
//        }
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.v(CLASS_NAME, "create");
        if (viewType == FLAG_HEADER) {
            View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_header, parent, false);
            return new RecyclerHeaderHolderView(view1);
        } else if (viewType == FLAG_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    mUseCardView ? R.layout.main_content_item : R.layout.main_content_item_no_card, parent, false);
            return new RecyclerItemViewHolder(view);
        }
        throw new RuntimeException("There is no type that matches the type " + viewType +
                " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof RecyclerItemViewHolder) {
            ((RecyclerItemViewHolder) holder).textView.setText(data.get(position-1).get("title"));
            Animator animator = ObjectAnimator.ofFloat(holder.itemView, "alpha", 0.1f, 1f);
            animator.setDuration(300);
            animator.start();

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent sendIntent = new Intent();
                    String sendData = RecyclerViewAdapter.this.data.get(position-1).get("title")
                            + RecyclerViewAdapter.this.data.get(position-1).get("url");
                    sendIntent.putExtra(EditWeiboActivity.EXTRA_FLAG, sendData);
                    sendIntent.setClass(v.getContext(), EditWeiboActivity.class);
                    v.getContext().startActivity(sendIntent);
                    return true;
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = RecyclerViewAdapter.this.data.get(position-1).get("url");
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    v.getContext().startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return FLAG_HEADER;
        }
        return FLAG_ITEM;
    }
}
