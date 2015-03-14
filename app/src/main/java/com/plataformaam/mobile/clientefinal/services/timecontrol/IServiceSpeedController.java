package com.plataformaam.mobile.clientefinal.services.timecontrol;

/**
 * Created by bernard on 17/02/2015.
 */
public interface IServiceSpeedController {

    public void setReferenceSpeed();
    public void serviceMaxSpeed();
    public void serviceSpeedUp();
    public void serviceSpeedDown();
    public int  getWaitTime();
    public void serviceMinSpeed() ;
}
