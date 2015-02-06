package com.cikoapps.rigatransport;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

public class TabbedFragment extends Fragment {

    public static final String TAG = TabbedFragment.class.getSimpleName();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static TabbedFragment newInstance() {
        return new TabbedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabbed, container, false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        Calendar today = Calendar.getInstance();
        if (today.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || today.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            mViewPager.setCurrentItem(1);
        } else {
            mViewPager.setCurrentItem(0);
        }
        return view;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {

            switch (index) {
                case 0:
                    return new WeekdayTimeTableTab();
                case 1:
                    return new WeekendTimeTableTab();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public String getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "     Weekdays     ";
                case 1:
                    return "     Weekends     ";
            }
            return null;
        }
    }
}
