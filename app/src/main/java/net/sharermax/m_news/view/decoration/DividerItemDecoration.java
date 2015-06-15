package net.sharermax.m_news.view.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author: SharerMax
 * Time  : 2015/6/14
 * E-Mail: mdcw1103@gmail.com
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTR = {android.R.attr.listDivider};
    private Drawable mDivider;
    public DividerItemDecoration(Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(ATTR);
        mDivider = typedArray.getDrawable(0);
        typedArray.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getRight() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        if (1 > childCount) {
            super.onDraw(c, parent, state);
            return;
        }
        int dividerSize = mDivider.getIntrinsicHeight();
        for (int i=1; i<childCount; i++) {
            View childView = parent.getChildAt(i-1);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)childView.getLayoutParams();
            int top = childView.getBottom() + params.bottomMargin;
            int bottom = top + dividerSize;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
    }
}
