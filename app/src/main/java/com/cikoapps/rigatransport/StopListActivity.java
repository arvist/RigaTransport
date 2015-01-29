package com.cikoapps.rigatransport;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by arvis.taurenis on 1/28/2015.
 */
public class StopListActivity extends ActionBarActivity {

    ArrayList<Stop> ArrayListParameters = new ArrayList<Stop>();
    int transport_type = -1;
    int route_num = -1;
    int route_id = -1;
    Bundle savedInstance = null;
    Bundle transportBundle = null;
    UserDataBaseHelper userDataBaseHelper;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstance = savedInstanceState;
        transportBundle = getIntent().getExtras();
        if (transportBundle != null) {
            transport_type = transportBundle.getInt("transport_type");
            route_num = transportBundle.getInt("route_num");

            DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());

            route_id = dataBaseHelper.getRouteIntByTransportTypeAndNum(transport_type, route_num);

            if (transport_type > 0 && transport_type < 4 && route_num > 0) {
                Cursor cursor = dataBaseHelper.getAllRouteStopsByRouteId(route_id);
                if (cursor.moveToFirst()) {
                    do {

                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        int id = cursor.getInt(cursor.getColumnIndex("_id"));
                        double lat = cursor.getDouble(cursor.getColumnIndex("lat"));
                        double lng = cursor.getDouble(cursor.getColumnIndex("lng"));
                        Stop stop = new Stop(name, id, lat, lng);
                        ArrayListParameters.add(stop);
                    } while (cursor.moveToNext());
                }
            }


            setContentView(R.layout.route_list_layout);
            ListAdapter theAdapter;
            theAdapter = new StopListArrayAdapter(this, ArrayListParameters);
            ListView theListView = (ListView) findViewById(R.id.route_list);
            theListView.setAdapter(theAdapter);
            dataBaseHelper.close();

            userDataBaseHelper = new UserDataBaseHelper(getApplicationContext());
        }

    }

    @Override
    public View onCreateView(String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    public void onStopMapClick(View view) {
            Cursor cursor = null;


            int position = Integer.parseInt(view.getTag().toString());
            Stop stop = ArrayListParameters.get(position);
            ArrayList<LatLng> latLngArrayList = new ArrayList<LatLng>();
            latLngArrayList.add(stop.getLatLng());
            ArrayList<String> namesArrayList = new ArrayList<String>();
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
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.switch_dir:
                switch_directions();
                return true;
            case R.id.favorite_route:
                favorite_route(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void favorite_route(MenuItem item) {

        if (!userDataBaseHelper.isFavoriteRoute(route_id)) {
            userDataBaseHelper.addFavoriteRoute(route_id);
            userDataBaseHelper.close();
            menu.findItem(R.id.favorite_route).setIcon(R.drawable.favorites_clicked);
        } else {
            userDataBaseHelper.removeFavoriteRoute(route_id);
            userDataBaseHelper.close();
            menu.findItem(R.id.favorite_route).setIcon(R.drawable.favorites);
        }
    }

    private void switch_directions() {
        ListAdapter theAdapter;
        theAdapter = new StopListArrayAdapter(this, ArrayListParameters);
        Collections.reverse(ArrayListParameters);
        theAdapter = new StopListArrayAdapter(this, ArrayListParameters);
        ListView theListView = (ListView) findViewById(R.id.route_list);
        theListView.setAdapter(theAdapter);
    }
}
