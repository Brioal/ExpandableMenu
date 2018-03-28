package com.brioal.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;

/**
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by Brioal on 2018/3/27.
 */

public class ExpandableMenu extends ViewGroup {
    private Context mContext;
    // 动画的间隔
    private int mDuration = 500;
    // 插补器
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    // 菜单是否展开了
    private boolean isExpend = false;
    // 是否已经初始化了
    private boolean isInited = false;

    public ExpandableMenu(@NonNull Context context) {
        this(context, null);
    }

    public ExpandableMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

    }

    /**
     * 设置动画间隔
     *
     * @param duration
     */
    public ExpandableMenu setDuration(int duration) {
        mDuration = duration;
        return this;
    }

    /**
     * 设置插补器
     *
     * @param interpolator
     */
    public ExpandableMenu setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
        return this;
    }



    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int left = i2;
        int top = i3;
        int x = 0;
        int y = 0;

        for (int j = 0; j < getChildCount(); j++) {
            View childView = getChildAt(j);
            int width = childView.getMeasuredWidth();
            int height = childView.getMeasuredHeight();
            left = left - width;
            x++;
            if (top == i3) {
                top = i3 -  height;
            }
            if (left < 0) {
                y++;
                x = 1;
                left = i2 -  width;
                top = top -  height;
            }
            if (j == getChildCount() - 1) {
                // 最后一个,放置在第一个的位置
                childView.layout(i2 - width, i3 - height, i2, i3);
            } else {
                childView.layout(left, top, left + width, top + height);
            }
        }
        if (!isInited) {
            init();
            isInited = true;
        }
    }

    private void init() {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (i != 0) {
                childView.setVisibility(GONE);
            }
        }
        // 设置点击事件
        getChildAt(0).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                expand();
            }
        });
        getChildAt(getChildCount() - 1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
    }

    /**
     * 菜单是否是展开的
     * @return
     */
    public boolean isExpend() {
        return isExpend;
    }


    /**
     * 关闭菜单
     */
    private void close() {
        // 显示第一个按钮
        getChildAt(0).setVisibility(VISIBLE);
        // 隐藏最后一个按钮
        getChildAt(getChildCount() - 1).setVisibility(GONE);
        // 收回菜单
        isExpend = false;
        for (int i = 1; i < getChildCount() - 1; i++) {
            final View childView = getChildAt(i);
            TranslateAnimation animation = new TranslateAnimation(
                    0.0f, getChildAt(0).getLeft() - childView.getLeft(), 0.0f, getChildAt(0).getTop() - childView.getTop()
            );
            animation.setInterpolator(mInterpolator);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    childView.setVisibility(GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            childView.setVisibility(VISIBLE);
            animation.setDuration(mDuration);
            childView.startAnimation(animation);
        }
    }

    /**
     * 展开菜单
     */
    private void expand() {
        // 隐藏第一个按钮
        getChildAt(0).setVisibility(GONE);
        // 显示最后一个按钮
        getChildAt(getChildCount() - 1).setVisibility(VISIBLE);
        isExpend = true;
        for (int i = 1; i < getChildCount() - 1; i++) {
            View childView = getChildAt(i);
            TranslateAnimation animation = new TranslateAnimation(
                    getChildAt(0).getLeft() - childView.getLeft(), 0.0f, getChildAt(0).getTop() - childView.getTop(), 0.0f
            );
            animation.setInterpolator(mInterpolator);
            childView.setVisibility(VISIBLE);
            animation.setDuration(mDuration);
            childView.startAnimation(animation);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

}
