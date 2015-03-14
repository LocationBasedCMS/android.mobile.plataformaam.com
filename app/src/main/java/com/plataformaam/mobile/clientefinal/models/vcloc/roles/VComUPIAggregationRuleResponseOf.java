package com.plataformaam.mobile.clientefinal.models.vcloc.roles;

import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleResponseOf;

import java.io.Serializable;

/**
 * Created by bernard on 31/01/2015.
 */
public class VComUPIAggregationRuleResponseOf   implements Serializable {
    int id;
    VComUserRole vComUserRole;
    boolean
            allowedRead,
            allowedWrite ;
    UPIAggregationRuleResponseOf upiaggregationruleresponseof;

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

    public UPIAggregationRuleResponseOf getUpiaggregationruleresponseof() {
        return upiaggregationruleresponseof;
    }

    public void setUpiaggregationruleresponseof(UPIAggregationRuleResponseOf upiaggregationruleresponseof) {
        this.upiaggregationruleresponseof = upiaggregationruleresponseof;
    }

    public VComUPIAggregationRuleResponseOf(int id, VComUserRole vComUserRole, UPIAggregationRuleResponseOf upiaggregationruleresponseof) {
        this.id = id;
        this.vComUserRole = vComUserRole;
        this.upiaggregationruleresponseof = upiaggregationruleresponseof;
    }

    public VComUPIAggregationRuleResponseOf() {
    }

    public VComUPIAggregationRuleResponseOf(int id, VComUserRole vComUserRole, boolean allowedRead, boolean allowedWrite, UPIAggregationRuleResponseOf upiaggregationruleresponseof) {
        this.id = id;
        this.vComUserRole = vComUserRole;
        this.allowedRead = allowedRead;
        this.allowedWrite = allowedWrite;
        this.upiaggregationruleresponseof = upiaggregationruleresponseof;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", vComUserRole=" + vComUserRole +
                ", allowedRead=" + allowedRead +
                ", allowedWrite=" + allowedWrite +
                ", upiaggregationruleresponseof=" + upiaggregationruleresponseof +
                '}';
    }
}
