package com.plataformaam.mobile.clientefinal.adapters;

import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;

/**
 * Created by bernard on 09/03/2015.
 */
public class UPITextInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View myView;
    private final VComUPIPublication publication;

    public UPITextInfoWindowAdapter(VComUPIPublication publication, View myView) {
        this.publication = publication;
        this.myView = myView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {


        return null;
    }
}
