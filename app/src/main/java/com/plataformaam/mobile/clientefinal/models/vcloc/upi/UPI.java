package com.plataformaam.mobile.clientefinal.models.vcloc.upi;

import com.google.gson.annotations.SerializedName;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfig;
import com.plataformaam.mobile.clientefinal.helpers.volley.IObjectToPost;
import com.plataformaam.mobile.clientefinal.models.User;

import java.io.Serializable;

/**
 * Created by bernard on 11/01/2015.
 */
public class UPI  implements Serializable,IObjectToPost {
    //PK
    int id;
    //FK
    @SerializedName("upitype0")
    UPIType upitype;
    @SerializedName("user0")
    User user;
    //INFORMATION
    String title;
    String description;
    String content;

    public UPIType getUpiType() {
        return upitype;
    }

    public void setUpiType(int upiTypeID) {
        UPIType type  = MyAppConfig.generateUpiType(upiTypeID);
        this.upitype = type;
    }


    public void setUpiType(UPIType upiType) {
        this.upitype = upiType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", upitype=" + upitype.getId() +
                ", user=" + user.getId() +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                '}';
    }


    @Override
    public String generatePostJson() {
        return "{" +
                " upitype:" + upitype.getId() +
                ", user:" + user.getId() +
                ", title:'" + title + '\'' +
                ", description:'" + description + '\'' +
                ", content:'" + content + '\'' +
                '}';

    }
}
