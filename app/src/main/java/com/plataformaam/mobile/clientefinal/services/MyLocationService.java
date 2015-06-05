package com.plataformaam.mobile.clientefinal.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.plataformaam.mobile.clientefinal.AppController;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfig;
import com.plataformaam.mobile.clientefinal.exceptions.InvalidCoordinatesException;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyMessage;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyPositionMessage;
import com.plataformaam.mobile.clientefinal.helpers.gson.adapters.GSonBooleanAsIntAdapter;
import com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.model.post.PostVComUPIPublicationResponse;
import com.plataformaam.mobile.clientefinal.helpers.volley.MyPostStringRequest;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.location.UserPosition;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.userinterfaces.mapsfragments.MapFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MyLocationService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static List<String> queue = Collections.synchronizedList(new ArrayList<String>());




    public MyLocationService() {

    }

    //LIFE CYCLE
        @Override
        public void onCreate() {
            super.onCreate();
            EventBus.getDefault().register(MyLocationService.this);
        }

        @Override
        public void onDestroy() {
            EventBus.getDefault().unregister(MyLocationService.this);
            super.onDestroy();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            callConnect();
            return super.onStartCommand(intent, flags, startId);
        }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }






    private synchronized void callConnect(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    private void initLocationRequest(){
        mLocationRequest  = new LocationRequest();
        mLocationRequest.setInterval(120000);
        mLocationRequest.setFastestInterval(60000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private void startLocationUpdate(){
        initLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MyLocationService.this);
    }

    private void stopLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, MyLocationService.this);
    }


    // LISTENER
        @Override
        public void onConnected(Bundle bundle) {
            Log.i(MyAppConfig.LOG.ServiceLocation, "onConnected(Bundle bundle)" + bundle);
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if( lastLocation != null ){
                Log.i(MyAppConfig.LOG.ServiceLocation, " Latitude: " + lastLocation.getLatitude() + " Longitude: " + lastLocation.getLongitude());
                AppController app =AppController.getInstance();
                if( app != null && app.getOnlineUser() != null ) {
                    UserPosition position = new UserPosition();
                    position.setUser(app.getOnlineUser());
                    try {
                        position.setLongitude(lastLocation.getLatitude());
                        position.setLongitude(lastLocation.getLongitude());
                    } catch (InvalidCoordinatesException e) {
                        e.printStackTrace();
                    }
                    sendMessage(position);
                }

            }
            startLocationUpdate();
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }

    //Update de Localizações
    @Override
    public void onLocationChanged(Location location) {
        if( location != null ){
            Log.i(MyAppConfig.LOG.ServiceLocation,location.toString());
            savePosition(location);
        }
    }


    private void savePosition(Location location){
        AppController app =AppController.getInstance();
        //TEST IF THE APLICATION IS RUNNING AND HAS A LOGGED USER
        if( app != null && app.getOnlineUser() != null && location != null ){

            User user = app.getOnlineUser();
            VComComposite composite = app.getOnlineComposite();
            UserPosition position = new UserPosition();
            position.setUser(user);
            //DATE
            Calendar c  = Calendar.getInstance();
            java.util.Date date = c.getTime();
            position.setCurrentTime(date);


            //CONTENT
            position.setContent(MyAppConfig.POSITION_CONTENT.NAVIGATE);
            if( composite != null ){
                position.setComposite(composite.getId());
                position.setContent(MyAppConfig.POSITION_CONTENT.INSIDE_VCLOC);
            }

            try {
                //LOCATION
                position.setLatitude(location.getLatitude());
                position.setLongitude(location.getLongitude());
                savePositionOperation(position);
                sendMessage(position);

            } catch (InvalidCoordinatesException e) {
                Log.e(MyAppConfig.LOG.ServiceLocation," Invalid Location e->message: "+e.getMessage() );
                e.printStackTrace();
            }


        }

    }

    //THERE's NO PROBLEM IF FAIL
    private void savePositionOperation(final UserPosition position){
        if( position != null ){

            String request_url = MyAppConfig.getInstance().prepareWebService("UserPosition");
            StringRequest stringRequest = new MyPostStringRequest(
                    request_url,
                    position,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            GsonBuilder builderResult = new GsonBuilder();
                            builderResult.setDateFormat("yyyy-MM-dd HH:mm:ss");
                            builderResult.registerTypeAdapter(Boolean.class, new GSonBooleanAsIntAdapter()).registerTypeAdapter(boolean.class , new GSonBooleanAsIntAdapter());
                            Gson gsonResult = builderResult.create();
                            PostVComUPIPublicationResponse output= gsonResult.fromJson( response, PostVComUPIPublicationResponse.class);
                            if( output.isSuccess() && output.getData().getTotalCount() == 1   ) {
                                Log.i(MyAppConfig.LOG.ServiceLocation,MyAppConfig.VOLLEY_TAG.SAVE_POSITION + " SUCCESS  ");
                            }else{
                                Log.i(MyAppConfig.LOG.ServiceLocation,MyAppConfig.VOLLEY_TAG.SAVE_POSITION + " FAIL ");
                            }
                        }
                    }
                    ,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(MyAppConfig.LOG.ServiceLocation,MyAppConfig.VOLLEY_TAG.SAVE_POSITION + " : "+error.getMessage() );
                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(stringRequest, MyAppConfig.VOLLEY_TAG.SAVE_POSITION);

        }
    }

    //
    private static UserPosition lastUserPosition = null;
    private void sendMessage(UserPosition position){
        if(position != null ) {
            lastUserPosition = position;
            MyPositionMessage message = new MyPositionMessage(MyLocationService.class.getSimpleName(), MyAppConfig.EVENT_BUS_MESSAGE.LOCATION_CHANGE, position);
            Log.i(MyAppConfig.LOG.ServiceLocation, message.toString());
            EventBus.getDefault().post(message);
        }
    }

    //NEED TO EVENT BUS
    public void onEvent(MyMessage message){

    }

    public void onEvent(MyPositionMessage message){
        if( message != null && message.getSender().equals(MapFragment.class.getSimpleName()) ){
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.REQUEST_LAST_POSITION) ){
                if( lastUserPosition != null ) {
                    sendMessage(lastUserPosition);
                }
            }
        }
    }





}
