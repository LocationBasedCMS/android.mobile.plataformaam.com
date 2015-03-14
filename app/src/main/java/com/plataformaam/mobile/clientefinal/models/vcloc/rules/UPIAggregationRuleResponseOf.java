package com.plataformaam.mobile.clientefinal.models.vcloc.rules;

import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPIType;

/**
 * Created by bernard on 31/01/2015.
 */
public class UPIAggregationRuleResponseOf {
    int id;
    UPIAggregationRuleStart upiAggregationRuleStart;
    UPIType upiType;
    String name;
    boolean
            requirePositionToResponse,
            requirePositionToView,
            republisAllowed,
            aceptMultiple;


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

    public boolean isRepublisAllowed() {
        return republisAllowed;
    }

    public void setRepublisAllowed(boolean republisAllowed) {
        this.republisAllowed = republisAllowed;
    }

    public boolean isAceptMultiple() {
        return aceptMultiple;
    }

    public void setAceptMultiple(boolean aceptMultiple) {
        this.aceptMultiple = aceptMultiple;
    }

    public UPIAggregationRuleResponseOf() {
    }

    public UPIAggregationRuleResponseOf(int id, UPIAggregationRuleStart upiAggregationRuleStart, UPIType upiType, String name, boolean requirePositionToResponse, boolean requirePositionToView, boolean republisAllowed, boolean aceptMultiple) {
        this.id = id;
        this.upiAggregationRuleStart = upiAggregationRuleStart;
        this.upiType = upiType;
        this.name = name;
        this.requirePositionToResponse = requirePositionToResponse;
        this.requirePositionToView = requirePositionToView;
        this.republisAllowed = republisAllowed;
        this.aceptMultiple = aceptMultiple;
    }

    public UPIAggregationRuleResponseOf(int id, UPIAggregationRuleStart upiAggregationRuleStart, UPIType upiType, String name) {
        this.id = id;
        this.upiAggregationRuleStart = upiAggregationRuleStart;
        this.upiType = upiType;
        this.name = name;
    }
}
