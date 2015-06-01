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
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfiguration;
import com.plataformaam.mobile.clientefinal.exceptions.InvalidCoordinatesException;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyMessage;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyPositionMessage;
import com.plataformaam.mobile.clientefinal.helpers.gson.adapters.GSonBooleanAsIntAdapter;
import com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.model.post.PostVComUPIPublicationResponse;
import com.plataformaam.mobile.clientefinal.helpers.volley.MyPostStringRequest;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.location.UserPosition;

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

    private synchronized String manipulateQueueOperation(String userPositionContent){
        if( userPositionContent == null ){
            String operation = null;
            if(queue.size()>0){
                operation = queue.remove(0);
            }
            return operation;
        }else{
            queue.add(userPositionContent);
        }
        return null;
    }


    public void onEvent(MyMessage message){
        if( message.getSender().equals(MyService.class.getSimpleName())){
            if( message.getMessage().equals(MyAppConfiguration.EVENT_BUS_MESSAGE.LOGIN_DONE)){
                manipulateQueueOperation(MyAppConfiguration.UserPositionContent.LOGIN_POSITION);
            }
        }
        return;
    }

    public void onEvent(MyPositionMessage message){
        return;
    }


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
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdate(){
        initLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,MyLocationService.this);
    }

    private void stopLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,MyLocationService.this);
    }


    // LISTENER
        @Override
        public void onConnected(Bundle bundle) {
            Log.i(MyAppConfiguration.LOG.Service,"onConnected(Bundle bundle)"+bundle);
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if( lastLocation != null ){
                Log.i(MyAppConfiguration.LOG.ServiceLocation," Latitude: "+lastLocation.getLatitude() + " Longitude: "+lastLocation.getLongitude() );

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
        Log.i(MyAppConfiguration.LOG.ServiceLocation," Latitude: "+location.getLatitude() + " Longitude: "+location.getLongitude() );
        //IF HAS A CONNECTED USER
        sendMessage( prepareUserPosition(location));
    }



    private UserPosition prepareUserPosition(Location location)  {
        User user = AppController.getInstance().getOnlineUser();
        UserPosition userPosition = null;
        try {
            if( user != null ) {
                userPosition = new UserPosition();
                userPosition.setUser(user);
                userPosition.setLatitude(location.getLatitude());
                userPosition.setLongitude(location.getLongitude());
                Calendar c  = Calendar.getInstance();
                java.util.Date date = c.getTime();
                userPosition.setCurrentTime(date);
                String positionContent = manipulateQueueOperation(null);
                if( positionContent == null ) {
                    positionContent  = MyAppConfiguration.UserPositionContent.NAVIGATE;
                }
                userPosition.setContent(positionContent);
                AppController.getInstance().getOnlineUser().getUserPositions().add(userPosition);
                if(
                           positionContent.equals(MyAppConfiguration.UserPositionContent.LOGIN_POSITION )
                        || positionContent.equals(MyAppConfiguration.UserPositionContent.PUBLISH )
                        || positionContent.equals(MyAppConfiguration.UserPositionContent.SUBSCRIBE_VCOM )
                ) {
                    saveUserPosition(null);
                }

            }
        } catch (InvalidCoordinatesException e) {
            e.printStackTrace();
        }
        return userPosition;

    }


    private void sendMessage(UserPosition position){
        if(position != null ) {
            MyPositionMessage message = new MyPositionMessage(MyLocationService.class.getSimpleName(), MyAppConfiguration.EVENT_BUS_MESSAGE.LOCATION_CHANGE, position);
            EventBus.getDefault().post(message);
        }
    }

    //THE OPERATION FAIL ISN'T A PROBLEM
    //EXCEPTION IS A PROBLEM TO PERFORMANCE
    private synchronized void saveUserPosition(final String userPositionContent){

        User user = AppController.getInstance().getOnlineUser();
        if( user != null ){
            if( user.getUserPositions() != null && user.getUserPositions().size()>0 ){

                final UserPosition position = user.getUserPositions().get(user.getUserPositions().size() -1);
                position.setUser(user);
                if( userPositionContent != null ){
                    position.setContent(userPositionContent);
                }
                //SALVANDO AS POSIÇÔES
                String request_url = MyAppConfiguration.getInstance().prepareWebService("UserPosition");
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
                                    sendMessage(position);

                                }else{
                                    Log.i(MyAppConfiguration.LOG.Service,"SavePosition: Falha "+position.getContent()+" :" +position.getLatitude()+"/"+position.getLongitude() );
                                }
                            }
                        }
                        ,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(MyAppConfiguration.LOG.Service,"SavePosition: Falha "+position.getContent()+" :" +position.getLatitude()+"/"+position.getLongitude() );
                                Log.i(MyAppConfiguration.LOG.Service,"\n"+error.getMessage() );
                            }
                        }
                );
                AppController.getInstance().addToRequestQueue(stringRequest,MyAppConfiguration.VOLLEY_TAG.MANIPULATE_UPI);
            }
        }
    }



}
