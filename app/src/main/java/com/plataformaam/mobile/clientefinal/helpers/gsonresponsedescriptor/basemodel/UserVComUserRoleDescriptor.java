package com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.basemodel;

import com.plataformaam.mobile.clientefinal.models.vcloc.roles.UserVcomUserRole;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class UserVComUserRoleDescriptor {
    int totalCount;
    List<UserVcomUserRole> userVcomUserRoles;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<UserVcomUserRole> getUserVcomUserRoles() {
        return userVcomUserRoles;
    }

    public void setUserVcomUserRoles(List<UserVcomUserRole> userVcomUserRoles) {
        this.userVcomUserRoles = userVcomUserRoles;
    }

    public UserVComUserRoleDescriptor(int totalCount, List<UserVcomUserRole> userVcomUserRoles) {
        this.totalCount = totalCount;
        this.userVcomUserRoles = userVcomUserRoles;
    }

    public UserVComUserRoleDescriptor() {
    }
}
