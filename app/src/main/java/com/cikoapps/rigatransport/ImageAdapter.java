package com.cikoapps.rigatransport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static com.cikoapps.rigatransport.R.*;


/**
 * Creation date 1/20/2015
 * -------------------------
 * Modified 2/1/2015 by Arvis code formatting
 */
class ImageAdapter extends BaseAdapter {
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
            grid = inflater.inflate(layout.categorie_view, null);
            TextView textView = (TextView) grid.findViewById(id.grid_image_text);
            ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
            textView.setText(mThumbIdsTitles[position]);
            imageView.setImageResource(mThumbIds[position]);
        } else {
            grid = convertView;
        }
        return grid;

    }

    // Keep all Images in array
    private static Integer[] mThumbIds = {
            R.drawable.bus,
            R.drawable.tram,
            R.drawable.trolley,
            R.drawable.favorites
    };
    private static String[] mThumbIdsTitles = {
            "Bus", "Tram", "Trolley", "Favorites"
    };

    public static int getTitlesIndex(String text) {

        for (int i = 0; i < mThumbIdsTitles.length; i++) {
            if (text.equalsIgnoreCase(mThumbIdsTitles[i])) return i;
        }
        return -1;
    }
}
