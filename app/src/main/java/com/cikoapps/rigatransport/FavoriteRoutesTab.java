package com.cikoapps.rigatransport;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by arvis.taurenis on 1/29/2015.
 */
public class FavoriteRoutesTab extends Fragment {
    ArrayList<Route> ArrayListParameters = new ArrayList<Route>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

                DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
                UserDataBaseHelper userDataBaseHelper = new UserDataBaseHelper((getActivity().getApplicationContext()));
                int [] paramArray = userDataBaseHelper.getRouteFavoriteIds();
                if(paramArray.length>0) {


                    Cursor cursor = dataBaseHelper.getFavoriteRoutes(paramArray);
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
            View rootView = inflater.inflate(R.layout.route_list_layout, container, false);

            ListAdapter theAdapter = new RouteListArrayAdapter(getActivity().getApplicationContext(), ArrayListParameters);
            final ListView theListView = (ListView) rootView.findViewById(R.id.route_list);

            theListView.setAdapter(theAdapter);


        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Route route =(Route) theListView.getItemAtPosition(position);
                int routeNum = Integer.parseInt((view.findViewById(R.id.route_map)).getTag().toString());

                Intent intent = new Intent(getActivity(), StopListActivity.class);
                intent.putExtra("transport_type", route.getType());
                intent.putExtra("route_num", routeNum);
                startActivity(intent);
            }
        });





            dataBaseHelper.close();

        return rootView;
        }
    public void onRouteClick(View view) {

        int routeNum = Integer.parseInt((view.findViewById(R.id.route_map)).getTag().toString());

        Intent intent = new Intent(getActivity(), StopListActivity.class);
        intent.putExtra("transport_type", 3);
        intent.putExtra("route_num", routeNum);
        startActivity(intent);
    }
    }

