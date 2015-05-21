package net.sharermax.m_news.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import net.sharermax.m_news.R;
import net.sharermax.m_news.activity.EditWeiboActivity;
import net.sharermax.m_news.adapter.viewholder.RecyclerItemViewHolder;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

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
                mUseCardView ? R.layout.favorite_item : R.layout.main_content_item_no_card, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FavoriteViewHolder) {
            FavoriteViewHolder favVH = (FavoriteViewHolder)holder;
            ((FavoriteViewHolder) holder).textView.setText(mRecordList.get(position).title);
            Animator animator = ObjectAnimator.ofFloat(holder.itemView, "alpha", 0.1f, 1f);
            animator.setDuration(300);
            animator.start();
            // test start
            favVH.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            favVH.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, favVH.deleteImage);


            favVH.itemNoCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String title = mRecordList.get(position).title;
                    String url = mRecordList.get(position).url;
                    String sendData = title + url;
                    showActivity(v.getContext(), sendData);
                    return true;
                }
            });
            favVH.itemNoCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = mRecordList.get(position).url;
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    v.getContext().startActivity(intent);
                }
            });
            favVH.deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Delete", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }

    private void showActivity(Context context, String sendData) {
        Intent sendIntent = new Intent();
        sendIntent.putExtra(EditWeiboActivity.EXTRA_FLAG, sendData);
        sendIntent.setClass(context, EditWeiboActivity.class);
        context.startActivity(sendIntent);
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.item_delete) public ImageView deleteImage;
        @InjectView(R.id.item_no_card) public View itemNoCardView;
        @InjectView(R.id.main_item_textview) public TextView textView;
        @InjectView(R.id.swipe_layout) public SwipeLayout swipeLayout;
        public FavoriteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
