package com.cikoapps.rigatransport;

/**
 * Created by arvis.taurenis on 1/31/2015.
 */

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class TimeTableActivity extends FragmentActivity {
    private static final String TAG = TimeTableActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private static ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mDrawerItmes;

    int routeId = -1;
    int stopId = -1;
    ArrayList<String> mDrawerItemsArrayList = new ArrayList<String>();

    static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        Bundle routeBundle = getIntent().getExtras();
        if (routeBundle != null) {
            routeId = routeBundle.getInt("route_id");
            stopId = routeBundle.getInt("stop_id");
        }

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        Cursor cursor = dataBaseHelper.getAllRouteStopsByRouteId(routeId);
        if (cursor.moveToFirst()) {
            do {

                String name = cursor.getString(cursor.getColumnIndex("name"));
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                mDrawerItemsArrayList.add(name);

            } while (cursor.moveToNext());
        }
        dataBaseHelper.close();
        setContentView(R.layout.activity_time_table_test);
        mTitle = mDrawerTitle = getTitle();
        mDrawerItmes = mDrawerItemsArrayList.toArray(new String[mDrawerItemsArrayList.size()]);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // Add items to the ListView
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mDrawerItmes));

        // Enable ActionBar app icon to behave as action to toggle the NavigationDrawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, TabbedFragment.newInstance(), TabbedFragment.TAG).commit();
        }
    }

    public static void reloadDrawer(String[] newDrawerItems) {
        mDrawerList.setAdapter(new ArrayAdapter<String>(context, R.layout.drawer_list_item, newDrawerItems));
    }

    /*
     * If you do not have any menus, you still need this function
     * in order to open or close the NavigationDrawer when the user
     * clicking the ActionBar app icon.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * When using the ActionBarDrawerToggle, you must call it during onPostCreate()
     * and onConfigurationChanged()
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
}

