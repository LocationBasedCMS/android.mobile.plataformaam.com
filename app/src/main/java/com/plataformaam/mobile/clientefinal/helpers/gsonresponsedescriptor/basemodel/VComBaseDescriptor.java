package com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.basemodel;

import com.plataformaam.mobile.clientefinal.models.vcloc.VComBase;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class VComBaseDescriptor {
    int totalCount;
    List<VComBase> vComBase;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<VComBase> getvComBase() {
        return vComBase;
    }

    public void setvComBase(List<VComBase> vComBase) {
        this.vComBase = vComBase;
    }

    public VComBaseDescriptor(int totalCount, List<VComBase> vComBase) {
        this.totalCount = totalCount;
        this.vComBase = vComBase;
    }

    public VComBaseDescriptor() {
    }
}
