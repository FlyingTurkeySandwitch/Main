package com.test1.videotest.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.test1.videotest.fragments.Home;
import com.test1.videotest.fragments.PostTask;
import com.test1.videotest.fragments.Profile;
import com.test1.videotest.fragments.HelperView;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int noOfTabs;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int noOfTabs) {
        super(fm);
        this.noOfTabs = noOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return new Home();

            case 1:
                return new HelperView();

            case 2:
                return new PostTask();

            case 3:
                return new Profile();

            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
