package com.plataformaam.mobile.clientefinal.services.statecontrol;

import com.plataformaam.mobile.clientefinal.exceptions.InvalidServiceStateException;

/**
 * Created by bernard on 17/02/2015.
 */
public interface IServiceStateController {
    public int getState();
    public void setState(int newState) throws InvalidServiceStateException;
    public boolean isState(int TestState);
}
