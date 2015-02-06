package com.cikoapps.rigatransport;

import com.google.android.gms.maps.model.LatLng;

/**
 * Creation date 1/28/2015
 * -------------------------
 * Modified 2/1/2015 by Arvis code formatting
 */
class Stop {
    private String name;
    private int id;
    private LatLng latLng;
    private int routeId;

    public Stop(String name, int id, double lat, double lng, int routeId) {
        this.name = name;
        this.id = id;
        latLng = new LatLng(lat, lng);
        this.routeId = routeId;
    }

    public Stop(String name, int id, int routeId) {
        this.name = name;
        this.id = id;
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
