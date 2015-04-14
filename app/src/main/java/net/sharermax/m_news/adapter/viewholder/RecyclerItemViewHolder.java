package net.sharermax.m_news.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.sharermax.m_news.R;

/**
 * Author: SharerMax
 * Time  : 2015/4/14
 * E-Mail: mdcw1103@gmail.com
 */
public class RecyclerItemViewHolder extends RecyclerView.ViewHolder{
    public TextView textView;
    public RecyclerItemViewHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.main_item_textview);
    }
}
