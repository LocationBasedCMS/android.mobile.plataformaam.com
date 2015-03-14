package com.plataformaam.mobile.clientefinal.services.broadcastsender;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.plataformaam.mobile.clientefinal.services.VCLocClientService;

/**
 * Created by bernard on 13/02/2015.
 */
public class VClocClientServiceBroadcasterSender {

    public static void sendLocalBroadCast(VCLocClientService service,String BroadCastMessage){
        Log.v(VClocClientServiceBroadcasterSender.class.getSimpleName(),"send : "+BroadCastMessage);
        Intent intentPanel = new Intent( BroadCastMessage );
        LocalBroadcastManager.getInstance(service).sendBroadcast(intentPanel);
    }


}

