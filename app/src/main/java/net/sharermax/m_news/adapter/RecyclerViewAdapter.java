package net.sharermax.m_news.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharermax.m_news.R;
import net.sharermax.m_news.activity.EditWeiboActivity;
import net.sharermax.m_news.adapter.viewholder.RecyclerHeaderHolderView;
import net.sharermax.m_news.adapter.viewholder.RecyclerItemViewHolder;
import net.sharermax.m_news.network.WebResolve;
import net.sharermax.m_news.view.ItemDialog;

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
    private boolean mItemDialogEnable;
    public RecyclerViewAdapter(List<HashMap<String, String>> data, boolean useCardView) {
        this.data = data;
        this.mUseCardView = useCardView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.v(CLASS_NAME, "create");
        if (viewType == FLAG_HEADER) {
            View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.padding, parent, false);
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
                    String title = RecyclerViewAdapter.this.data.get(position-1).get(WebResolve.FIELD_TITLE);
                    String url = RecyclerViewAdapter.this.data.get(position-1).get(WebResolve.FIELD_URL);
                    String sendData = title + url;
                    if (mItemDialogEnable) {
                        showItemDialog(v.getContext(), title, url);
                        return true;
                    }
                    showActivity(v.getContext(), sendData);
//                    DatabaseAdapter databaseAdapter = DatabaseAdapter.getInstance(v.getContext());
//                    databaseAdapter.setSendData(sendData);
//                    DatabaseAdapter.NewsDataRecord itemRecord = new DatabaseAdapter.NewsDataRecord();
//                    itemRecord.title = title;
//                    itemRecord.url = url;
//                    itemRecord.time = System.currentTimeMillis();
//                    databaseAdapter.setItemRecord(itemRecord);
//                    ItemDialog dialog = ItemDialog.getInstance(v.getContext());
//                    dialog.setAdapter(databaseAdapter);
//                    dialog.show();
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

    public void setItemDialogEnable(boolean enable) {
        mItemDialogEnable = enable;
    }

    private void showItemDialog(Context context, String title, String url) {
        String sendData = title + url;
        DatabaseAdapter databaseAdapter = DatabaseAdapter.getInstance(context);
        databaseAdapter.setSendData(sendData);
        DatabaseAdapter.NewsDataRecord itemRecord = new DatabaseAdapter.NewsDataRecord();
        itemRecord.title = title;
        itemRecord.url = url;
        itemRecord.time = System.currentTimeMillis();
        databaseAdapter.setItemRecord(itemRecord);
        ItemDialog dialog = ItemDialog.getInstance(context);
        dialog.setAdapter(databaseAdapter);
        dialog.show();
    }

    private void showActivity(Context context, String sendData) {
        Intent sendIntent = new Intent();
        sendIntent.putExtra(EditWeiboActivity.EXTRA_FLAG, sendData);
        sendIntent.setClass(context, EditWeiboActivity.class);
        context.startActivity(sendIntent);
    }
}
