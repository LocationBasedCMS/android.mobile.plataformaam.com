package com.plataformaam.mobile.clientefinal.services.statecontrol;

import com.plataformaam.mobile.clientefinal.exceptions.InvalidServiceStateException;

/**
 * Created by bernard on 17/02/2015.
 */
public class ServiceStateController implements IServiceStateController {
    public IServiceStateControl service;
    public int SERVICE_STATE;

    public static final int CREATED                    = 0;
    public static final int STARTED                    = 1;
    public static final int LOGGED_USER                = 2;
    public static final int INSIDE_VCLOC               = 3;
    public static final int INSIDE_INTERACTION_AREA    = 4;

    public ServiceStateController() {
        SERVICE_STATE = CREATED;
    }

    public static IServiceStateController newInstance(IServiceStateControl service){
        ServiceStateController controller = new ServiceStateController();
        controller.service = service;
        return controller;
    }

    public boolean isState(int TestState){
        return SERVICE_STATE == TestState;
    }

    @Override
    public int getState() {
        return SERVICE_STATE;
    }

    @Override
    public void setState(int newState) throws InvalidServiceStateException {
        if( SERVICE_STATE != newState ) {
            //REGRAS DOS ESTADOS
            if (SERVICE_STATE == CREATED && newState != STARTED) {
                throw new InvalidServiceStateException(" SERVICE_STATE : " + SERVICE_STATE + " to " + newState);
            }
            if (SERVICE_STATE == STARTED && newState != LOGGED_USER) {
                throw new InvalidServiceStateException(" SERVICE_STATE : " + SERVICE_STATE + " to " + newState);
            }
            if (newState > 4 || newState < 0) {
                throw new InvalidServiceStateException(" SERVICE_STATE desconhecido : " + SERVICE_STATE);
            }
            SERVICE_STATE = newState;
            service.onStateChanged(this,newState);
        }

    }
}
