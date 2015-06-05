package com.plataformaam.mobile.clientefinal.models.vcloc.rules;

import com.google.gson.annotations.SerializedName;
import com.plataformaam.mobile.clientefinal.models.vcloc.roles.VComUPIAggregationRuleResponseOf;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPIType;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class UPIAggregationRuleResponseOf implements Serializable {
    int id;
    String name;
    boolean  requirePositionToResponse;
    boolean requirePositionToView;
    @SerializedName("republisAllowed")
    boolean republishAllowed;
    @SerializedName("aceptMultiple")
    boolean acceptMultiple;

    @SerializedName("upiaggregationrulestart0")
    UPIAggregationRuleStart upiAggregationRuleStart;
    @SerializedName("upitype0")
    UPIType upiType;
    @SerializedName("vComUPIAggregationRuleResponseOfs")
    List<VComUPIAggregationRuleResponseOf> roles;
    @SerializedName("vComUPIPublications")
    List<VComUPIPublication> publications;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UPIAggregationRuleStart getUpiAggregationRuleStart() {
        return upiAggregationRuleStart;
    }

    public void setUpiAggregationRuleStart(UPIAggregationRuleStart upiAggregationRuleStart) {
        this.upiAggregationRuleStart = upiAggregationRuleStart;
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

    public boolean isRequirePositionToResponse() {
        return requirePositionToResponse;
    }

    public void setRequirePositionToResponse(boolean requirePositionToResponse) {
        this.requirePositionToResponse = requirePositionToResponse;
    }

    public boolean isRequirePositionToView() {
        return requirePositionToView;
    }

    public void setRequirePositionToView(boolean requirePositionToView) {
        this.requirePositionToView = requirePositionToView;
    }

    public boolean isRepublishAllowed() {
        return republishAllowed;
    }

    public void setRepublishAllowed(boolean republishAllowed) {
        this.republishAllowed = republishAllowed;
    }

    public boolean isAceptMultiple() {
        return acceptMultiple;
    }

    public void setAceptMultiple(boolean acceptMultiple) {
        this.acceptMultiple = acceptMultiple;
    }

    public List<VComUPIPublication> getPublications() {
        return publications;
    }

    public void setPublications(List<VComUPIPublication> publications) {
        this.publications = publications;
    }

    public List<VComUPIAggregationRuleResponseOf> getRoles() {
        return roles;
    }

    public void setRoles(List<VComUPIAggregationRuleResponseOf> roles) {
        this.roles = roles;
    }

    public UPIAggregationRuleResponseOf() {
    }

    public UPIAggregationRuleResponseOf(int id, UPIAggregationRuleStart upiAggregationRuleStart, UPIType upiType, String name, boolean requirePositionToResponse, boolean requirePositionToView, boolean republishAllowed, boolean aceptMultiple) {
        this.id = id;
        this.upiAggregationRuleStart = upiAggregationRuleStart;
        this.upiType = upiType;
        this.name = name;
        this.requirePositionToResponse = requirePositionToResponse;
        this.requirePositionToView = requirePositionToView;
        this.republishAllowed = republishAllowed;
        this.acceptMultiple = aceptMultiple;
    }

    public UPIAggregationRuleResponseOf(int id, UPIAggregationRuleStart upiAggregationRuleStart, UPIType upiType, String name) {
        this.id = id;
        this.upiAggregationRuleStart = upiAggregationRuleStart;
        this.upiType = upiType;
        this.name = name;
    }

    @Override
    public String toString() {
        return "UPIAggregationRuleResponseOf{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", requirePositionToResponse=" + requirePositionToResponse +
                ", requirePositionToView=" + requirePositionToView +
                ", republishAllowed=" + republishAllowed +
                ", acceptMultiple=" + acceptMultiple +
                ", upiAggregationRuleStart=" + upiAggregationRuleStart +
                ", upiType=" + upiType +
                ", roles=" + roles +
                ", publications=" + publications +
                '}';
    }
}
