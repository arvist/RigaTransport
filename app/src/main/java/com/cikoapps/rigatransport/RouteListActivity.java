package com.cikoapps.rigatransport;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by arvis on 15.20.1.
 */
public class RouteListActivity extends ActionBarActivity {

    String[] routeEndArray = {"Pētersalas iela","Sarkandaugava","Klīniskā slimnīca", "Iļģuciems", "Ieriķu iela",
    "Šmerlis","Ieriķu iela", "Mežciems","Ķengarags","Šmerlis","Purvciems","Mežciems","Ziepniekkalns","Televīzijas centrs",
    "Pļavnieki","Purvciems","Pētersalas iela","Iļģuciems","Ziepniekkalns"};
    String[] routeStartArray = {"Valmieras iela","Centrāltirgus","Daugavas stadions","Stacijas laukums","Centrālā stacija",
    "Āgenskalna priedes","Centrāltirgus","Esplanāde","Latvijas Universitāte","Pļavnieki","Centrālā stacija", "Centrālā stacija",
    "Pētersalas iela","Latvijas Universitāte","Centrālā stacija","Centrālā stacija","A/s Dzintars","Brīvības iela","Stacijas laukums"
    };
    int [] routeNumArray = {1,3,5,9,11,12,13,14,15,16,17,18,19,20,22,23,24,25,27};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_list_layout);
        ArrayList<Route> ArrayListParameters = new ArrayList<Route>();
        for(int i=0; i<routeEndArray.length;i++){

            Route r = new Route(routeStartArray[i],routeEndArray[i], routeNumArray[i]);
            ArrayListParameters.add(r);
        }
        ListAdapter theAdapter = new RouteListArrayAdapter(this,ArrayListParameters);
        ListView theListView = (ListView) findViewById(R.id.route_list);
        theListView.setAdapter(theAdapter);
    }


    @Override
    public View onCreateView(String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

}
