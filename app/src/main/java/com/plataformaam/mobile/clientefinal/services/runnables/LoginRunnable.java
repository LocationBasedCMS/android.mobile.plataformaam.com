package com.plataformaam.mobile.clientefinal.services.runnables;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.plataformaam.mobile.clientefinal.configurations.MyAppConfiguration;
import com.plataformaam.mobile.clientefinal.exceptions.InvalidServiceStateException;
import com.plataformaam.mobile.clientefinal.exceptions.UserNotAllowedException;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.services.VCLocClientService;
import com.plataformaam.mobile.clientefinal.services.broadcastsender.VClocClientServiceBroadcasterSender;
import com.plataformaam.mobile.clientefinal.services.statecontrol.ServiceStateController;

/**
 * Created by bernard on 17/02/2015.
 */
public class LoginRunnable implements Runnable {
    VCLocClientService mService;

    public LoginRunnable(VCLocClientService service) {
        this.mService = service;
        Log.v(LoginRunnable.class.getSimpleName(),"Created -> Service State: "+service.getState() );
    }

    @Override
    public void run() {


        if (mService.getState() == ServiceStateController.LOGGED_USER ) {
            Log.e(LoginRunnable.class.getSimpleName(), "doAutoLogin(EXIT):  Service State : " + mService.getState());

        } else {
            Log.v(LoginRunnable.class.getSimpleName(), "doAutoLogin : Service State : " + mService.getState());

            SharedPreferences sharedpreferences = mService.getSharedPreferences(MyAppConfiguration.Preferences.PREFERENCE_AUTO_LOGIN, Context.MODE_PRIVATE);
            if (sharedpreferences.contains(MyAppConfiguration.Preferences.HTTP_X_REST_USERNAME) && sharedpreferences.contains(MyAppConfiguration.Preferences.HTTP_X_REST_PASSWORD)) {

                User user = new User();
                user.setLogin(sharedpreferences.getString(MyAppConfiguration.Preferences.HTTP_X_REST_USERNAME, ""));
                user.setPassword(sharedpreferences.getString(MyAppConfiguration.Preferences.HTTP_X_REST_PASSWORD, ""));

                try {
                    mService.doLogin(user);
                } catch (InvalidServiceStateException e) {

                    Log.e(VCLocClientService.class.getSimpleName(), " InvalidServiceStateException ->  Login : " + user.getLogin() + "  Password: " + user.getPassword());
                    e.printStackTrace();

                } catch (UserNotAllowedException e) {
                    Log.e(VCLocClientService.class.getSimpleName(), " UserNotAllowedException ->  Login : " + user.getLogin() + "  Password: " + user.getPassword());
                    e.printStackTrace();
                }
            }
            VClocClientServiceBroadcasterSender.sendLocalBroadCast(mService, MyAppConfiguration.BroadCastMessage.AUTO_LOGIN_DONE);
            mService.getSpeedController().serviceSpeedDown();
        }
    }
}
