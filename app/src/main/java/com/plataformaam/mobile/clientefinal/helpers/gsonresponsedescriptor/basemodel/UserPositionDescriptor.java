package com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.basemodel;


/**
 * Created by bernard on 31/01/2015.
 */
public class UserPositionDescriptor<RESPONSE> {
    int totalCount;
    RESPONSE userPosition;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public RESPONSE getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(RESPONSE userPosition) {
        this.userPosition = userPosition;
    }

    public UserPositionDescriptor() {
    }

    public UserPositionDescriptor(int totalCount, RESPONSE userPosition) {
        this.totalCount = totalCount;
        this.userPosition = userPosition;
    }


}
