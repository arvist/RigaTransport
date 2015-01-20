package com.cikoapps.rigatransport;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by arvis on 15.19.1.
 */
public class CategoriesActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_layout);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
    }

    public void onGridElementClick(View view) {
        TextView textView = (TextView) view.findViewById(R.id.grid_image_text);
        String selection = textView.getText().toString();
        Toast.makeText(getApplicationContext(), selection, Toast.LENGTH_SHORT).show();
    }
}

