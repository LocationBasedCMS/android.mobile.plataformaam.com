package com.plataformaam.mobile.clientefinal.services.vcomcontrol;

import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.services.usercontrol.IUserControl;
import com.plataformaam.mobile.clientefinal.services.usercontrol.IUserController;

import java.util.List;
import java.util.Map;

/**
 * Created by bernard on 18/02/2015.
 */
public class VComController implements IVComController {
    private static VComController controller;
    IVComControl service;


    public static VComController getInstance(IVComControl service){
        if( controller == null ) {
            controller = new VComController();
        }
        controller.service = service;
        return controller;
    }

    static Map<Integer,VComComposite> allVComComposite;
    static VComComposite onlineVCom;


    public VComController() {

    }

    @Override
    public IVComControl getService() {
        return service;
    }


    @Override
    public void connectVComComposite(IVComController controller, IUserController userController, VComComposite composite) {

        onlineVCom = composite;
        service.onUserVComConnect(composite, userController, this);

    }

    @Override
    public void disconnectVComComposite(IUserControl service, VComComposite composite) {
        if( composite.getId() == onlineVCom.getId() ){
            onlineVCom = null;
            this.service.onUserVComDisconnect(composite, service.getUserController(), this);
        } else{
            this.service.onUserVComDisconnect(null, service.getUserController(), this);
        }
    }

    @Override
    public VComComposite getConnectedVComComposite() {
        return onlineVCom;
    }

    @Override
    public List<User> getOnlineUser(IVComController controller) {
        return null;
    }

    @Override
    public void setOnlineUser(IVComController controller, User u) {

    }

    @Override
    public Map<Integer, VComComposite> getAllVCom() {
        return allVComComposite;
    }


    @Override
    public void setAllVComComposite(Map<Integer, VComComposite> map) {
        allVComComposite = map;
        service.onVcomLoad(map,this);
    }
}
