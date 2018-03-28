### 效果图
![这里写图片描述](https://img-blog.csdn.net/20180328095846793?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI2OTcxODAz/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
![这里写图片描述](https://img-blog.csdn.net/20180328095855780?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI2OTcxODAz/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
#### 本文想试着从头开始讲解,中间贴的代码只是部分的,如果需要全部代码请翻到最后,有造好的轮子和源码.
### 需求:
#### 如效果图所示的效果大家应该见过很多了,但是很多都是把每个菜单的按钮的样式基本上固定了,虽然可以用但是对于不同的项目来说风格真的能搭配上吗?能不能做到每个菜单样式都能自己定义而且不用太过于麻烦?
### 实现思路:
#### 1.自定义ViewGroup,用户只需要往这个组件里面添加按钮即可,组件负责处理菜单按钮的功能,显示,动画等等
#### 2.添加菜单项要是能从xml文件中添加就更好了,方便预览菜单按钮的效果
### 详细思路:
#### 之前了解过其他类似的项目的内部实现方式,有的是默认把所有按钮叠加在一起 ,让展开按钮覆盖后面的菜单按钮,点击展开按钮的时候用ObjectAnimation将其他组件移动到位置,个人觉得这样实现起来是否太过于复杂,为何不能先把菜单按钮放置到展开之后的位置,然后通过动画来做位移的效果,配合按钮的显示与隐藏也能达到同样的效果,而且菜单项本身是没有发生位置变化的.
### 代码实现:
#### 1. 第一步,自定义一个ViewGroup,能够让添加到其中的View按照效果图摆放
##### 设计思路:第一个ChildView和最后一个ChildView分别当作点击展开和关闭的按钮,都放置到右下角,其他的菜单按照自动换行的效果摆放,如下图
![这里写图片描述](https://img-blog.csdn.net/2018032810085044?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI2OTcxODAz/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
#### 代码实现:
##### 创建自定义ViewGroup,继承ViewGroup,重写构造方法:
```
public class ExpandableMenu extends ViewGroup {
    

    public ExpandableMenu(@NonNull Context context) {
        this(context, null);
    }

    public ExpandableMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

```
#### 这时候需要重写`onMeasure`方法来测量子组件并且规定父组件的大小
###### 循环测量子组件,因为一般菜单的使用场景就是覆盖到顶部,所以父组件的大小就干脆都设置为`match_parent`
```

 @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
```
#### 初次之外还需要重写`onLayout`方法来设置子`View`的位置
###### 传入的参数依次为`isChanged`,`left`,`top`,`right`,`bottom`,其中的int值为父组件的四个方向的位置,就是我们用于放置子组件的依据
```
 @Override
protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

}
```
##### 规定子View的位置的方法为:

```
childView.layout(left, top, right, bottom);
```
###### 能获取到childView的宽高,那么只需要确定其left,top的值即可获取到位置的四个参数了
#### 如何获取子View的left,top?
![这里写图片描述](https://img-blog.csdn.net/20180328102649202?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI2OTcxODAz/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
##### 简要的画了个图说明一下,原本是应该考虑每个组件的magin的,后来发现可以按照简单的方法来,不考虑magin,这样onLayout的实现会简单很多,而且可以用padding来达到和magin同样的效果
##### 为了达到自动换行的效果,需要设置一个X方向和Y方向的标志位,每放置一个子View需要对X,Y的值进行变化,X的值是每次减少一个子View的宽度,Y不变,直到X<=0,即需要换行,此使X恢复,Y需要变化,具体的实现代码如下:
```
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
    }
```
#### 之后需要编写展开菜单和隐藏菜单的动画,这部分很简单,所以直接放代码,封装了两个方法,处理动画和菜单项的显示隐藏
###### 其中位移动画是从第一个View的位置移动到当前的位置,位移的距离及是当前View的left,top值与第一个子View的对应参数的差.
```
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
```
#### 对应的隐藏菜单的方法
###### 执行与展开相反的动画,并且在动画结束的时候把菜单项隐藏
```
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
```
#### 之后需要给按钮设置展开和关闭的点击事件,同时默认隐藏其他按钮,代码如下:
```
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
```
###### init()方法需要在子View已经添加进来之后再调用,所以我将init方法放置在了onLayout之后,保证获取到的子View不会为空,但是onLayout在被调用很多次,所以加了个标志位,如下:
```
if (!isInited) {
     init();
     isInited = true;
}
```
#### 至此组件的编写就完了,没有多余的方法,要添加菜单可以直接在xml内添加,也可以用代码添加,只要注意菜单项的大小应当相同,并且第一个和最后一个view是用于展开和关闭的,至于点击事件,在添加到View之前设置即可,不用给第一个和最后一个view设置点击事件,因为设置了也会被覆盖掉.
#### xml使用方式如下:
```

    <com.brioal.view.ExpandableMenu
        android:id="@+id/expandableMenu"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom">
        <!--展开按钮-->
        <ImageButton
            android:layout_width="105dp"
            android:layout_height="105dp"
            android:background="@android:color/transparent"
            android:padding="5dp"
            android:scaleType="centerCrop"
            android:src="@drawable/ic_add_black"/>
        <!--菜单按钮-->
        <ImageButton
            android:layout_width="105dp"
            android:layout_height="105dp"
            android:background="@android:color/transparent"
            android:padding="5dp"
            android:scaleType="centerCrop"
            android:src="@mipmap/ic_launcher_round"/>
        <!--菜单按钮-->
        <ImageButton
            android:layout_width="105dp"
            android:layout_height="105dp"
            android:background="@android:color/transparent"
            android:padding="5dp"
            android:scaleType="centerCrop"
            android:src="@mipmap/ic_launcher_round"/>
        <!--菜单按钮-->
        <ImageButton
            android:layout_width="105dp"
            android:layout_height="105dp"
            android:background="@android:color/transparent"
            android:padding="5dp"
            android:scaleType="centerCrop"
            android:src="@mipmap/ic_launcher_round"/>
        <!--菜单按钮-->
        <ImageButton
            android:layout_width="105dp"
            android:layout_height="105dp"
            android:background="@android:color/transparent"
            android:padding="5dp"
            android:scaleType="centerCrop"
            android:src="@mipmap/ic_launcher_round"/>
        <!--菜单按钮-->
        <ImageButton
            android:layout_width="105dp"
            android:layout_height="105dp"
            android:background="@android:color/transparent"
            android:padding="5dp"
            android:scaleType="centerCrop"
            android:src="@mipmap/ic_launcher_round"/>
        <!--结束按钮-->
        <ImageButton
            android:layout_width="105dp"
            android:layout_height="105dp"
            android:background="@android:color/transparent"
            android:padding="5dp"
            android:scaleType="centerCrop"
            android:src="@drawable/ic_close_black"/>
    </com.brioal.view.ExpandableMenu>
```
##### 然后是整个ExpandableMenu的代码
```
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

```
### 轮子地址:[ExpandableMenu](https://github.com/Brioal/ExpandableMenu)
