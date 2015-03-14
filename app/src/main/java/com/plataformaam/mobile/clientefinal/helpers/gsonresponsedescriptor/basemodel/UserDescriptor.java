package com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.basemodel;

import com.plataformaam.mobile.clientefinal.models.User;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class UserDescriptor {
    List<User> user;
    int totalCount;

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public UserDescriptor(List<User> user, int totalCount) {
        this.user = user;
        this.totalCount = totalCount;
    }

    public UserDescriptor() {
    }


}
