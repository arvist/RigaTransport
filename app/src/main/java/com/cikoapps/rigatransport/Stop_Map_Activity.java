package com.cikoapps.rigatransport;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by arvis.taurenis on 1/27/2015.
 */
public class Stop_Map_Activity extends ActionBarActivity {

    static LatLng flagPosition = null;
    static LatLng middleFlagPosition = null;
    static String stopName = null;

    private GoogleMap googleMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.stop_map_layout);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            ArrayList<LatLng> flagArrayList = (ArrayList<LatLng>) bundle.get("flagArrayList");
            ArrayList<String> names = (ArrayList<String>) bundle.get("names");


            //flagPosition = new LatLng(lat,lng);
            middleFlagPosition = flagArrayList.get(flagArrayList.size() / 2);

            try {
                if (googleMap == null) {
                    googleMap = ((MapFragment) getFragmentManager()
                            .findFragmentById(R.id.stop_map)).getMap();
                }

                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                //googleMap.setIndoorEnabled(true);
                //googleMap.setBuildingsEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);

                for (int i = 0; i < flagArrayList.size(); i++) {
                    flagPosition = flagArrayList.get(i);
                    stopName = names.get(i);
                    Marker marker = googleMap.addMarker(new MarkerOptions()
                                    .position(flagPosition).title(stopName)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.dot))
                    );
                }

                // Move the camera instantly to location with a zoom of 15.
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middleFlagPosition, 12));

                // Zoom in, animating the camera.
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
