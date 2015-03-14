package com.plataformaam.mobile.clientefinal.models.vcloc.roles;

import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleStart;

/**
 * Created by bernard on 31/01/2015.
 */
public class VComUPIAggregationRuleStart {
    int id;
    VComUserRole vComUserRole;
    UPIAggregationRuleStart upiAggregationRuleStart;
    boolean
            allowedRead,
            allowedWrite ;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VComUserRole getvComUserRole() {
        return vComUserRole;
    }

    public void setvComUserRole(VComUserRole vComUserRole) {
        this.vComUserRole = vComUserRole;
    }

    public UPIAggregationRuleStart getUpiAggregationRuleStart() {
        return upiAggregationRuleStart;
    }

    public void setUpiAggregationRuleStart(UPIAggregationRuleStart upiAggregationRuleStart) {
        this.upiAggregationRuleStart = upiAggregationRuleStart;
    }

    public boolean isAllowedRead() {
        return allowedRead;
    }

    public void setAllowedRead(boolean allowedRead) {
        this.allowedRead = allowedRead;
    }

    public boolean isAllowedWrite() {
        return allowedWrite;
    }

    public void setAllowedWrite(boolean allowedWrite) {
        this.allowedWrite = allowedWrite;
    }


    public VComUPIAggregationRuleStart(int id, VComUserRole vComUserRole, UPIAggregationRuleStart upiAggregationRuleStart) {
        this.id = id;
        this.vComUserRole = vComUserRole;
        this.upiAggregationRuleStart = upiAggregationRuleStart;
    }

    public VComUPIAggregationRuleStart(int id, VComUserRole vComUserRole, UPIAggregationRuleStart upiAggregationRuleStart, boolean allowedRead, boolean allowedWrite) {
        this.id = id;
        this.vComUserRole = vComUserRole;
        this.upiAggregationRuleStart = upiAggregationRuleStart;
        this.allowedRead = allowedRead;
        this.allowedWrite = allowedWrite;
    }

    public VComUPIAggregationRuleStart() {
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", vComUserRole=" + vComUserRole +
                ", upiAggregationRuleStart=" + upiAggregationRuleStart +
                ", allowedRead=" + allowedRead +
                ", allowedWrite=" + allowedWrite +
                '}';
    }
}
