package com.plataformaam.mobile.clientefinal.helpers.eventbus;

import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleResponseOf;
import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleStart;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;

import java.util.List;


public class MyPublishDetailMessage {
    private String Sender;
    private String Message;
    private VComUPIPublication publication;
    private UPIAggregationRuleStart publicationRule;
    private List<UPIAggregationRuleResponseOf> responseRules;
    private List<UPI> responsePublications;

    public MyPublishDetailMessage(String sender, String message) {
        Sender = sender;
        Message = message;
    }



    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public VComUPIPublication getPublication() {
        return publication;
    }

    public void setPublication(VComUPIPublication publication) {
        this.publication = publication;
    }

    public UPIAggregationRuleStart getPublicationRule() {
        return publicationRule;
    }

    public void setPublicationRule(UPIAggregationRuleStart publicationRule) {
        this.publicationRule = publicationRule;
    }

    public List<UPIAggregationRuleResponseOf> getResponseRules() {
        return responseRules;
    }

    public void setResponseRules(List<UPIAggregationRuleResponseOf> responseRules) {
        this.responseRules = responseRules;
    }

    public List<UPI> getResponsePublications() {
        return responsePublications;
    }

    public void setResponsePublications(List<UPI> responsePublications) {
        this.responsePublications = responsePublications;
    }
}
