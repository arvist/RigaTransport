package com.cikoapps.rigatransport;

import android.content.Context;
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


        public RouteListArrayAdapter(Context context, ArrayList<Route> values) {
            super(context, R.layout.route_row_layout,values);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater theInflater = LayoutInflater.from(getContext());

            Route route =  getItem(position);

            View theView = theInflater.inflate(R.layout.route_row_layout, parent, false);

            TextView routeNumTextView = (TextView) theView.findViewById(R.id.route_num);
            TextView startStopTextView = (TextView) theView.findViewById(R.id.start_stop);
            TextView endStopTextView = (TextView) theView.findViewById(R.id.end_stop);
            ImageView favoriteImageView = (ImageView) theView.findViewById(R.id.favorite_image);

            startStopTextView.setText(route.getStart());
            endStopTextView.setText(route.getEnd());
            routeNumTextView.setText(route.getNumber()+"");
            favoriteImageView.setImageResource(R.drawable.favorites);




            return theView;
        }
    }


