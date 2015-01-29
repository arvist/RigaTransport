package com.cikoapps.rigatransport;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by arvis on 15.20.1.
 */
public class RouteListActivity extends ActionBarActivity {

    int transportType;
    ArrayList<Route> ArrayListParameters = new ArrayList<Route>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle transportBundle = getIntent().getExtras();
        if (transportBundle != null) {
            transportType = transportBundle.getInt("transport_type");
            DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
            if (transportType > 0 && transportType < 4) {
                Cursor cursor = dataBaseHelper.getTransportListQuery(transportType);
                if (cursor.moveToFirst()) {
                    do {
                        int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                        int number = cursor.getInt(cursor.getColumnIndex("number"));
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        int type = cursor.getInt(cursor.getColumnIndex("type"));
                        Route route = new Route(name, number, _id, type);
                        ArrayListParameters.add(route);
                    } while (cursor.moveToNext());
                }
            }
            setContentView(R.layout.route_list_layout);
            ListAdapter theAdapter = new RouteListArrayAdapter(this, ArrayListParameters);
            final ListView theListView = (ListView) findViewById(R.id.route_list);
            theListView.setAdapter(theAdapter);

            theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Route o =(Route) theListView.getItemAtPosition(position);
                    //prestationEco str=(prestationEco)o;//As you are using Default String Adapter
                    Toast.makeText(getBaseContext(),o.getName(),Toast.LENGTH_SHORT).show();
                    int routeNum = Integer.parseInt((view.findViewById(R.id.route_map)).getTag().toString());

                    Intent intent = new Intent(RouteListActivity.this, StopListActivity.class);

                    intent.putExtra("transport_type", transportType);
                    intent.putExtra("route_num", routeNum);
                    startActivity(intent);
                }
            });

            dataBaseHelper.close();
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

   /* public void testMapClick(View view) {
        Cursor cursor = null;
        ArrayList<LatLng> flagArrayList = new ArrayList<LatLng>();
        ArrayList<String> names = new ArrayList<String>();

        int num = Integer.parseInt(view.getTag().toString());
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        int route_id = dataBaseHelper.getRouteIntByTransportTypeAndNum(transportType, num);
        if (route_id != -1) {
            cursor = dataBaseHelper.getAllRouteStopsPositionsByRouteId(route_id);
            if (cursor.moveToFirst()) {
                do {
                    double lat = cursor.getDouble(cursor.getColumnIndex("lat"));
                    double lng = cursor.getDouble(cursor.getColumnIndex("lng"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    LatLng latLng = new LatLng(lat, lng);

                    flagArrayList.add(latLng);
                    names.add(name);

                } while (cursor.moveToNext());
            }


        }


        Intent intent = new Intent(RouteListActivity.this, Stop_Map_Activity.class);
        intent.putExtra("flagArrayList", flagArrayList);
        intent.putExtra("names", names);
        startActivity(intent);
    }*/

    /*public void onRouteClick(View view) {

        int routeNum = Integer.parseInt((view.findViewById(R.id.google_maps)).getTag().toString());

        Intent intent = new Intent(RouteListActivity.this, StopListActivity.class);
        intent.putExtra("transport_type", transportType);
        intent.putExtra("route_num", routeNum);
        startActivity(intent);
    }*/
}
