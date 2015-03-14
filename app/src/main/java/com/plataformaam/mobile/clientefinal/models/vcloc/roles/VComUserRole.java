package com.plataformaam.mobile.clientefinal.models.vcloc.roles;

import com.google.gson.annotations.SerializedName;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class VComUserRole implements Serializable {
    int id;

    @SerializedName("vcomcomposite0")
    VComComposite vComComposite;

    String name;
    int
            allowedEditVComAggregationRule,
            allowedEditVCom,
            isClientDefault,
            isClientSelectable,
            allowedAccessPedagogicalPanel,
            allowedAccessOnlineMap;

    @SerializedName("vComUPIAggregationRuleResponseOfs")
    List<VComUPIAggregationRuleResponseOf> responseRules;

    @SerializedName("vComUPIAggregationRuleStarts")
    List<VComUPIAggregationRuleStart> publicationRules;


    public List<VComUPIAggregationRuleResponseOf> getResponseRules() {
        return responseRules;
    }

    public void setResponseRules(List<VComUPIAggregationRuleResponseOf> responseRules) {
        this.responseRules = responseRules;
    }

    public List<VComUPIAggregationRuleStart> getPublicationRules() {
        return publicationRules;
    }

    public void setPublicationRules(List<VComUPIAggregationRuleStart> publicationRules) {
        this.publicationRules = publicationRules;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VComComposite getvComComposite() {
        return vComComposite;
    }

    public void setvComComposite(VComComposite vComComposite) {
        this.vComComposite = vComComposite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int isAllowedEditVComAggregationRule() {
        return allowedEditVComAggregationRule;
    }

    public void setAllowedEditVComAggregationRule(int allowedEditVComAggregationRule) {
        this.allowedEditVComAggregationRule = allowedEditVComAggregationRule;
    }

    public int isAllowedEditVCom() {
        return allowedEditVCom;
    }

    public void setAllowedEditVCom(int allowedEditVCom) {
        this.allowedEditVCom = allowedEditVCom;
    }

    public int isClientDefault() {
        return isClientDefault;
    }

    public void setClientDefault(int isClientDefault) {
        this.isClientDefault = isClientDefault;
    }

    public int isClientSelectable() {
        return isClientSelectable;
    }

    public void setClientSelectable(int isClientSelectable) {
        this.isClientSelectable = isClientSelectable;
    }

    public int isAllowedAccessPedagogicalPanel() {
        return allowedAccessPedagogicalPanel;
    }

    public void setAllowedAccessPedagogicalPanel(int allowedAccessPedagogicalPanel) {
        this.allowedAccessPedagogicalPanel = allowedAccessPedagogicalPanel;
    }

    public int isAllowedAccessOnlineMap() {
        return allowedAccessOnlineMap;
    }

    public void setAllowedAccessOnlineMap(int allowedAccessOnlineMap) {
        this.allowedAccessOnlineMap = allowedAccessOnlineMap;
    }

    public VComUserRole(int id, VComComposite vComComposite, String name, int allowedEditVComAggregationRule, int allowedEditVCom, int isClientDefault, int isClientSelectable, int allowedAccessPedagogicalPanel, int allowedAccessOnlineMap) {
        this.id = id;
        this.vComComposite = vComComposite;
        this.name = name;
        this.allowedEditVComAggregationRule = allowedEditVComAggregationRule;
        this.allowedEditVCom = allowedEditVCom;
        this.isClientDefault = isClientDefault;
        this.isClientSelectable = isClientSelectable;
        this.allowedAccessPedagogicalPanel = allowedAccessPedagogicalPanel;
        this.allowedAccessOnlineMap = allowedAccessOnlineMap;
    }

    public VComUserRole() {
    }

    public VComUserRole(int id, VComComposite vComComposite, String name) {
        this.id = id;
        this.vComComposite = vComComposite;
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", vComComposite=" + vComComposite +
                ", name='" + name + '\'' +
                ", allowedEditVComAggregationRule=" + allowedEditVComAggregationRule +
                ", allowedEditVCom=" + allowedEditVCom +
                ", isClientDefault=" + isClientDefault +
                ", isClientSelectable=" + isClientSelectable +
                ", allowedAccessPedagogicalPanel=" + allowedAccessPedagogicalPanel +
                ", allowedAccessOnlineMap=" + allowedAccessOnlineMap +
                '}';
    }
}
