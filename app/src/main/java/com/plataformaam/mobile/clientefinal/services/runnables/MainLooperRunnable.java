package com.plataformaam.mobile.clientefinal.services.runnables;


import com.plataformaam.mobile.clientefinal.configurations.MyAppConfiguration;
import com.plataformaam.mobile.clientefinal.configurations.MyAppData;
import com.plataformaam.mobile.clientefinal.services.VCLocClientService;
import com.plataformaam.mobile.clientefinal.services.broadcastsender.VClocClientServiceBroadcasterSender;
import com.plataformaam.mobile.clientefinal.services.statecontrol.ServiceStateController;

/**
 * Created by bernard on 13/02/2015.
 */
public class MainLooperRunnable implements  Runnable{

    final VCLocClientService mService;
    MyAppData myAppData;

    public MainLooperRunnable(VCLocClientService service) {
        this.mService = service;
        myAppData = MyAppData.getInstance();
    }

    @Override
    public void run() {
        try {
            while( true ) {
                switch (mService.getState()){

                    case  ServiceStateController.STARTED:
                        actionOnStartedState();
                        break;
                    case ServiceStateController.LOGGED_USER:
                        actionOnLoggedUserState();
                        break;
                    case ServiceStateController.INSIDE_VCLOC:
                        actionOnInsideVCloc();
                        break;
                    case ServiceStateController.INSIDE_INTERACTION_AREA:
                        actionOnInteractedAreaState();
                        break;
                    default:
                        actionOnStartedState();
                        break;
                }
                Thread.sleep(mService.getWaitTime());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public void actionOnStartedState(){
        mService.loadAllVCom();
        //mService.doAutoLogin();

    }

    public void actionOnLoggedUserState(){

        VClocClientServiceBroadcasterSender.sendLocalBroadCast(mService, MyAppConfiguration.BroadCastMessage.SERVICE_READY);
        mService.saveUserPosition(MyAppConfiguration.UserPositionContent.NAVIGATE);


    }

    public void actionOnInsideVCloc() {

        mService.saveUserPosition(MyAppConfiguration.UserPositionContent.INSIDE_VCLOC);


    }

    public void actionOnInteractedAreaState(){


    }


}
