package com.cikoapps.rigatransport;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Creation date 1/25/2015
 * -------------------------
 * Modified 2/1/2015 by Arvis code formatting
 */
public class RouteListActivity extends ActionBarActivity {

    private int transportType;
    private ArrayList<Route> ArrayListParameters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle transportBundle = getIntent().getExtras();
        if (transportBundle != null) {
            transportType = transportBundle.getInt("transport_type");
            setContentView(R.layout.route_list_layout);
            new createRouteList().execute();
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    private class createRouteList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
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
                dataBaseHelper.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ListAdapter theAdapter = new RouteListArrayAdapter(getApplicationContext(), ArrayListParameters);
            final ListView theListView = (ListView) findViewById(R.id.route_list);
            theListView.setAdapter(theAdapter);

            theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    int routeNum = Integer.parseInt((view.findViewById(R.id.route_map)).getTag().toString());

                    Intent intent = new Intent(RouteListActivity.this, StopListActivity.class);

                    intent.putExtra("transport_type", transportType);
                    intent.putExtra("route_num", routeNum);
                    startActivity(intent);
                }
            });
        }
    }
}

