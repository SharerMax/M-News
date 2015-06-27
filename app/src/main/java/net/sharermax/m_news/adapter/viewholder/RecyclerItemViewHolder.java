package net.sharermax.m_news.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.sharermax.m_news.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Author: SharerMax
 * Time  : 2015/4/14
 * E-Mail: mdcw1103@gmail.com
 */
public class RecyclerItemViewHolder extends RecyclerView.ViewHolder{
    @InjectView(R.id.main_item_title) public TextView title;
    @InjectView(R.id.main_item_subtext) public TextView sbuText;
    @InjectView(R.id.main_item_hostname) public TextView hostname;
    public RecyclerItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
