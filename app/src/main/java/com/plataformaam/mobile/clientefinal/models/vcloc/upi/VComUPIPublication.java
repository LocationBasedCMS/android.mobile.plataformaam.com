package com.plataformaam.mobile.clientefinal.models.vcloc.upi;

import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComBase;
import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleResponseOf;
import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleStart;

/**
 * Created by bernard on 31/01/2015.
 */
public class VComUPIPublication {
    int id;
    User user;
    VComBase vComBase;
    UPI upi;
    UPIAggregationRuleStart upiAggregationRuleStart;
    UPIAggregationRuleResponseOf aggregationRuleResponseOf;
    double
            latitude,
            longitude;


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

    public UPIAggregationRuleStart getUpiAggregationRuleStart() {
        return upiAggregationRuleStart;
    }

    public void setUpiAggregationRuleStart(UPIAggregationRuleStart upiAggregationRuleStart) {
        this.upiAggregationRuleStart = upiAggregationRuleStart;
    }

    public UPIAggregationRuleResponseOf getAggregationRuleResponseOf() {
        return aggregationRuleResponseOf;
    }

    public void setAggregationRuleResponseOf(UPIAggregationRuleResponseOf aggregationRuleResponseOf) {
        this.aggregationRuleResponseOf = aggregationRuleResponseOf;
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

    public VComUPIPublication() {
    }

    public VComUPIPublication(int id, User user, VComBase vComBase, UPI upi, UPIAggregationRuleStart upiAggregationRuleStart, UPIAggregationRuleResponseOf aggregationRuleResponseOf, double latitude, double longitude) {
        this.id = id;
        this.user = user;
        this.vComBase = vComBase;
        this.upi = upi;
        this.upiAggregationRuleStart = upiAggregationRuleStart;
        this.aggregationRuleResponseOf = aggregationRuleResponseOf;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public VComUPIPublication(int id, User user, VComBase vComBase, UPI upi, UPIAggregationRuleStart upiAggregationRuleStart, double latitude, double longitude) {
        this.id = id;
        this.user = user;
        this.vComBase = vComBase;
        this.upi = upi;
        this.upiAggregationRuleStart = upiAggregationRuleStart;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public VComUPIPublication(int id, User user, VComBase vComBase, UPI upi, double latitude, double longitude, UPIAggregationRuleResponseOf aggregationRuleResponseOf) {
        this.id = id;
        this.user = user;
        this.vComBase = vComBase;
        this.upi = upi;
        this.latitude = latitude;
        this.longitude = longitude;
        this.aggregationRuleResponseOf = aggregationRuleResponseOf;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", user=" + user +
                ", vComBase=" + vComBase +
                ", upi=" + upi +
                ", upiAggregationRuleStart=" + upiAggregationRuleStart +
                ", aggregationRuleResponseOf=" + aggregationRuleResponseOf +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
