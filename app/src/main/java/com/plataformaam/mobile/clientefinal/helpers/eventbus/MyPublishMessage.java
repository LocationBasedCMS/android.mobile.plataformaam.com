package com.plataformaam.mobile.clientefinal.helpers.eventbus;

import com.google.android.gms.maps.model.LatLng;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComBase;
import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleResponseOf;
import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleStart;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;

import java.util.List;

/**
 * Created by bernard on 06/03/2015.
 */
public class MyPublishMessage {

    private String Sender;
    private String Message;

    VComUPIPublication publication;
    VComBase base;
    UPI upi;
    UPIAggregationRuleStart publishRule;
    UPIAggregationRuleResponseOf responseRule;
    LatLng position;
    List<VComUPIPublication> publications;

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

    public VComBase getBase() {
        return base;
    }

    public void setBase(VComBase base) {
        this.base = base;
    }

    public UPI getUpi() {
        return upi;
    }

    public void setUpi(UPI upi) {
        this.upi = upi;
    }

    public UPIAggregationRuleStart getPublishRule() {
        return publishRule;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public void setPublishRule(UPIAggregationRuleStart publishRule) {
        this.publishRule = publishRule;
    }

    public UPIAggregationRuleResponseOf getResponseRule() {
        return responseRule;
    }

    public void setResponseRule(UPIAggregationRuleResponseOf responseRule) {
        this.responseRule = responseRule;
    }

    public List<VComUPIPublication> getPublications() {
        return publications;
    }

    public void setPublications(List<VComUPIPublication> publications) {
        this.publications = publications;
    }

    public MyPublishMessage(String sender, String message, VComUPIPublication publication) {
        Sender = sender;
        Message = message;
        this.publication = publication;
    }

    public MyPublishMessage(String sender, String message) {
        Sender = sender;
        Message = message;
    }

    public MyPublishMessage() {
    }
}
