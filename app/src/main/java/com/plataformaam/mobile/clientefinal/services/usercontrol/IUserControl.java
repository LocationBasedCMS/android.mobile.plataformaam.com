package com.plataformaam.mobile.clientefinal.services.usercontrol;

import com.plataformaam.mobile.clientefinal.exceptions.InvalidServiceStateException;
import com.plataformaam.mobile.clientefinal.exceptions.UserNotAllowedException;
import com.plataformaam.mobile.clientefinal.models.User;

/**
 * Created by bernard on 17/02/2015.
 */
public interface IUserControl {
    public User getUser();
    public IUserController getUserController();

    public boolean doLogin(User user)throws InvalidServiceStateException, UserNotAllowedException;

    public void onUserLogin(IUserController controller,User user);
    public void onUserLogout(IUserController controller,User user);
    public void onUserChanged(IUserController controller,User user,Class aClass);

}
