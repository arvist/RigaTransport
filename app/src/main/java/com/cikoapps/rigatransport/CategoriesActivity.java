package com.cikoapps.rigatransport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;


/**
 * Creation date 1/19/2015
 * -------------------------
 * Modified 2/1/2015 by Arvis code formatting
 */
public class CategoriesActivity extends ActionBarActivity {

    private GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_layout);
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));


    }

    public void onGridElementClick(View view) {
        TextView textView = (TextView) view.findViewById(R.id.grid_image_text);

        String selectedCategoryName = textView.getText().toString();
        int transport_type = 1 + ImageAdapter.getTitlesIndex(selectedCategoryName);

        if (selectedCategoryName.equalsIgnoreCase("favorites")) {
            Intent favoritesActivity = new Intent(CategoriesActivity.this, FavoritesActivity.class);


            startActivity(favoritesActivity);
        } else {
            Intent routeListActivity = new Intent(this, RouteListActivity.class);
            routeListActivity.putExtra("transport_type", transport_type);
            startActivity(routeListActivity);
        }
    }


}

