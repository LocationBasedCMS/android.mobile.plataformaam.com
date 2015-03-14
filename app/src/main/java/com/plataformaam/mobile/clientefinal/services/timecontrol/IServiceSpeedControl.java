package com.plataformaam.mobile.clientefinal.services.timecontrol;

/**
 * Created by bernard on 17/02/2015.
 */
public interface IServiceSpeedControl {
    public int  getWaitTime();
    public IServiceSpeedController getSpeedController();
    public void onServiceSpeedChanged(IServiceSpeedController iServiceSpeedController,int newSpeed);

}
