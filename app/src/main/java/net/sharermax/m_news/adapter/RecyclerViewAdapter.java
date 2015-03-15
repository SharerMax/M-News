package net.sharermax.m_news.adapter;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import net.sharermax.m_news.R;

import java.util.HashMap;
import java.util.List;

/**
 * Author: SharerMax
 * Time  : 2015/3/5
 * E-Mail: mdcw1103@gmail.com
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    public static final String CLASS_NAME = "RecyclerViewAdapter";
    private List<HashMap<String, String>> data;
    public RecyclerViewAdapter(List<HashMap<String, String>> data) {
        this.data = data;
    }
    public TextView mTextView;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.main_item_textview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), RecyclerViewAdapter.this.data.get(getPosition()).get("url"), Toast.LENGTH_LONG).show();
                    String url = RecyclerViewAdapter.this.data.get(getPosition()).get("url");
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_content_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        mTextView.setText(data.get(position).get("title"));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
