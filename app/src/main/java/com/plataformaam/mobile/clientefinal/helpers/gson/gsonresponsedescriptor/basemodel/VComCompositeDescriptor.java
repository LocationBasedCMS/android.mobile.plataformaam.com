package com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.basemodel;

import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class VComCompositeDescriptor {
    int totalCount;
    List<VComComposite> vComComposite;


    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<VComComposite> getvComComposite() {
        return vComComposite;
    }

    public void setvComComposite(List<VComComposite> vComComposite) {
        this.vComComposite = vComComposite;
    }

    public VComCompositeDescriptor(int totalCount, List<VComComposite> vComComposite) {
        this.totalCount = totalCount;
        this.vComComposite = vComComposite;
    }

    public VComCompositeDescriptor() {
    }


}
