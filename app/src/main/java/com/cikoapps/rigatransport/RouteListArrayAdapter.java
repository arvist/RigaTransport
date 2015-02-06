package com.cikoapps.rigatransport;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


/**
 * Creation date 1/26/2015
 * -------------------------
 * Modified 2/1/2015 by Arvis code formatting
 */
class RouteListArrayAdapter extends ArrayAdapter<Route> {
    private Context myContext;
    Typeface font;

    public RouteListArrayAdapter(Context context, ArrayList<Route> values) {
        super(context, R.layout.route_row_layout, values);
        myContext = context;
        this.font = Typeface.createFromAsset(myContext.getAssets(), "NotoSerif-Regular.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        final Route route = getItem(position);

        View theView = theInflater.inflate(R.layout.route_row_layout, parent, false);

        TextView routeNumTextView = (TextView) theView.findViewById(R.id.route_num);

        TextView stopNameTextView = (TextView) theView.findViewById(R.id.route_name);

        ImageView googleMapsImage = (ImageView) theView.findViewById(R.id.route_map);


        stopNameTextView.setTypeface(font);
        stopNameTextView.setText(route.getName());
        stopNameTextView.setTag(route.getNumber());

        googleMapsImage.setImageResource(R.drawable.map);
        googleMapsImage.setTag(route.getNumber());

        routeNumTextView.setTypeface(font);
        routeNumTextView.setText(route.getNumber() + "");


        googleMapsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor;
                ArrayList<LatLng> flagArrayList = new ArrayList<>();
                ArrayList<String> names = new ArrayList<>();

                int num = Integer.parseInt(view.getTag().toString());
                DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
                int route_id = dataBaseHelper.getRouteIntByTransportTypeAndNum(route.getType(), num);
                dataBaseHelper.close();
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
                    dataBaseHelper.close();
                }
                Intent intent = new Intent(getContext(), Stop_Map_Activity.class);
                intent.putExtra("flagArrayList", flagArrayList);
                intent.putExtra("names", names);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });

        return theView;
    }

}


