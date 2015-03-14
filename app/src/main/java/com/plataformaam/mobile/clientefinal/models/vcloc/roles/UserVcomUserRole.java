package com.plataformaam.mobile.clientefinal.models.vcloc.roles;

import com.plataformaam.mobile.clientefinal.models.User;

import java.io.Serializable;

/**
 * Created by bernard on 31/01/2015.
 */
public class UserVcomUserRole implements Serializable {
    User user;
    VComUserRole vComUserRole;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VComUserRole getvComUserRole() {
        return vComUserRole;
    }

    public void setvComUserRole(VComUserRole vComUserRole) {
        this.vComUserRole = vComUserRole;
    }

    public UserVcomUserRole(User user, VComUserRole vComUserRole) {
        this.user = user;
        this.vComUserRole = vComUserRole;
    }

    public UserVcomUserRole() {
    }

    @Override
    public String toString() {
        return "{" +
                "user=" + user +
                ", vComUserRole=" + vComUserRole +
                '}';
    }
}
