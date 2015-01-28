package com.cikoapps.rigatransport;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by arvis.taurenis on 1/28/2015.
 */
public class StopListActivity extends ActionBarActivity {

    ArrayList<Stop> ArrayListParameters = new ArrayList<Stop>();
    int transport_type = -1;
    int route_num = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle transportBundle = getIntent().getExtras();
        if (transportBundle != null) {
            transport_type = transportBundle.getInt("transport_type");
            route_num = transportBundle.getInt("route_num");

            DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());

            int routeId = dataBaseHelper.getRouteIntByTransportTypeAndNum(transport_type, route_num);

            if (transport_type > 0 && transport_type < 4 && route_num > 0) {
                Cursor cursor = dataBaseHelper.getAllRouteStopsByRouteId(routeId);
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
            ListAdapter theAdapter = new StopListArrayAdapter(this, ArrayListParameters);
            ListView theListView = (ListView) findViewById(R.id.route_list);
            theListView.setAdapter(theAdapter);
            dataBaseHelper.close();
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
}
