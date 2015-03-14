package com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.basemodel;

import com.plataformaam.mobile.clientefinal.models.vcloc.roles.VComUserRole;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class VComUserRoleDescriptor {
    int totalCount;
    List<VComUserRole> vComUserRole;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<VComUserRole> getvComUserRole() {
        return vComUserRole;
    }

    public void setvComUserRole(List<VComUserRole> vComUserRole) {
        this.vComUserRole = vComUserRole;
    }

    public VComUserRoleDescriptor(int totalCount, List<VComUserRole> vComUserRole) {
        this.totalCount = totalCount;
        this.vComUserRole = vComUserRole;
    }

    public VComUserRoleDescriptor() {
    }
}
