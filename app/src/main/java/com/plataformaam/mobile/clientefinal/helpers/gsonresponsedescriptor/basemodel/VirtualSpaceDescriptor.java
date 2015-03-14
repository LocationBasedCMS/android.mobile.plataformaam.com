package com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.basemodel;

import com.plataformaam.mobile.clientefinal.models.location.VirtualSpace;

import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class VirtualSpaceDescriptor {
    int totalCount;
    List<VirtualSpace> virtualSpace;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<VirtualSpace> getVirtualSpace() {
        return virtualSpace;
    }

    public void setVirtualSpace(List<VirtualSpace> virtualSpace) {
        this.virtualSpace = virtualSpace;
    }

    public VirtualSpaceDescriptor(int totalCount, List<VirtualSpace> virtualSpace) {
        this.totalCount = totalCount;
        this.virtualSpace = virtualSpace;
    }

    public VirtualSpaceDescriptor() {
    }
}
