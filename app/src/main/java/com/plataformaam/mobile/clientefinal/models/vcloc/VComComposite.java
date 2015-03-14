package com.plataformaam.mobile.clientefinal.models.vcloc;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.location.VirtualSpace;
import com.plataformaam.mobile.clientefinal.models.vcloc.roles.VComUserRole;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */

public class VComComposite implements Serializable{
    //VCom Corretamente Carregado de forma recursiva.
    private boolean ready;

    int id;
    String
            name,
            description;
    @SerializedName("vcomcomposite0")
    VComComposite vComComposite;
    @SerializedName("virtualspace0")
    VirtualSpace virtualSpace;
    @SerializedName("createuser")
    User createUser;

    @SerializedName("vComUserRoles")
    List<VComUserRole> userRoles;

    List<VComBase> vComBases;
    List<VComComposite> vComComposites;

    public List<VComBase> getvComBases() {
        return vComBases;
    }

    public void setvComBases(List<VComBase> vComBases) {
        this.vComBases = vComBases;
    }

    public List<VComComposite> getvComComposites() {
        return vComComposites;
    }

    public void setvComComposites(List<VComComposite> vComComposites) {
        this.vComComposites = vComComposites;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VComComposite getvComComposite() {
        return vComComposite;
    }

    public void setvComComposite(VComComposite vComComposite) {
        this.vComComposite = vComComposite;
    }

    public VirtualSpace getVirtualSpace() {
        return virtualSpace;
    }

    public void setVirtualSpace(VirtualSpace virtualSpace) {
        this.virtualSpace = virtualSpace;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public List<VComUserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<VComUserRole> userRoles) {
        this.userRoles = userRoles;
    }


    public VComComposite(int id, String name, String description, VComComposite vComComposite, VirtualSpace virtualSpace, User createUser) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.vComComposite = vComComposite;
        this.virtualSpace = virtualSpace;
        this.createUser = createUser;
    }



    public VComComposite() {
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", vComComposite=" + vComComposite +
                ", virtualSpace=" + virtualSpace +
                ", createUser=" + createUser +
                '}';
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isInside(LatLng location){
        boolean test = virtualSpace.isInside(location);
        /*
        if( ready ) {
            for (VComComposite sonComposite : vComComposites) {
                test = test || sonComposite.isInside(location);
            }
            for (VComBase base : vComBases) {
                test = test || base.isInside(location);
            }
        } else{
            Log.w(VComComposite.class.getSimpleName(),"isInside(Location location) : VComComposite is not complete / ignored  ");
        }
        */
        return test;
    }

    public boolean isInside(Location location){
        boolean test = virtualSpace.isInside(location);
        if( ready ) {
            for (VComComposite sonComposite : vComComposites) {
                test = test || sonComposite.isInside(location);
            }
            for (VComBase base : vComBases) {
                test = test || base.isInside(location);
            }
        } else{
            Log.w(VComComposite.class.getSimpleName(),"isInside(Location location) : VComComposite is not complete / ignored  ");
        }
        return test;
    }

    public boolean hasRole(VComUserRole testRole){
        boolean result = false;
        for(VComUserRole role : userRoles){
            result = result || ( role.getId() == testRole.getId() );
        }
        return result;
    }

    public boolean hasRole(User user) {
        boolean test  = false;
        if( user != null ){
            List<VComUserRole> roles = user.getRoles();
            if( roles != null){
                for(VComUserRole role:roles){
                    test = test || hasRole(role);
                }
            }
        }
        return test;
    }
}
