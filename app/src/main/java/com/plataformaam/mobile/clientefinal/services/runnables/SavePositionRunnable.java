package com.plataformaam.mobile.clientefinal.services.runnables;

import android.location.Location;
import android.util.Log;

import com.plataformaam.mobile.clientefinal.configurations.MyAppData;
import com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.model.post.PostUserPosition;
import com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.operation.WebClientPostData;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.location.UserPosition;
import com.plataformaam.mobile.clientefinal.services.VCLocClientService;

import java.util.Calendar;

/**
 * Created by bernard on 13/02/2015.
 */
public class SavePositionRunnable implements Runnable {
    VCLocClientService mService;
    final String upContent;
    MyAppData  myAppData;
    public SavePositionRunnable(VCLocClientService service,String upContent) {
        this.mService = service;
        this.upContent = upContent;
        myAppData = MyAppData.getInstance();
    }

    @Override
    public void run() {

        User user = mService.getUser();

        //LOGIN TESTE
        if( user != null ){

            Location location = mService.getLocation();
            if(mService.testLocationVariation(location,upContent) ) {

                UserPosition userPosition = new UserPosition(
                        null,
                        user,
                        Calendar.getInstance().getTime(),
                        upContent,
                        location.getLatitude(),
                        location.getLongitude());

                PostUserPosition postUserPosition = (PostUserPosition) (new WebClientPostData(UserPosition.class, PostUserPosition.class, userPosition, mService.getUserController() )).execute();
                UserPosition savedPosition = postUserPosition.getData().getUserPosition();
                savedPosition.setUser( mService.getUser() );

                mService.getUserController().setLastPosition(savedPosition);
                Log.i(SavePositionRunnable.class.getSimpleName(),"Salvo : User("+savedPosition.getUser().getId()+") : LAT/LNG -> "+savedPosition.getLatitude()+"/"+savedPosition.getLongitude() );

                mService.getSpeedController().serviceSpeedUp();
            }else{
                mService.getSpeedController().serviceSpeedDown();
            }
        }
    }
}

