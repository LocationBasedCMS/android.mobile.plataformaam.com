package com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.basemodel;

import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class VComUPIPublicationDescriptor {
    int totalCount;
    List<VComUPIPublication> vComUPIPublication;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<VComUPIPublication> getvComUPIPublication() {
        return vComUPIPublication;
    }

    public void setvComUPIPublication(List<VComUPIPublication> vComUPIPublication) {
        this.vComUPIPublication = vComUPIPublication;
    }

    public VComUPIPublicationDescriptor(int totalCount, List<VComUPIPublication> vComUPIPublication) {
        this.totalCount = totalCount;
        this.vComUPIPublication = vComUPIPublication;
    }

    public VComUPIPublicationDescriptor() {
    }
}
