package com.plataformaam.mobile.clientefinal.models.vcloc.upi;

import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfig;
import com.plataformaam.mobile.clientefinal.helpers.volley.IObjectToPost;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComBase;
import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleResponseOf;
import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleStart;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;


public class VComUPIPublication   implements Serializable,IObjectToPost {
    int id;
    @SerializedName("user0")
    User user;
    @SerializedName("vcombase0")
    VComBase vComBase;
    @SerializedName("upi0")
    UPI upi;
    @SerializedName("upiaggregationrulestart0")
    UPIAggregationRuleStart publishRule;
    @SerializedName("upiaggregationruleresponseof0")
    UPIAggregationRuleResponseOf responseRule;

    double
            latitude,
            longitude;



    Date currentTime;

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

    public VComBase getvComBase() {
        return vComBase;
    }

    public void setvComBase(VComBase vComBase) {
        this.vComBase = vComBase;
    }

    public UPI getUpi() {
        return upi;
    }

    public void setUpi(UPI upi) {
        this.upi = upi;
    }

    public UPIAggregationRuleStart getPublishRule() {
        return publishRule;
    }

    public void setPublishRule(UPIAggregationRuleStart publishRule) {
        this.publishRule = publishRule;
    }

    public UPIAggregationRuleResponseOf getResponseRule() {
        return responseRule;
    }

    public void setResponseRule(UPIAggregationRuleResponseOf responseRule) {
        this.responseRule = responseRule;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatLng(LatLng latLng){
        if( latLng != null ){
            this.latitude = latLng.latitude;
            this.longitude = latLng.longitude;
        }
    }

    public Date getCurrentTime() {
        if( currentTime == null ){
            Calendar c  = Calendar.getInstance();
            currentTime = c.getTime();
        }
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public VComUPIPublication() {
    }



    public VComUPIPublication(UPI upi, User user, VComBase vComBase,  UPIAggregationRuleStart publishRule, LatLng latLng ) {
        this.user = user;
        this.upi = upi;
        this.publishRule = publishRule;
        this.vComBase = vComBase;
        setLatLng(latLng);
    }

    public VComUPIPublication(UPI upi, User user, VComBase vComBase,  UPIAggregationRuleResponseOf responseRule, LatLng latLng ) {
        this.user = user;
        this.upi = upi;
        this.responseRule = responseRule;
        this.vComBase = vComBase;
        setLatLng(latLng);
    }

    public VComUPIPublication(int id, User user, VComBase vComBase, UPI upi, UPIAggregationRuleStart publishRule, UPIAggregationRuleResponseOf responseRule, double latitude, double longitude, Date currentTime) {
        this.id = id;
        this.user = user;
        this.vComBase = vComBase;
        this.upi = upi;
        this.publishRule = publishRule;
        this.responseRule = responseRule;
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentTime = currentTime;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", user=" + user +
                ", vComBase=" + vComBase +
                ", upi=" + upi +
                ", publishRule=" + publishRule +
                ", responseRule=" + responseRule +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public String generatePostJson() {
        String jsonToPost = null ;
        try {
            java.text.SimpleDateFormat sdf =  new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedCurrentTime = sdf.format(getCurrentTime());
            if (publishRule != null) {

                jsonToPost = "{" +
                        " user:" + user.getId() +
                        ", vcombase:" + vComBase.getId() +
                        ", upi:" + upi.getId() +
                        ", upiaggregationrulestart:" + publishRule.getId() +
                        ", latitude:" + latitude +
                        ", longitude:" + longitude +
                        ", currentTime:'" + formattedCurrentTime + '\'' +
                        '}';
            } else {
                jsonToPost = "{" +
                        " user:" + user.getId() +
                        ", vcombase:" + vComBase.getId() +
                        ", upi:" + upi.getId() +
                        ", upiaggregationruleresponseof:" + responseRule.getId() +
                        ", latitude:" + latitude +
                        ", longitude:" + longitude +
                        ", currentTime:'" + formattedCurrentTime + '\'' +
                        '}';
            }
        } catch (NullPointerException e){
            Log.i(MyAppConfig.LOG.Model,"VComUPIPublication: A construção do objeto de publicação não cumpre os requisitos estabelecidos pelo modelo. ");
            e.printStackTrace();
        } finally {
            return jsonToPost;
        }
    }
}
