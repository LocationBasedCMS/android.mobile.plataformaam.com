package com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.basemodel;

import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class UPIDescriptor {

    int totalCount;
    List<UPI> uPI;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<UPI> getuPI() {
        return uPI;
    }

    public void setuPI(List<UPI> uPI) {
        this.uPI = uPI;
    }

    public UPIDescriptor(int totalCount, List<UPI> uPI) {
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
