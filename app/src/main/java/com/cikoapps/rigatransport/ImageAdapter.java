package com.cikoapps.rigatransport;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import static com.cikoapps.rigatransport.R.*;

/**
 * Created by arvis on 15.19.1.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(layout.categorie_view, null);
            TextView textView = (TextView) grid.findViewById(id.grid_image_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(mThumbIdsTitles[position]);
            imageView.setImageResource(mThumbIds[position]);
        } else {
            grid = (View) convertView;
        }
        return grid;

    }

    // Keep all Images in array
    public static Integer[] mThumbIds = {
            R.drawable.tram, R.drawable.trolley,
            R.drawable.bus, R.drawable.nightbus,
            R.drawable.minibus, R.drawable.favorites
    };
    public static String[] mThumbIdsTitles = {
            "Tram","Trolley","Bus","MiniBus","NightBus","Favorites"
    };

    public static int getTitlesIndex(String text) {

        for(int i=0; i < mThumbIdsTitles.length;i++){
            if(text.equalsIgnoreCase(mThumbIdsTitles[i])) return i;
        }
        return -1;
    }
}
