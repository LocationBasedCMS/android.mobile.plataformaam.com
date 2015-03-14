package com.plataformaam.mobile.clientefinal.models.location;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by bernard on 31/01/2015.
 */
public class VirtualSpace {
    int id;
    String name;
    double
            startLatitude,
            startLongitude,
            stopLatitude,
            stopLongitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public double getStopLatitude() {
        return stopLatitude;
    }

    public void setStopLatitude(double stopLatitude) {
        this.stopLatitude = stopLatitude;
    }

    public double getStopLongitude() {
        return stopLongitude;
    }

    public void setStopLongitude(double stopLongitude) {
        this.stopLongitude = stopLongitude;
    }


    public VirtualSpace(int id, String name, double startLatitude, double startLongitude, double stopLatitude, double stopLongitude) {
        this.id = id;
        this.name = name;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.stopLatitude = stopLatitude;
        this.stopLongitude = stopLongitude;
    }

    public VirtualSpace() {
    }


    public PolylineOptions getPolylineOptionsGoogleMapsV2(){
        return new PolylineOptions()
                .add(new LatLng(    getStartLatitude()    ,   getStartLongitude()  ))
                .add(new LatLng(    getStartLatitude()    ,   getStopLongitude()    ))  // North of the previous point, but at the same longitude
                .add(new LatLng(    getStopLatitude()     ,   getStopLongitude()   ))  // Same latitude, and 30km to the west
                .add(new LatLng(    getStopLatitude()     ,   getStartLongitude()  ))  // Same longitude, and 16km to the south
                .add(new LatLng(getStartLatitude(), getStartLongitude())); // Closes the polyline.
    }

    public PolygonOptions getPolygonOptionsGoogleMapsV2(){
        return new PolygonOptions()
                .add(new LatLng(    getStartLatitude()    ,   getStartLongitude()  ))
                .add(new LatLng(    getStartLatitude()    ,   getStopLongitude()    ))  // North of the previous point, but at the same longitude
                .add(new LatLng(    getStopLatitude()     ,   getStopLongitude()   ))  // Same latitude, and 30km to the west
                .add(new LatLng(    getStopLatitude()     ,   getStartLongitude()  ))  // Same longitude, and 16km to the south
                .add(new LatLng(    getStartLatitude()    ,   getStartLongitude()  )); // Closes the polyline.
    }


    public boolean isInside(LatLng latLng){
        if( latLng == null )
            return false;
        boolean testLatitude = startLatitude< latLng.latitude && latLng.latitude < stopLatitude;
        boolean testLongitude = startLongitude<latLng.longitude && latLng.longitude <stopLongitude;
        return testLatitude && testLongitude;
    }

    public boolean isInside(Location location){
        if( location == null )
            return false;
        boolean testLatitude = startLatitude< location.getLatitude() && location.getLatitude() < stopLatitude;
        boolean testLongitude = startLongitude< location.getLongitude() && location.getLongitude() <stopLongitude;
        return testLatitude && testLongitude;
    }



}
