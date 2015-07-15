package com.alieeen.bulbdialclock.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alieeen.bulbdialclock.fragments.ChatFragment;
import com.alieeen.bulbdialclock.fragments.MainFragment;

/**
 * Created by alinekborges on 07/07/15.
 */
public class SlidingAdapter extends FragmentPagerAdapter {

    private ChatFragment chatFragment;
    private MainFragment mainFragment;

    public SlidingAdapter(FragmentManager fm) {
        super(fm);

        chatFragment = ChatFragment.newInstance();
        mainFragment = MainFragment.newInstance();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return mainFragment;
            case 1:
                return chatFragment;
            default:
                return mainFragment;
        }


    }

    @Override
    public int getCount() {
        return 1;
    }


}

