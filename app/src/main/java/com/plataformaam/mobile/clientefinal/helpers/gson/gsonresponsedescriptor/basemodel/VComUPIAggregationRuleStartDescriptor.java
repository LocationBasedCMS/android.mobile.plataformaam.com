package com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.basemodel;

import com.plataformaam.mobile.clientefinal.models.vcloc.roles.VComUPIAggregationRuleStart;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class VComUPIAggregationRuleStartDescriptor {
    int totalCount;
    List<VComUPIAggregationRuleStart> vComUPIAggregationRuleStart;


    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<VComUPIAggregationRuleStart> getvComUPIAggregationRuleStart() {
        return vComUPIAggregationRuleStart;
    }

    public void setvComUPIAggregationRuleStart(List<VComUPIAggregationRuleStart> vComUPIAggregationRuleStart) {
        this.vComUPIAggregationRuleStart = vComUPIAggregationRuleStart;
    }

    public VComUPIAggregationRuleStartDescriptor(int totalCount, List<VComUPIAggregationRuleStart> vComUPIAggregationRuleStart) {
        this.totalCount = totalCount;
        this.vComUPIAggregationRuleStart = vComUPIAggregationRuleStart;
    }

    public VComUPIAggregationRuleStartDescriptor() {
    }
}
