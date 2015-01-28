package com.cikoapps.rigatransport;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by arvis.taurenis on 1/28/2015.
 */
public class StopListArrayAdapter extends ArrayAdapter<Stop> {
    Context myContext;

    public StopListArrayAdapter(Context context, ArrayList<Stop> values) {
        super(context, R.layout.stop_row_layout, values);
        myContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater theInflater = LayoutInflater.from(getContext());

        Stop stop = getItem(position);

        View theView = theInflater.inflate(R.layout.stop_row_layout, parent, false);

        ImageView stopImageView = (ImageView) theView.findViewById(R.id.stop_image);
        TextView stopNameView = (TextView) theView.findViewById(R.id.stop_name);
        ImageView stopMapImageView = (ImageView) theView.findViewById(R.id.stop_map_image);

        Typeface font = Typeface.createFromAsset(myContext.getAssets(), "NotoSerif-Regular.ttf");
        stopNameView.setTypeface(font);
        stopNameView.setText(stop.getName());
        stopMapImageView.setImageResource(R.drawable.map);

        int count = getCount();
        if (position == 0) {
            stopImageView.setImageResource(R.drawable.start);
        } else if (position == count - 1) {
            stopImageView.setImageResource(R.drawable.finish);
        } else {
            stopImageView.setImageResource(R.drawable.middle);
        }

        return theView;
    }

}
