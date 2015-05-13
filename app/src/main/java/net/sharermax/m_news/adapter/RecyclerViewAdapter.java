package net.sharermax.m_news.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import net.sharermax.m_news.R;
import net.sharermax.m_news.activity.EditWeiboActivity;
import net.sharermax.m_news.adapter.viewholder.RecyclerHeaderHolderView;
import net.sharermax.m_news.adapter.viewholder.RecyclerItemViewHolder;
import net.sharermax.m_news.network.WebResolve;
import net.sharermax.m_news.view.ItemDialog;

import java.util.List;
import java.util.Map;

/**
 * Author: SharerMax
 * Time  : 2015/3/5
 * E-Mail: mdcw1103@gmail.com
 */
public class RecyclerViewAdapter<T extends Map<String, String>> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String CLASS_NAME = "RecyclerViewAdapter";
    public static final int FLAG_HEADER_TOOLBAR = 0;
    public static final int FLAG_HEADER_TAB = 1;
    public static final int FLAG_ITEM = 2;
    public static final int HEADER_COUNT = 2;
    private List<T> data;
    private boolean mUseCardView;
    private boolean mItemDialogEnable;
    public RecyclerViewAdapter(List<T> data, boolean useCardView) {
        this.data = data;
        this.mUseCardView = useCardView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        Log.v(CLASS_NAME, "create");
//        if (viewType == FLAG_HEADER_TOOLBAR) {
//            View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.padding_toolbar, parent, false);
//            return new RecyclerHeaderHolderView(view1);
//        } else if (viewType == FLAG_ITEM) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(
//                    mUseCardView ? R.layout.main_content_item : R.layout.main_content_item_no_card, parent, false);
//            return new RecyclerItemViewHolder(view);
//        }

        switch (viewType) {
            case FLAG_HEADER_TOOLBAR:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.padding_toolbar, parent, false);
                return new RecyclerHeaderHolderView(view1);
            case FLAG_HEADER_TAB:
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.padding_tab, parent, false);
                return new RecyclerHeaderHolderView(view2);
            case FLAG_ITEM:
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        mUseCardView ? R.layout.main_content_item : R.layout.main_content_item_no_card, parent, false);
                return new RecyclerItemViewHolder(view);
            default:
                break;
        }

        throw new RuntimeException("There is no type that matches the type" +
                " make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof RecyclerItemViewHolder) {
            ((RecyclerItemViewHolder) holder).textView.setText(data.get(position-HEADER_COUNT).get("title"));
            initAnimation(holder.itemView);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String title = data.get(position-HEADER_COUNT).get(WebResolve.FIELD_TITLE);
                    String url = data.get(position-HEADER_COUNT).get(WebResolve.FIELD_URL);
                    String sendData = title + url;
                    if (mItemDialogEnable) {
                        showItemDialog(v.getContext(), title, url);
                        return true;
                    }
                    showActivity(v.getContext(), sendData);
                    return true;
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = data.get(position-HEADER_COUNT).get("url");
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    v.getContext().startActivity(intent);
                }
            });
        }

    }

    private void initAnimation(View itemView) {
        Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.scale_in_bottom);
        itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return data.size() + HEADER_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return FLAG_HEADER_TOOLBAR;
        } else if (position == 1) {
            return FLAG_HEADER_TAB;
        }
        return FLAG_ITEM;
    }

    public void addItem(int position, T map) {
        data.add(position, map);
        notifyItemInserted(position + HEADER_COUNT);
    }

    public void addItems(int startPosition, List<T> list) {
        data.addAll(startPosition, list);
        notifyItemRangeInserted(startPosition + HEADER_COUNT, list.size());
    }

    public void clean() {
        data.clear();
        notifyDataSetChanged();
    }

    public int getDataSize() {
        return data.size();
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

    public void setUseCardView(boolean enable) {
        mUseCardView = enable;
    }

    public boolean getUseCardView() {
        return mUseCardView;
    }
}
