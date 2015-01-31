package com.cikoapps.rigatransport;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by arvis.taurenis on 1/28/2015.
 */
public class Stop {
    String name;
    int id;
    LatLng latLng;
    int routeId;

    public Stop(String name, int id, double lat, double lng, int routeId) {
        this.name = name;
        this.id = id;
        latLng = new LatLng(lat, lng);
        this.routeId = routeId;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public int getRouteId() {
        return routeId;
    }
}
