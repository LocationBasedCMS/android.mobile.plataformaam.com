package com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.basemodel;

import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleResponseOf;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class UPIAggregationRuleResponseOfDescriptor {
    int totalCount;
    List<UPIAggregationRuleResponseOf> uPIAggregationRuleResponse;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<UPIAggregationRuleResponseOf> getuPIAggregationRuleResponse() {
        return uPIAggregationRuleResponse;
    }

    public void setuPIAggregationRuleResponse(List<UPIAggregationRuleResponseOf> uPIAggregationRuleResponse) {
        this.uPIAggregationRuleResponse = uPIAggregationRuleResponse;
    }

    public UPIAggregationRuleResponseOfDescriptor(int totalCount, List<UPIAggregationRuleResponseOf> uPIAggregationRuleResponse) {
        this.totalCount = totalCount;
        this.uPIAggregationRuleResponse = uPIAggregationRuleResponse;
    }

    public UPIAggregationRuleResponseOfDescriptor() {
    }
}
