package com.plataformaam.mobile.clientefinal.services.statecontrol;

/**
 * Created by bernard on 17/02/2015.
 */
public interface IServiceStateControl {
    public int      getState();
    public void     onStateChanged(IServiceStateController controller,int newState);
}
