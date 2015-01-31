package com.cikoapps.rigatransport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    public View getView(int position, View convertView, final ViewGroup parent) {

        final LayoutInflater theInflater = LayoutInflater.from(getContext());

        final Stop stop = getItem(position);

        View theView = theInflater.inflate(R.layout.stop_row_layout, parent, false);

        ImageView stopImageView = (ImageView) theView.findViewById(R.id.stop_image);
        TextView stopNameView = (TextView) theView.findViewById(R.id.stop_name);
        ImageView stopMapImageView = (ImageView) theView.findViewById(R.id.stop_map_image);

        Typeface font = Typeface.createFromAsset(myContext.getAssets(), "NotoSerif-Regular.ttf");
        stopNameView.setTypeface(font);
        stopNameView.setText(stop.getName());
        stopMapImageView.setImageResource(R.drawable.map);

        stopMapImageView.setTag(position);
        int count = getCount();
        if (position == 0) {
            stopImageView.setImageResource(R.drawable.start);
        } else if (position == count - 1) {
            stopImageView.setImageResource(R.drawable.finish);
        } else {
            stopImageView.setImageResource(R.drawable.middle);
        }


        // theView.setTag(stop.getId());


        theView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               /* SharedPreferences prefs  = myContext.getSharedPreferences("RigaTransport", myContext.MODE_PRIVATE);
                int direction = prefs.getInt("dir",-1);*/


                LinearLayout stopListLayout = (LinearLayout) theInflater.inflate(R.layout.stop_list_layout, parent, false);
                ListView stopListView = (ListView) stopListLayout.findViewById(R.id.stop_list);


                Intent intent = new Intent(getContext(), StopTimeTableActivity.class);
                intent.putExtra("stop_id", stop.getId());
                //intent.putExtra("direction",direction);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });


        return theView;
    }

}
