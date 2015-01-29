package com.cikoapps.rigatransport;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by arvis.taurenis on 1/29/2015.
 */
public class TabsPageAdapter extends FragmentStatePagerAdapter {


    public TabsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new FavoriteStopsTab();
            case 1:
                // Games fragment activity
                return new FavoriteRoutesTab();
            case 2:

        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }



}
