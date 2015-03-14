package com.plataformaam.mobile.clientefinal.models.location;


import com.google.gson.annotations.SerializedName;
import com.plataformaam.mobile.clientefinal.exceptions.InvalidCoordinatesException;
import com.plataformaam.mobile.clientefinal.models.User;

import java.util.Date;

/**
 * Created by bernard on 04/01/2015.
 */

public class UserPosition {
    Integer id;


    @SerializedName("user0")
    User user;

    Date currentTime;
    String content;

    double latitude;
    double longitude;

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

    @Override
    public String toString() {
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String formatedCurrentTime = sdf.format(currentTime);

        return "{" +
                "longitude:" + longitude +
                ", latitude:" + latitude +
                ", content:'" + content + '\'' +
                ", currentTime:'" + formatedCurrentTime + '\'' +
                ", user:" + user.getId() +
                ", id:" + id +
                '}';
    }


}
