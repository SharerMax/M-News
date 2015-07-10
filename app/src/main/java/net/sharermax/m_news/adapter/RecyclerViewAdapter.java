package net.sharermax.m_news.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import net.sharermax.m_news.BuildConfig;
import net.sharermax.m_news.R;
import net.sharermax.m_news.activity.EditWeiboActivity;
import net.sharermax.m_news.activity.NewsViewerActivity;
import net.sharermax.m_news.adapter.viewholder.RecyclerHeaderHolderView;
import net.sharermax.m_news.adapter.viewholder.RecyclerItemViewHolder;
import net.sharermax.m_news.api.news.common.NewsData;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: SharerMax
 * Time  : 2015/3/5
 * E-Mail: mdcw1103@gmail.com
 */
public class RecyclerViewAdapter<T extends NewsData> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String CLASS_NAME = "RecyclerViewAdapter";
    public static final int FLAG_HEADER_TOOLBAR = 0;
    public static final int FLAG_HEADER_TAB = 1;
    public static final int FLAG_ITEM = 2;
    public static final int FLAG_FOOTER = 3;
    public static final int HEADER_COUNT = 2;
    private List<T> data;
    private boolean mUseCardView;
    private boolean mItemDialogEnable;
    private int mFooterPosition;
    private int mShowLastPosition = -1;
    private boolean mFirstLoad = true;
    private boolean mListAnimationEnable = true;
    private int mFooterCount = 0;
    public RecyclerViewAdapter(List<T> data, boolean useCardView) {
        this.data = data;
        this.mUseCardView = useCardView;
        mFooterPosition = getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (BuildConfig.DEBUG) {
            Log.v(CLASS_NAME, "create view holder");
        }
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
            case FLAG_FOOTER:
                View view3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_content_footer, parent, false);
                return new RecyclerHeaderHolderView(view3);
            default:
                break;
        }

        throw new RuntimeException("There is no type that matches the type" +
                " make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (BuildConfig.DEBUG) {
            Log.v(CLASS_NAME, "position:" + position);
            Log.v(CLASS_NAME, "viewholder:" + holder.hashCode());
        }
        if (holder instanceof RecyclerItemViewHolder) {
            RecyclerItemViewHolder itemVH = (RecyclerItemViewHolder)holder;
            final String title = data.get(position - HEADER_COUNT).title;
            final String url = data.get(position - HEADER_COUNT).url;
            Log.v(CLASS_NAME, url);
            Pattern pattern = Pattern.compile("\\b([a-z0-9]+(-[a-z0-9]+)*\\.)+[a-z]{2,}\\b");
            Matcher matcher = pattern.matcher(url);
            String hostname ="";
            if (matcher.find()) {
                hostname = matcher.group();
            }
            String subText = data.get(position - HEADER_COUNT).subText;
            itemVH.title.setText(title);
            itemVH.hostname.setText(hostname);
            itemVH.sbuText.setText(subText);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    String url = data.get(position - HEADER_COUNT).get("url");
                    Intent intent = new Intent();
                    intent.putExtra(NewsViewerActivity.FLAG_EXTRA_TITLE, title);
                    intent.putExtra(NewsViewerActivity.FLAG_EXTRA_URL, url);
                    intent.setClass(v.getContext(), NewsViewerActivity.class);
                    v.getContext().startActivity(intent);
                }
            });

            if (mListAnimationEnable) {
                setAnimation(holder.itemView, position, ((RecyclerItemViewHolder) holder).tag);
            }
            ((RecyclerItemViewHolder) holder).tag = position;
        }
    }

    private void setAnimation(final View itemView, final int position, int itemTag) {
        if (position > mShowLastPosition) {
            if (mFirstLoad) {
                itemView.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.scale_in_bottom);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                itemView.setVisibility(View.VISIBLE);
                                mFirstLoad = true;
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mFirstLoad = false;
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        animation.setFillAfter(true);
                        itemView.startAnimation(animation);
                    }
                }, (position - HEADER_COUNT) * 180);
            } else {
//                if (-1 != itemTag || itemTag != position) {
//                    if (itemView.getAnimation() != null) itemView.getAnimation().cancel();
//                    if (BuildConfig.DEBUG) {
//                        Log.v(CLASS_NAME, "position animation is cancel" + position);
//                    }
//                }
                Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.scale_in_bottom);
                itemView.startAnimation(animation);
            }
            mShowLastPosition = position;
        } else {
            ViewCompat.setAlpha(itemView, 1);
            ViewCompat.setScaleY(itemView, 1);
            ViewCompat.setScaleX(itemView, 1);
            ViewCompat.setTranslationY(itemView, 0);
            ViewCompat.setTranslationX(itemView, 0);
            ViewCompat.setRotation(itemView, 0);
            ViewCompat.setRotationY(itemView, 0);
            ViewCompat.setRotationX(itemView, 0);
            // @TODO https://code.google.com/p/android/issues/detail?id=80863
//        ViewCompat.setPivotY(v, v.getMeasuredHeight() / 2);
            itemView.setPivotY(itemView.getMeasuredHeight() / 2);
            ViewCompat.setPivotX(itemView, itemView.getMeasuredWidth() / 2);
            ViewCompat.animate(itemView).setInterpolator(null);
        }

    }

    @Override
    public int getItemCount() {
        return data.size() + HEADER_COUNT + mFooterCount;
    }

    @Override
    public int getItemViewType(int position) {
//        Log.v(CLASS_NAME, "" + position);
        if (position == 0) {
            return FLAG_HEADER_TOOLBAR;
        } else if (position == 1) {
            return FLAG_HEADER_TAB;
        } else if (position == mFooterPosition) {
            return FLAG_FOOTER;
        }
        return FLAG_ITEM;
    }

    public void addItem(int position, T map) {
        data.add(position, map);
        notifyItemInserted(position + HEADER_COUNT);
    }

    public void addItems(int startPosition, List<T> list) {
        if (null == list || list.isEmpty()) return;
        removeItem(mFooterPosition);
        data.addAll(startPosition, list);
        notifyItemRangeInserted(startPosition + HEADER_COUNT, list.size());
        mFooterPosition += list.size();
        if (0 == mFooterCount) {
            mFooterCount = 1;
            notifyItemInserted(mFooterPosition);
        }
    }

    public void clear() {
        data.clear();
        mFooterCount = 0;
        mFooterPosition = getItemCount();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        notifyItemRemoved(position);
    }

    public int getDataSize() {
        return data.size();
    }

    public void setListAnimationEnable(boolean enable) {
        mListAnimationEnable = enable;
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
