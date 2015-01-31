package com.cikoapps.rigatransport;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by arvis.taurenis on 1/30/2015.
 */
public class FavePageAdapter extends FragmentStatePagerAdapter {


    public FavePageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new WeekdayTimeTableTab();
            case 1:
                // Games fragment activity
                return new WeekendTimeTableTab();


        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }


}