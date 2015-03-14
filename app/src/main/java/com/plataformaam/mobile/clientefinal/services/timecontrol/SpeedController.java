package com.plataformaam.mobile.clientefinal.services.timecontrol;

import com.plataformaam.mobile.clientefinal.services.VCLocClientService;

/**
 * Created by bernard on 17/02/2015.
 */
public class SpeedController
        implements IServiceSpeedController {

    IServiceSpeedControl service;
    protected int WAIT_TIME;

    private static final int MAX_WAIT               = 600000;   //10 minutos
    private static final int REFERENCE_WAIT_TIME    = 030000;   //30 segundos
    private static final int MIN_WAIT               = 010000;   //10segunbdos

    public static SpeedController newInstance(VCLocClientService vcLocClientService){
        SpeedController timer = new SpeedController();
        timer.service = vcLocClientService;
        return timer;
    }

    public int  getWaitTime(){
        return WAIT_TIME;
    }

    private void setWaitTime(int newTime){
        WAIT_TIME = newTime;
        service.onServiceSpeedChanged(this,newTime);
    }

    public SpeedController() {
        WAIT_TIME = REFERENCE_WAIT_TIME;
    }

    public void setReferenceSpeed(){
        setWaitTime( REFERENCE_WAIT_TIME );
    }
    public void serviceMaxSpeed() {
        setWaitTime(MIN_WAIT);
    }

    public void serviceSpeedUp(){
        setWaitTime(Math.max(WAIT_TIME/2, MIN_WAIT  ));
    }
    public void serviceSpeedDown(){
        setWaitTime( Math.min(WAIT_TIME*2,MAX_WAIT ) );
    }
    public void serviceMinSpeed() {
        setWaitTime( MAX_WAIT );
    }
}
