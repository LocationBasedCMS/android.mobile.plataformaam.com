package com.plataformaam.mobile.clientefinal.services.usercontrol;

import android.util.Log;

import com.plataformaam.mobile.clientefinal.exceptions.UserNotAllowedException;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.location.UserPosition;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.models.vcloc.roles.VComUserRole;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by bernard on 17/02/2015.
 */
public class UserController implements IUserController {
    private static UserController controller;
    IUserControl mService;
    private static User mUser;
    //ONLINE USER DATA
    private static UserPosition lastPosition;

    public UserController(IUserControl service) {
        this.mService = service;
    }

    public static IUserController getInstance(IUserControl service){
        if( controller == null ){
            controller = new UserController(service);
        }
        controller.mService = service;
        return controller;
    }

    @Override
    public User getUser() {
        return mUser;
    }

    @Override
    public void setUser(User user) throws UserNotAllowedException {
        //DATABASE ID TEST
        if( user.getId() >= 0 ){
            mUser = user;
            if( user.getRoles() == null ){
                mUser.setRoles(new ArrayList<VComUserRole>());
            }
            if( mService != null ) {
                mService.onUserLogin(this, mUser);
            }
        }else{
            throw new UserNotAllowedException("MyAppConfiguration - User ID < 0 ");
        }
    }

    @Override
    public void unSetUser() {
        User user = mUser;
        mUser =null;
        if( mService != null ){
            mService.onUserLogout(this,user);
        }
    }


    @Override
    public void updateUpis(List<UPI> list) {
        mUser.setUpis(list);
        mService.onUserChanged(this,mUser,UPI.class);
    }

    @Override
    public void updateUserPositions(List<UserPosition> list) {
        mUser.setUserPositions(list);
        mService.onUserChanged(this,mUser,UserPosition.class);
    }

    @Override
    public void updateRoles(List<VComUserRole> list) {
        mUser.setRoles(list);
        mService.onUserChanged(this,mUser,VComUserRole.class);
    }

    @Override
    public void updatePublication(List<VComUPIPublication> list) {
        mUser.setPublications(list);
        mService.onUserChanged(this,mUser,VComUPIPublication.class);
    }


    @Override
    public Map<Integer, VComComposite> getMyVComComposite(Map<Integer, VComComposite> allVComComposite) {
        Map<Integer, VComComposite>  myVComComposite = new HashMap<Integer,VComComposite>();
        if( mUser != null ){
            myVComComposite = new HashMap<Integer,VComComposite>();
            List<VComUserRole> userRoles = mUser.getRoles();

            Iterator<Integer> itr = allVComComposite.keySet().iterator();
            while (itr.hasNext()) {
                VComComposite currentVComComposite = allVComComposite.get(itr.next());
                List<VComUserRole> allRoles = currentVComComposite.getUserRoles();
                for( VComUserRole role_to_test : allRoles){
                    for( VComUserRole user_role : userRoles) {

                        if( role_to_test.getId() == user_role.getId() ){
                            //Usuário possui acesso ao VCLoc + Adciona uma vez
                            if( myVComComposite.get(currentVComComposite.getId()) == null ){
                                myVComComposite.put(currentVComComposite.getId(),currentVComComposite);
                            }
                        }
                    }
                }
            }
            return myVComComposite;
        }
        return new HashMap<Integer,VComComposite>();

    }

    @Override
    public UserPosition getLastPosition() {
        if( lastPosition == null ){
            List<UserPosition> positions = mUser.getUserPositions();
            if( positions.size() >0 ){
                lastPosition = positions.get(0);
            }
            for(UserPosition p : positions ){
                try {
                    Date d = p.getCurrentTime();
                    //(evita) disparar exceção para comer recurso.
                    if( d!=null) {
                        if ( lastPosition.getCurrentTime().compareTo(d)  > 0 ) {
                            lastPosition = p;
                        }
                    }
                }catch (NullPointerException e){
                    Log.w(UserController.class.getSimpleName(),"Alerta: Data nula em UserPositions ! ");
                }
            }
        }
        return lastPosition;
    }

    @Override
    public void setLastPosition(UserPosition newLastPosition) {
        newLastPosition.setUser(mUser);
        lastPosition = newLastPosition;
        mUser.getUserPositions().add(lastPosition);
    }
}
