package com.cikoapps.rigatransport;

/**
 * Creation date 1/31/2015
 * -------------------------
 * Modified 2/1/2015 code formatting
 * Modified 4/2/2015
 */

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class TimeTableActivity extends FragmentActivity {

    private static ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mDrawerItems;
    private static DrawerLayout mDrawerLayout;

    private int routeId = -1;
    private int stopId = -1;
    private ArrayList<String> mDrawerItemsArrayList = new ArrayList<>();
    private static ProgressDialog pd;
    private static Context mContext;

    private UserDataBaseHelper userDataBaseHelper;

    private String stopName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);

        mContext = getApplicationContext();
        Bundle routeBundle = getIntent().getExtras();
        if (routeBundle != null) {
            routeId = routeBundle.getInt("route_id");
            stopId = routeBundle.getInt("stop_id");
        }
        // Initialize database objects
        userDataBaseHelper = new UserDataBaseHelper(getApplicationContext());
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        Cursor cursor = dataBaseHelper.getAllRouteStopsByRouteId(routeId);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                if (id == stopId) stopName = name;
                mDrawerItemsArrayList.add(name);

            } while (cursor.moveToNext());
        }
        dataBaseHelper.close();
        setContentView(R.layout.activity_time_table_test);
        mDrawerItems = mDrawerItemsArrayList.toArray(new String[mDrawerItemsArrayList.size()]);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Add items to the ListView
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mDrawerItems));

        // Set Custom View to Action Bar
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.stop_time_table_action_bar);

        // Creates Action Bar Drawer Toggle
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, TabbedFragment.newInstance(), TabbedFragment.TAG).commit();
        }

        // Set's image appropriate favorite image. Either favorited or not favorited
        ImageView favorite_stop_image_view = (ImageView) getActionBar().getCustomView().findViewById(R.id.favorites_stop_view);
        TextView stop_name_text_view = (TextView) getActionBar().getCustomView().findViewById(R.id.stop_name_action_bar);
        stop_name_text_view.setText(stopName);
        if (!userDataBaseHelper.isFavoriteStop(stopId)) {
            favorite_stop_image_view.setImageResource(R.drawable.favorites);
        } else {
            favorite_stop_image_view.setImageResource(R.drawable.favorites_clicked);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

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

    /*
        Reloads navigation drawer with new information
     */
    public static void reloadDrawer(String[] newDrawerItems) {
        mDrawerLayout.setEnabled(true);
        mDrawerList.setAdapter(new ArrayAdapter<>(mContext, R.layout.drawer_list_item, newDrawerItems));
    }

    /*
        Enables loading widget, blocks UI while getting information
     */
    public static void showLoadingWidget() {
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Retrieving data");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
    }

    /*
        Closes loading widget and opens navigation drawer if not already opened;
     */
    public static void closeLoadingWidget() {
        pd.cancel();
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        } else {
            mDrawerLayout.openDrawer(Gravity.START);
        }
    }

    /*
      Adds stop or removes stop from favorites
     */
    public void onFavoriteIconClick(View view) {
        ImageView fave_stop_view = (ImageView) view;
        if (!userDataBaseHelper.isFavoriteStop(stopId)) {
            userDataBaseHelper.addFavoriteStop(stopId);
            fave_stop_view.setImageResource(R.drawable.favorites_clicked);
        } else {
            userDataBaseHelper.close();
            userDataBaseHelper.removeFavoriteStop(stopId);
            userDataBaseHelper.close();
            fave_stop_view.setImageResource(R.drawable.favorites);
        }
    }

    /*
        On Click Drawer Icon, opens if closed and vice versa
     */
    public void onDrawerIconClick(View view) {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        } else {
            mDrawerLayout.openDrawer(Gravity.START);
        }
    }

}

