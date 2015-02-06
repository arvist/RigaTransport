package com.cikoapps.rigatransport;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Creation date 1/28/2015
 * -------------------------
 * Modified 2/1/2015 by Arvis code formatting
 */
public class StopListActivity extends ActionBarActivity {

    ArrayList<Stop> ArrayListParameters = new ArrayList<>();
    private int transport_type = -1;
    private int route_num = -1;
    private int route_id = -1;
    private int direction = 0;
    private Bundle savedInstance = null;
    private Bundle transportBundle = null;
    private UserDataBaseHelper userDataBaseHelper;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstance = savedInstanceState;
        transportBundle = getIntent().getExtras();
        if (transportBundle != null) {
            transport_type = transportBundle.getInt("transport_type");
            route_num = transportBundle.getInt("route_num");
            setContentView(R.layout.stop_list_layout);
            userDataBaseHelper = new UserDataBaseHelper(getApplicationContext());
            DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
            route_id = dataBaseHelper.getRouteIntByTransportTypeAndNum(transport_type, route_num);
            dataBaseHelper.close();
            new createStopList(route_id).execute();
        }

    }

    @Override
    public View onCreateView(String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    public void onStopMapClick(View view) {

        int position = Integer.parseInt(view.getTag().toString());
        Stop stop = ArrayListParameters.get(position);
        ArrayList<LatLng> latLngArrayList = new ArrayList<>();
        latLngArrayList.add(stop.getLatLng());
        ArrayList<String> namesArrayList = new ArrayList<>();
        namesArrayList.add(stop.getName());
        Intent intent = new Intent(StopListActivity.this, Stop_Map_Activity.class);
        intent.putExtra("flagArrayList", latLngArrayList);
        intent.putExtra("names", namesArrayList);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_stop_list, menu);
        if (!userDataBaseHelper.isFavoriteRoute(route_id)) {
            menu.findItem(R.id.favorite_route).setIcon(R.drawable.favorites);
        } else {
            menu.findItem(R.id.favorite_route).setIcon(R.drawable.favorites_clicked);
        }
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_dir:
                switch_directions();
                return true;
            case R.id.favorite_route:
                favorite_route();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void favorite_route() {
        boolean isFavRoute = userDataBaseHelper.isFavoriteRoute(route_id);
        if (!isFavRoute) {
            userDataBaseHelper.addFavoriteRoute(route_id);
            menu.findItem(R.id.favorite_route).setIcon(R.drawable.favorites_clicked);
        } else {
            userDataBaseHelper.removeFavoriteRoute(route_id);
            menu.findItem(R.id.favorite_route).setIcon(R.drawable.favorites);
        }
    }

    private void switch_directions() {
        if (direction == 0) direction = 1;
        else direction = 0;

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        if (direction == 1) {
            route_id = dataBaseHelper.getRouteIntByTransportTypeAndNumRev(transport_type, route_num);
            dataBaseHelper.close();
        } else {
            route_id = dataBaseHelper.getRouteIntByTransportTypeAndNum(transport_type, route_num);
            dataBaseHelper.close();
        }
        new createStopList(route_id).execute();
        setContentView(R.layout.stop_list_layout);

    }

    private class createStopList extends AsyncTask<String, Void, String> {

        public createStopList(int route_id) {

        }

        @Override
        protected String doInBackground(String... params) {

            DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
            if (transport_type > 0 && transport_type < 4 && route_num > 0) {
                ArrayListParameters.clear();
                Cursor cursor = dataBaseHelper.getAllRouteStopsByRouteId(route_id);
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        int id = cursor.getInt(cursor.getColumnIndex("_id"));
                        double lat = cursor.getDouble(cursor.getColumnIndex("lat"));
                        double lng = cursor.getDouble(cursor.getColumnIndex("lng"));
                        Stop stop = new Stop(name, id, lat, lng, route_id);
                        ArrayListParameters.add(stop);
                    } while (cursor.moveToNext());
                }
                dataBaseHelper.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ListAdapter theAdapter;
            theAdapter = new StopListArrayAdapter(getApplicationContext(), ArrayListParameters, "routeList");
            ListView theListView = (ListView) findViewById(R.id.stop_list);
            theListView.setAdapter(theAdapter);


        }
    }
}
