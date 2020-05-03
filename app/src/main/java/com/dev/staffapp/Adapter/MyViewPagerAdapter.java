package com.dev.staffapp.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.dev.staffapp.Fragments.FragmentAccount;
import com.dev.staffapp.Fragments.FragmentAccountLoaded;

public class MyViewPagerAdapter extends FragmentPagerAdapter {
    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                return FragmentAccount.getInstance() ;
            case 1:
                return FragmentAccountLoaded.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
