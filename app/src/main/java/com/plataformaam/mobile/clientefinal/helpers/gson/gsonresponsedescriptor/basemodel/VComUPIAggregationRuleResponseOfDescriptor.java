package com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.basemodel;

import com.plataformaam.mobile.clientefinal.models.vcloc.roles.VComUPIAggregationRuleResponseOf;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class VComUPIAggregationRuleResponseOfDescriptor {
    int totalCount;
    List<VComUPIAggregationRuleResponseOf> vComUPIAggregationRuleResponseOf;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<VComUPIAggregationRuleResponseOf> getvComUPIAggregationRuleResponseOf() {
        return vComUPIAggregationRuleResponseOf;
    }

    public void setvComUPIAggregationRuleResponseOf(List<VComUPIAggregationRuleResponseOf> vComUPIAggregationRuleResponseOf) {
        this.vComUPIAggregationRuleResponseOf = vComUPIAggregationRuleResponseOf;
    }

    public VComUPIAggregationRuleResponseOfDescriptor(int totalCount, List<VComUPIAggregationRuleResponseOf> vComUPIAggregationRuleResponseOf) {
        this.totalCount = totalCount;
        this.vComUPIAggregationRuleResponseOf = vComUPIAggregationRuleResponseOf;
    }

    public VComUPIAggregationRuleResponseOfDescriptor() {
    }
}
