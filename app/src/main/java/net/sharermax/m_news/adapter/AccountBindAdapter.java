package net.sharermax.m_news.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import net.sharermax.m_news.R;
import net.sharermax.m_news.support.Utility;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Author: SharerMax
 * Time  : 2015/4/9
 * E-Mail: mdcw1103@gmail.com
 */
public class AccountBindAdapter extends BaseAdapter {
    public static final String CLASS_NAME = "AccountBindAdapter";
    public static final String FLAG_CIRCLE_IMAGE = "circle_image";
    public static final String FLAG_TEXT = "text";
    private LayoutInflater mInflater;
    private List<Map<String, Object>> mData;
    private Context mContext;
    public AccountBindAdapter(Context context, List<Map<String, Object>> data) {
        mContext = context;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.accountbind_item, null);
            viewHolder = new ViewHolder();
            viewHolder.circleImageView = (CircleImageView)convertView.findViewById(R.id.circle_image);
            viewHolder.textView = (TextView)convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Log.v(CLASS_NAME, "getview");
        Map map = mData.get(position);
        viewHolder.textView.setText((String) map.get(FLAG_TEXT));
        if (map.get(FLAG_CIRCLE_IMAGE) instanceof String) {
            loadImage(viewHolder.circleImageView, map);
        } else {
            viewHolder.circleImageView.setImageResource((int)map.get(FLAG_CIRCLE_IMAGE));
        }

        return convertView;
    }

    private void loadImage(final CircleImageView imageView, final Map map) {
        String url = (String)map.get(FLAG_CIRCLE_IMAGE);
        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v(CLASS_NAME, error.toString());
                        imageView.setImageResource((int)map.get(FLAG_CIRCLE_IMAGE));
                    }
                });
        Utility.getRequestQueue(mContext).add(imageRequest);
    }

    static class ViewHolder {
        CircleImageView circleImageView;
        TextView textView;
    }
}
