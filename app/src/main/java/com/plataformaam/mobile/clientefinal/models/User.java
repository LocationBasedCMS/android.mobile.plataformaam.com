package com.plataformaam.mobile.clientefinal.models;

import com.google.gson.annotations.SerializedName;
import com.plataformaam.mobile.clientefinal.models.location.UserPosition;
import com.plataformaam.mobile.clientefinal.models.vcloc.roles.VComUserRole;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bernard on 04/01/2015.
 */
public class User implements Serializable {

    private int id;
    private String name;
    private String login;
    private String password;
    private String email;

    //VCOMS RULES
    @SerializedName("vComUserRoles")
    List<VComUserRole> roles;
    @SerializedName("uPIs")
    List<UPI> upis;
    @SerializedName("vComUPIPublications")
    List<VComUPIPublication> publications;
    @SerializedName("userPositions")
    List<UserPosition> userPositions;


    public User() {
        super();
    }






    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }


    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }


    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }


    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public User(int id, String name, String login, String password, String email, List<VComUserRole> Roles) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
        this.roles = Roles;
    }

    public List<VComUserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<VComUserRole> roles) {
        this.roles = roles;
    }

    public List<VComUPIPublication> getPublications() {
        return publications;
    }

    public void setPublications(List<VComUPIPublication> publications) {
        this.publications = publications;
    }

    public List<UPI> getUpis() {
        return upis;
    }

    public void setUpis(List<UPI> upis) {
        this.upis = upis;
    }

    public List<UserPosition> getUserPositions() {
        return userPositions;
    }

    public void setUserPositions(List<UserPosition> userPositions) {
        this.userPositions = userPositions;
    }

    public User(int id, String name, String login, String password, String email) {
        super();
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
    }

    @Override
    public String toString() {
        return "{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
