package com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.basemodel;

import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleResponseOf;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class UPIAggregationRuleResponseOfDescriptor {
    int totalCount;
    List<UPIAggregationRuleResponseOf> uPIAggregationRuleResponseOf;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<UPIAggregationRuleResponseOf> getuPIAggregationRuleResponseOf() {
        return uPIAggregationRuleResponseOf;
    }

    public void setuPIAggregationRuleResponseOf(List<UPIAggregationRuleResponseOf> uPIAggregationRuleResponseOf) {
        this.uPIAggregationRuleResponseOf = uPIAggregationRuleResponseOf;
    }

    public UPIAggregationRuleResponseOfDescriptor(int totalCount, List<UPIAggregationRuleResponseOf> uPIAggregationRuleResponseOf) {
        this.totalCount = totalCount;
        this.uPIAggregationRuleResponseOf = uPIAggregationRuleResponseOf;
    }

    public UPIAggregationRuleResponseOfDescriptor() {
    }
}
