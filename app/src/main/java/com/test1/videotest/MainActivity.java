package com.test1.videotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.test1.videotest.adapter.ViewPagerAdapter;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    ViewPagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        addTabs();

    }

    private void init() {

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);


    }

    private void addTabs() {

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_taskview));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_post));
        //implement below if you want to add notifications tab
        //tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_heart));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_profile));

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_home);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_fill);
                        break;

                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_taskview_fill);
                        break;

                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_post_fill);
                        break;

                    case 3:
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_profile_fill);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
                        break;

                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_taskview);
                        break;

                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_post);
                        break;

                    case 3:
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_profile);
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_fill);
                        break;

                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_taskview_fill);
                        break;

                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_post_fill);
                        break;

                    case 3:
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_profile_fill);
                        break;
                }

            }
        });


    }
}