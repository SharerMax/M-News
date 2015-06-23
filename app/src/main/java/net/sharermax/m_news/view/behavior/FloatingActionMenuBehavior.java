package net.sharermax.m_news.view.behavior;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import net.sharermax.m_news.BuildConfig;

import java.util.List;

/**
 * Author: SharerMax
 * Time  : 2015/6/23
 * E-Mail: mdcw1103@gmail.com
 */
public class FloatingActionMenuBehavior extends CoordinatorLayout.Behavior {
    private Rect mTmpRect;
    private boolean mIsAnimatingOut;
    private float mTranslationY;
    public FloatingActionMenuBehavior() {
        super();
    }
    public FloatingActionMenuBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (BuildConfig.DEBUG) {
            Log.v("Behavior", dependency.getClass().getName());
        }
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if(dependency instanceof Snackbar.SnackbarLayout) {
            this.updateFabTranslationForSnackbar(parent, child, dependency);
        }
        return false;
    }

    private void updateFabTranslationForSnackbar(CoordinatorLayout parent, View fab, View snackbar) {
        float translationY = this.getFabTranslationYForSnackbar(parent, fab);
        if(translationY != this.mTranslationY) {
            ViewCompat.animate(fab).cancel();
            if(Math.abs(translationY - this.mTranslationY) == (float)snackbar.getHeight()) {
                ViewCompat.animate(fab).translationY(translationY).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setListener((ViewPropertyAnimatorListener)null);
            } else {
                ViewCompat.setTranslationY(fab, translationY);
            }

            this.mTranslationY = translationY;
        }

    }

    private float getFabTranslationYForSnackbar(CoordinatorLayout parent, View fab) {
        float minOffset = 0.0F;
        List dependencies = parent.getDependencies(fab);
        int i = 0;

        for(int z = dependencies.size(); i < z; ++i) {
            View view = (View)dependencies.get(i);
            if(view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(minOffset, ViewCompat.getTranslationY(view) - (float)view.getHeight());
            }
        }

        return minOffset;
    }

    private void animateIn(FloatingActionsMenu button) {
        button.setVisibility(View.VISIBLE);
        if(Build.VERSION.SDK_INT >= 14) {
            ViewCompat.animate(button).scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener((ViewPropertyAnimatorListener)null).start();
        } else {
            Animation anim = android.view.animation.AnimationUtils.loadAnimation(button.getContext(), android.support.design.R.anim.fab_in);
            anim.setDuration(200L);
            anim.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            button.startAnimation(anim);
        }

    }

    private void animateOut(final FloatingActionsMenu menu) {
        if(Build.VERSION.SDK_INT >= 14) {
            ViewCompat.animate(menu).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(new ViewPropertyAnimatorListener() {
                public void onAnimationStart(View view) {
                    FloatingActionMenuBehavior.this.mIsAnimatingOut = true;
                }

                public void onAnimationCancel(View view) {
                    FloatingActionMenuBehavior.this.mIsAnimatingOut = false;
                }

                public void onAnimationEnd(View view) {
                    FloatingActionMenuBehavior.this.mIsAnimatingOut = false;
                    view.setVisibility(View.GONE);
                }
            }).start();
        } else {
            Animation anim = android.view.animation.AnimationUtils.loadAnimation(menu.getContext(), android.support.design.R.anim.abc_fade_out);
            anim.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            anim.setDuration(200L);
            anim.setAnimationListener(new AnimationUtils.AnimationListenerAdapter() {
                public void onAnimationStart(Animation animation) {
                    FloatingActionMenuBehavior.this.mIsAnimatingOut = true;
                }

                public void onAnimationEnd(Animation animation) {
                    FloatingActionMenuBehavior.this.mIsAnimatingOut = false;
                    menu.setVisibility(View.GONE);
                }
            });
            menu.startAnimation(anim);
        }

    }
}
