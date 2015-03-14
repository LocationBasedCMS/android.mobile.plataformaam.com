package com.plataformaam.mobile.clientefinal.services.vcomcontrol;

import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;
import com.plataformaam.mobile.clientefinal.services.usercontrol.IUserController;

import java.util.Map;

/**
 * Created by bernard on 18/02/2015.
 */
public interface IVComControl {

    public IVComController getVComController();
    public void loadAllVCom();
    public void setAllVCom(Map<Integer, VComComposite> composites);
    public void onVcomLoad(Map<Integer, VComComposite> composites, IVComController ivComController);

    public void onUserVComConnect(VComComposite composite,IUserController userController,IVComController ivComController);
    public void onUserVComDisconnect(VComComposite composite,IUserController userController,IVComController ivComController);
    public void onUserPublish( VComUPIPublication publication  , VComComposite composite, IUserController userController );

}
