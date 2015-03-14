package com.plataformaam.mobile.clientefinal.models.vcloc.rules;

import com.google.gson.annotations.SerializedName;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComBase;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPIType;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class UPIAggregationRuleStart {
    int id;

    @SerializedName("vcombase0")
    VComBase vComBase;

    @SerializedName("upitype0")
    UPIType upiType;

    String
            name,
            description;

    boolean
            requirePositionToCreate,
            requirePositionToView,
            republisAllowed,
            isSingleton;
    int 	visibleDistance;

    @SerializedName("uPIAggregationRuleResponseOfs")
    List<UPIAggregationRuleResponseOf> responseRules;

    @SerializedName("vComUPIPublications")
    List<VComUPIPublication> publications;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VComBase getvComBase() {
        return vComBase;
    }

    public void setvComBase(VComBase vComBase) {
        this.vComBase = vComBase;
    }

    public UPIType getUpiType() {
        return upiType;
    }

    public void setUpiType(UPIType upiType) {
        this.upiType = upiType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRequirePositionToCreate() {
        return requirePositionToCreate;
    }

    public void setRequirePositionToCreate(boolean requirePositionToCreate) {
        this.requirePositionToCreate = requirePositionToCreate;
    }

    public boolean isRequirePositionToView() {
        return requirePositionToView;
    }

    public void setRequirePositionToView(boolean requirePositionToView) {
        this.requirePositionToView = requirePositionToView;
    }

    public boolean isRepublisAllowed() {
        return republisAllowed;
    }

    public void setRepublisAllowed(boolean republisAllowed) {
        this.republisAllowed = republisAllowed;
    }

    public boolean isSingleton() {
        return isSingleton;
    }

    public void setSingleton(boolean isSingleton) {
        this.isSingleton = isSingleton;
    }

    public int getVisibleDistance() {
        return visibleDistance;
    }

    public void setVisibleDistance(int visibleDistance) {
        this.visibleDistance = visibleDistance;
    }

    public UPIAggregationRuleStart(int id, VComBase vComBase, UPIType upiType, String name, int visibleDistance, String description) {
        this.id = id;
        this.vComBase = vComBase;
        this.upiType = upiType;
        this.name = name;
        this.visibleDistance = visibleDistance;
        this.description = description;
    }

    public UPIAggregationRuleStart(int id, VComBase vComBase, UPIType upiType, int visibleDistance) {
        this.id = id;
        this.vComBase = vComBase;
        this.upiType = upiType;
        this.visibleDistance = visibleDistance;
    }

    public UPIAggregationRuleStart(int id, VComBase vComBase, UPIType upiType, String name, String description, boolean requirePositionToCreate, boolean requirePositionToView, boolean republisAllowed, boolean isSingleton, int visibleDistance) {
        this.id = id;
        this.vComBase = vComBase;
        this.upiType = upiType;
        this.name = name;
        this.description = description;
        this.requirePositionToCreate = requirePositionToCreate;
        this.requirePositionToView = requirePositionToView;
        this.republisAllowed = republisAllowed;
        this.isSingleton = isSingleton;
        this.visibleDistance = visibleDistance;
    }

    public UPIAggregationRuleStart() {
    }





    public List<UPIAggregationRuleResponseOf> getResponseRules() {
        return responseRules;
    }

    public void setResponseRules(List<UPIAggregationRuleResponseOf> responseRules) {
        this.responseRules = responseRules;
    }

    public List<VComUPIPublication> getPublications() {
        return publications;
    }

    public void setPublications(List<VComUPIPublication> publications) {
        this.publications = publications;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", vComBase=" + vComBase +
                ", upiType=" + upiType +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", requirePositionToCreate=" + requirePositionToCreate +
                ", requirePositionToView=" + requirePositionToView +
                ", republisAllowed=" + republisAllowed +
                ", isSingleton=" + isSingleton +
                ", visibleDistance=" + visibleDistance +
                '}';
    }



}
