package net.sharermax.m_news.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import net.sharermax.m_news.BuildConfig;
import net.sharermax.m_news.R;
import net.sharermax.m_news.activity.EditWeiboActivity;
import net.sharermax.m_news.activity.NewsViewerActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Author: SharerMax
 * Time  : 2015/5/6
 * E-Mail: mdcw1103@gmail.com
 */
public class FavoriteViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String CLASS_NAME = "FavoriteViewAdapter";
    private List<DatabaseAdapter.NewsDataRecord> mRecordList;
    private boolean mUseCardView;
    private DatabaseAdapter mDatabaseAdapter;
    private View mParentView;

    public FavoriteViewAdapter(List<DatabaseAdapter.NewsDataRecord> recordList, boolean useCardView) {
        mRecordList = recordList;
        mUseCardView = useCardView;
    }

    public FavoriteViewAdapter(List<DatabaseAdapter.NewsDataRecord> recordList, boolean useCardView, View parentView) {
        mRecordList = recordList;
        mUseCardView = useCardView;
        mParentView = parentView;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                mUseCardView ? R.layout.favorite_item : R.layout.favorite_item_no_card, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FavoriteViewHolder) {
            final String title = mRecordList.get(position).title;
            final String url = mRecordList.get(position).url;
            FavoriteViewHolder favVH = (FavoriteViewHolder)holder;
            ((FavoriteViewHolder) holder).textView.setText(title);
            Animator animator = ObjectAnimator.ofFloat(holder.itemView, "alpha", 0.1f, 1f);
            animator.setDuration(300);
            animator.start();
            // test start
            favVH.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            favVH.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, favVH.deleteImage);


            favVH.textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String sendData = title + url;
                    showActivity(v.getContext(), sendData);
                    return true;
                }
            });
            favVH.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(v.getContext(), NewsViewerActivity.class);
                    intent.putExtra(NewsViewerActivity.FLAG_EXTRA_TITLE, title);
                    intent.putExtra(NewsViewerActivity.FLAG_EXTRA_URL,url);
                    v.getContext().startActivity(intent);
//                    intent.setAction(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(url));
//                    v.getContext().startActivity(intent);
                }
            });
            favVH.deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mDatabaseAdapter) {
                        final DatabaseAdapter.NewsDataRecord record = new DatabaseAdapter.NewsDataRecord();
                        DatabaseAdapter.NewsDataRecord tempRecord = mRecordList.get(position);
                        record.time = tempRecord.time;
                        record.title = tempRecord.title;
                        record.url = tempRecord.url;
                        record._id = tempRecord._id;
                        mDatabaseAdapter.beginTransaction();
                        mDatabaseAdapter.delete(mRecordList.get(position)._id);
                        Snackbar snackbar = Snackbar.make(mParentView, "Delete", Snackbar.LENGTH_LONG);
                        snackbar.setAction(R.string.fav_activity_undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mRecordList.add(position, record);
                                notifyItemInserted(position);
                                mDatabaseAdapter.endTransaction();
                            }
                        });
                        snackbar.show();
                        mRecordList.remove(position);
                        notifyItemRemoved(position);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mDatabaseAdapter.inTransaction()) {
                                    mDatabaseAdapter.setTransactionSuccessful();
                                    mDatabaseAdapter.endTransaction();
                                    if (BuildConfig.DEBUG) {
                                        Log.v(CLASS_NAME, "successful");
                                    }
                                }
                            }
                        }, snackbar.getDuration() == Snackbar.LENGTH_LONG ? 2750L : 1500L);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }

    public void setDataBaseAdapter(DatabaseAdapter adapter) {
        mDatabaseAdapter = adapter;
    }

    private void showActivity(Context context, String sendData) {
        Intent sendIntent = new Intent();
        sendIntent.putExtra(EditWeiboActivity.EXTRA_FLAG, sendData);
        sendIntent.setClass(context, EditWeiboActivity.class);
        context.startActivity(sendIntent);
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.item_delete) public ImageView deleteImage;
        @InjectView(R.id.main_item_title) public TextView textView;
        @InjectView(R.id.swipe_layout) public SwipeLayout swipeLayout;
        public FavoriteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
