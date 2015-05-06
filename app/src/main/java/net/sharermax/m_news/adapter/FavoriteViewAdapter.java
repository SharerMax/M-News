package net.sharermax.m_news.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author: SharerMax
 * Time  : 2015/5/6
 * E-Mail: mdcw1103@gmail.com
 */
public class FavoriteViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int FLAG_HEADER = 0;
    private static final int FLAG_ITEM = 1;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == position) {
            return FLAG_HEADER;
        }
        return FLAG_ITEM;
    }

    private class FavoriteViewHolder extends RecyclerView.ViewHolder {

        public FavoriteViewHolder(View itemView) {
            super(itemView);
        }
    }
}
