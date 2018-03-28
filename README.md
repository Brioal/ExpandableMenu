### 效果图
![这里写图片描述](https://img-blog.csdn.net/20180328095846793?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI2OTcxODAz/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
![这里写图片描述](https://img-blog.csdn.net/20180328095855780?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI2OTcxODAz/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 使用方式
#### 1.在项目的build.gradle内添加以下代码
```
allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
```
#### 2. 在app的build.gradle内添加以下代码
##### 最新版本为:[![](https://www.jitpack.io/v/Brioal/ExpandableMenu.svg)](https://www.jitpack.io/#Brioal/ExpandableMenu)
```
	dependencies {
	        implementation 'com.github.Brioal:ExpandableMenu:1.0'
	}
```

#### 代码讲解地址:[Android:一步步开发一个高度可定制化的扩展菜单](https://blog.csdn.net/qq_26971803/article/details/79724191)

### 示例代码
#### xml代码
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    >

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

</LinearLayout>

```
### MainActivity代码
```
 package com.brioal.expandablemenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.CycleInterpolator;

import com.brioal.view.ExpandableMenu;

 public class MainActivity extends AppCompatActivity {
     private ExpandableMenu mMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMenu = findViewById(R.id.expandableMenu);
        mMenu.setDuration(1000).setInterpolator(new AccelerateInterpolator());
    }
}

```
### 使用注意事项:
#### 1.添加菜单项可以从xml内直接添加,也可以从代码里面添加,但是要注意顺序,第一个子View是用于点击展开的按钮,最后一个是用于点击收起菜单的按钮
#### 2.务必保证每个子View的大小一致,否则显示的位置会不正确
#### 3.提供了设置动画间隔和设置插值器的代码
#### 4.菜单项的点击事件的设置,给xml内菜单项设置id,然后Activity内获取id设置点击事件即可,展开按钮和收起按钮不必设置事件
