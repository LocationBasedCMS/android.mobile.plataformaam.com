package com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.basemodel;

/**
 * Created by bernard on 31/01/2015.
 */
public class UPIDescriptor<RESPONSE> {

    int totalCount;
    RESPONSE uPI;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public RESPONSE getuPI() {
        return uPI;
    }

    public void setuPI(RESPONSE uPI) {
        this.uPI = uPI;
    }

    public UPIDescriptor(int totalCount, RESPONSE uPI) {
        this.totalCount = totalCount;
        this.uPI = uPI;
    }

    public UPIDescriptor() {
    }

    @Override
    public String toString() {
        return "{" +
                "totalCount=" + totalCount +
                ", uPI=" + uPI +
                '}';
    }
}
