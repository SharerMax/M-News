package net.sharermax.m_news.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private boolean mUseCardView;
    public RecyclerViewAdapter(List<HashMap<String, String>> data, boolean useCardView) {
        this.data = data;
        this.mUseCardView = useCardView;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    String sendData = RecyclerViewAdapter.this.data.get(getPosition()).get("title")
                                        + RecyclerViewAdapter.this.data.get(getPosition()).get("url");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, sendData);
                    sendIntent.setType("text/plain");
                    v.getContext().startActivity(Intent.createChooser(sendIntent, v.getResources().getString(R.string.share_to)));
                    return false;
                }
            });
            mTextView = (TextView)itemView.findViewById(R.id.main_item_textview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(v.getContext(), RecyclerViewAdapter.this.data.get(getPosition()).get("url"), Toast.LENGTH_LONG).show();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(
                mUseCardView ? R.layout.main_content_item : R.layout.main_content_item_no_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextView.setText(data.get(position).get("title"));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
