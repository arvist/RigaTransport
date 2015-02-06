package com.cikoapps.rigatransport;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Creation date 2/1/2015
 * -------------------------
 * Modified 2/1/2015 by Arvis code formatting
 */
public class FavoriteStopsTab extends Fragment {

    private ArrayList<Stop> ArrayListParameters = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
        UserDataBaseHelper userDataBaseHelper = new UserDataBaseHelper((getActivity().getApplicationContext()));
        int[] paramArray = userDataBaseHelper.getStopFavoriteIds();
        userDataBaseHelper.close();
        if (paramArray.length > 0) {


            Cursor cursor = dataBaseHelper.getFavoriteStops(paramArray);
            if (cursor.moveToFirst()) {
                do {
                    int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    int route_id = cursor.getInt(cursor.getColumnIndex("route_id"));
                    Stop stop = new Stop(name, _id, route_id);
                    ArrayListParameters.add(stop);
                } while (cursor.moveToNext());
            }
            dataBaseHelper.close();
        }
        View rootView = inflater.inflate(R.layout.stop_list_layout, container, false);

        ListAdapter theAdapter = new StopListArrayAdapter(getActivity().getApplicationContext(), ArrayListParameters, "favorites");
        final ListView theListView = (ListView) rootView.findViewById(R.id.stop_list);

        theListView.setAdapter(theAdapter);


        return rootView;
    }
}


