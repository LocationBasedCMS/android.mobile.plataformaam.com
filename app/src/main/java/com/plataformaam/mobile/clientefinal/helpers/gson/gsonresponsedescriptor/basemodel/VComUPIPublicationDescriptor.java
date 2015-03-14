package com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.basemodel;

import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class VComUPIPublicationDescriptor<RESPONSE> {
    int totalCount;
    RESPONSE vComUPIPublication;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public RESPONSE getvComUPIPublication() {
        return vComUPIPublication;
    }

    public void setvComUPIPublication(RESPONSE vComUPIPublication) {
        this.vComUPIPublication = vComUPIPublication;
    }

    public VComUPIPublicationDescriptor() {
    }

    public VComUPIPublicationDescriptor(int totalCount, RESPONSE vComUPIPublication) {
        this.totalCount = totalCount;
        this.vComUPIPublication = vComUPIPublication;
    }

    @Override
    public String toString() {
        return "{" +
                "totalCount=" + totalCount +
                ", vComUPIPublication=" + vComUPIPublication +
                '}';
    }
}
