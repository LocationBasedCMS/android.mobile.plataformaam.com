package com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.basemodel;

/**
 * Created by bernard on 31/01/2015.
 */
public class UPIPositionDescriptor<RESPONSE> {
    RESPONSE uPIPosition;
    int totalCount;

    public RESPONSE getuPIPosition() {
        return uPIPosition;
    }

    public void setuPIPosition(RESPONSE uPIPosition) {
        this.uPIPosition = uPIPosition;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public UPIPositionDescriptor(RESPONSE uPIPosition, int totalCount) {
        this.uPIPosition = uPIPosition;
        this.totalCount = totalCount;
    }

    public UPIPositionDescriptor() {
    }
}
