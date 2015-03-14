package com.plataformaam.mobile.clientefinal.models.vcloc;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.plataformaam.mobile.clientefinal.models.location.VirtualSpace;
import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleResponseOf;
import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleStart;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class VComBase {
    int id;
    String
        name,
        description;

    @SerializedName("vcomcomposite0")
    VComComposite vComComposite;

    @SerializedName("virtualspace0")
    VirtualSpace virtualSpace;

    @SerializedName("uPIAggregationRuleStarts")
    List<UPIAggregationRuleStart> startRules;

    @SerializedName("vComUPIPublications")
    List<VComUPIPublication> publications;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VComComposite getvComComposite() {
        return vComComposite;
    }

    public void setvComComposite(VComComposite vComComposite) {
        this.vComComposite = vComComposite;
    }

    public VirtualSpace getVirtualSpace() {
        return virtualSpace;
    }

    public void setVirtualSpace(VirtualSpace virtualSpace) {
        this.virtualSpace = virtualSpace;
    }

    public VComBase(int id, String name, String description, VComComposite vComComposite, VirtualSpace virtualSpace) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.vComComposite = vComComposite;
        this.virtualSpace = virtualSpace;
    }

    public VComBase() {
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", vComComposite=" + vComComposite +
                ", virtualSpace=" + virtualSpace +
                '}';
    }

    public boolean isInside(LatLng latLng){
        return virtualSpace.isInside(latLng);
    }

    public boolean isInside(Location location){
        return virtualSpace.isInside(location);
    }


    public List<UPIAggregationRuleStart> getStartRules() {
        return startRules;
    }

    public void setStartRules(List<UPIAggregationRuleStart> startRules) {
        this.startRules = startRules;
    }

    public List<VComUPIPublication> getPublications() {
        return publications;
    }

    public void setPublications(List<VComUPIPublication> publications) {
        this.publications = publications;
    }
}
