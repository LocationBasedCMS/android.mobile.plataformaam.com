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
public interface IVComController {


    public IVComControl getService();


    //CONEXÃO USUÁRIO VCOM
    public void connectVComComposite(IVComController controller, IUserController userController, VComComposite composite); //Conecta o Serviço/Usuário ao VCom
    public void disconnectVComComposite( IUserControl service,VComComposite composite ); //Conecta o Serviço/Usuário ao VCom
    public VComComposite getConnectedVComComposite();


    //Usuários onlines do VComComposite
    public List<User> getOnlineUser(IVComController controller);
    public void setOnlineUser(IVComController controller , User u);


    //Auxiliáres
    public Map<Integer, VComComposite> getAllVCom();    //Todos os VComs Carregados
    public void setAllVComComposite(Map<Integer,VComComposite> map);


}
