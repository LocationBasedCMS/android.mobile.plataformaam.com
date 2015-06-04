package com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.basemodel;

import com.plataformaam.mobile.clientefinal.models.User;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class UserDescriptor<RESPONSE> {
    RESPONSE user;
    int totalCount;

    public RESPONSE getUser() {
        return user;
    }

    public void setUser(RESPONSE user) {
        this.user = user;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public UserDescriptor() {
    }


}
