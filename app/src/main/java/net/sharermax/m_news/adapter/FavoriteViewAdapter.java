package net.sharermax.m_news.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharermax.m_news.R;
import net.sharermax.m_news.activity.EditWeiboActivity;
import net.sharermax.m_news.adapter.viewholder.RecyclerHeaderHolderView;
import net.sharermax.m_news.adapter.viewholder.RecyclerItemViewHolder;
import net.sharermax.m_news.network.WebResolve;

import java.util.List;

/**
 * Author: SharerMax
 * Time  : 2015/5/6
 * E-Mail: mdcw1103@gmail.com
 */
public class FavoriteViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int FLAG_HEADER = 0;
    private static final int FLAG_ITEM = 1;
    private List<DatabaseAdapter.NewsDataRecord> mRecordList;
    private boolean mUseCardView;

    public FavoriteViewAdapter(List<DatabaseAdapter.NewsDataRecord> recordList, boolean useCardView) {
        mRecordList = recordList;
        mUseCardView = useCardView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                mUseCardView ? R.layout.main_content_item : R.layout.main_content_item_no_card, parent, false);
        return new RecyclerItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RecyclerItemViewHolder) {
            ((RecyclerItemViewHolder) holder).textView.setText(mRecordList.get(position).title);
            Animator animator = ObjectAnimator.ofFloat(holder.itemView, "alpha", 0.1f, 1f);
            animator.setDuration(300);
            animator.start();

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String title = mRecordList.get(position).title;
                    String url = mRecordList.get(position).url;
                    String sendData = title + url;
                    showActivity(v.getContext(), sendData);
                    return true;
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = mRecordList.get(position).url;
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
        return mRecordList.size();
    }

    private class FavoriteViewHolder extends RecyclerView.ViewHolder {

        public FavoriteViewHolder(View itemView) {
            super(itemView);
        }
    }

    private void showActivity(Context context, String sendData) {
        Intent sendIntent = new Intent();
        sendIntent.putExtra(EditWeiboActivity.EXTRA_FLAG, sendData);
        sendIntent.setClass(context, EditWeiboActivity.class);
        context.startActivity(sendIntent);
    }
}
