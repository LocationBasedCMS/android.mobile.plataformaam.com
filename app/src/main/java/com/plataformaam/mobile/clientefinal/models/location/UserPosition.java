package com.plataformaam.mobile.clientefinal.models.location;


import com.google.gson.annotations.SerializedName;
import com.plataformaam.mobile.clientefinal.exceptions.InvalidCoordinatesException;
import com.plataformaam.mobile.clientefinal.helpers.volley.IObjectToPost;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComBase;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bernard on 04/01/2015.
 */

public class UserPosition implements Serializable, IObjectToPost{
    Integer id;


    @SerializedName("user0")
    User user;

    Date currentTime;
    String content;

    double latitude;
    double longitude;

    @SerializedName("vcomcomposite")
    Integer composite;
    @SerializedName("vcombase")
    Integer base;
    @SerializedName("upi")
    Integer upi;




    public UserPosition() {
    }

    public UserPosition(Integer id, User user, Date currentTime, String content, double latitude, double longitude) {
        this.id = id;
        this.user = user;
        this.currentTime = currentTime;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public UserPosition(User user, Date currentTime, double latitude, double longitude) {
        this.user = user;
        this.currentTime = currentTime;
        this.latitude = latitude;
        this.longitude = longitude;
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
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the currentTime
     */
    public Date getCurrentTime() {
        return currentTime;
    }

    /**
     * @param currentTime the currentTime to set
     */
    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) throws InvalidCoordinatesException{
        if( -180 <= latitude && latitude <= 180  ) {
            this.latitude = latitude;
        }else{
            this.latitude = 0;
            throw  new  InvalidCoordinatesException("UserPosition: Latitude Inválida ");
        }
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) throws InvalidCoordinatesException {
        if( -180 <= longitude && longitude <= 180  ) {
            this.longitude = longitude;
        }else{
            this.longitude = 0;
            throw  new InvalidCoordinatesException("UserPosition: Longitude Inválida " );
        }
    }

    public Integer getComposite() {
        return composite;
    }

    public void setComposite(Integer composite) {
        this.composite = composite;
    }

    public Integer getBase() {
        return base;
    }

    public void setBase(Integer base) {
        this.base = base;
    }

    public Integer getUpi() {
        return upi;
    }

    public void setUpi(Integer upi) {
        this.upi = upi;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String formattedCurrentTime = sdf.format(currentTime);
        String post_data ="{" +
                "longitude:" + longitude +
                ", latitude:" + latitude +
                ", content:'" + content + '\'' +
                ", currentTime:'" + formattedCurrentTime + '\'' +
                ", user:" + user.getId();

        if( composite != null ){
            post_data += ", vcomcomposite:" + composite.toString();
        }
        if( base != null ){
            post_data += ", vcombase:" + base.toString();
        }
        if( upi != null ){
            post_data += ", upi:" + upi.toString();
        }
        post_data +=", id:" + id +
                '}';
        return post_data;
    }

    @Override
    public String generatePostJson() {
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String formattedCurrentTime = sdf.format(currentTime);
        String post_data ="{" +
                "longitude:" + longitude +
                ", latitude:" + latitude +
                ", content:'" + content + '\'' +
                ", currentTime:'" + formattedCurrentTime + '\'' ;
        if( composite != null ){
            post_data += ", vcomcomposite:" + composite.toString();
        }
        if( base != null ){
            post_data += ", vcombase:" + base.toString();
        }
        if( upi != null ){
            post_data += ", upi:" + upi.toString();
        }
        post_data +=", user:" + user.getId() +
                '}';
        return post_data;


    }




}
