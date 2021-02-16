package com.jay.chatapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewpageAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragment=new ArrayList<>();
    public ViewpageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragment.get(position);
    }

    @Override
    public int getCount() {
        return fragment.size();
    }
    public void addFragment(Fragment fragment)
    {
        this.fragment.add(fragment);

    }
}
