package com.cikoapps.rigatransport;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by arvis on 15.20.1.
 */
public class RouteListArrayAdapter extends ArrayAdapter<Route> {
    Context myContext;

    public RouteListArrayAdapter(Context context, ArrayList<Route> values) {
        super(context, R.layout.route_row_layout, values);
        myContext = context;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        Route route = getItem(position);

        View theView = theInflater.inflate(R.layout.route_row_layout, parent, false);

        TextView routeNumTextView = (TextView) theView.findViewById(R.id.route_num);

        TextView stopNameTextView = (TextView) theView.findViewById(R.id.route_name);

        ImageView googleMapsImage = (ImageView) theView.findViewById(R.id.google_maps);

        Typeface font = Typeface.createFromAsset(myContext.getAssets(), "NotoSerif-Regular.ttf");
        stopNameTextView.setTypeface(font);
        stopNameTextView.setText(route.getName());
        stopNameTextView.setTag(route.getNumber());

        googleMapsImage.setImageResource(R.drawable.map);
        googleMapsImage.setTag(route.getNumber());
        googleMapsImage.setTag(route.getNumber());

        routeNumTextView.setTypeface(font);
        routeNumTextView.setText(route.getNumber() + "");


        return theView;
    }

}


