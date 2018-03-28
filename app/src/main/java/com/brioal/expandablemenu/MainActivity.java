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
