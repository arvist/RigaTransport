package com.cikoapps.rigatransport;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by arvis.taurenis on 1/29/2015.
 */
public class FavoriteStopsTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.favorite_routes_tab_layout, container, false);

        return rootView;
    }
}
