package com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.basemodel;

import com.plataformaam.mobile.clientefinal.models.location.UPIPosition;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class UPIPositionDescriptor {
    List<UPIPosition> uPIPosition;
    int totalCount;

    public List<UPIPosition> getuPIPosition() {
        return uPIPosition;
    }

    public void setuPIPosition(List<UPIPosition> uPIPosition) {
        this.uPIPosition = uPIPosition;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public UPIPositionDescriptor(List<UPIPosition> uPIPosition, int totalCount) {
        this.uPIPosition = uPIPosition;
        this.totalCount = totalCount;
    }

    public UPIPositionDescriptor() {
    }
}
