package com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.basemodel;

import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleStart;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class UPIAggregationRuleStartDescriptor {
    int totalCount;
    List<UPIAggregationRuleStart> uPIAggregationRuleStart;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<UPIAggregationRuleStart> getuPIAggregationRuleStart() {
        return uPIAggregationRuleStart;
    }

    public void setuPIAggregationRuleStart(List<UPIAggregationRuleStart> uPIAggregationRuleStart) {
        this.uPIAggregationRuleStart = uPIAggregationRuleStart;
    }

    public UPIAggregationRuleStartDescriptor(int totalCount, List<UPIAggregationRuleStart> uPIAggregationRuleStart) {
        this.totalCount = totalCount;
        this.uPIAggregationRuleStart = uPIAggregationRuleStart;
    }

    public UPIAggregationRuleStartDescriptor() {
    }


}
