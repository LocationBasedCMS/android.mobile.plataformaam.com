package com.plataformaam.mobile.clientefinal.services.usercontrol;

import com.plataformaam.mobile.clientefinal.exceptions.InvalidServiceStateException;
import com.plataformaam.mobile.clientefinal.exceptions.UserNotAllowedException;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.location.UserPosition;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.models.vcloc.roles.VComUserRole;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;

import java.util.List;
import java.util.Map;

/**
 * Created by bernard on 17/02/2015.
 */
public interface IUserController {

    public User getUser();
    public void setUser(User user) throws UserNotAllowedException;
    public void unSetUser();


    public void updateUpis(List<UPI> list);
    public void updateUserPositions(List<UserPosition> list);
    public void updateRoles(List<VComUserRole> list);
    public void updatePublication(List<VComUPIPublication> list);

    //Métodos de Auxílio
    public Map<Integer,VComComposite> getMyVComComposite(Map<Integer,VComComposite> allVComComposite);
    public UserPosition getLastPosition();
    public void setLastPosition(UserPosition lastPosition);

}
