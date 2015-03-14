package com.plataformaam.mobile.clientefinal.models.vcloc.upi;

/**
 * Created by bernard on 11/01/2015.
 */
public class UPIType {
    int id;
    String name;

    public UPIType() {
    }

    public UPIType(int id, String name) {
        this.id = id;
        this.name = name;
    }

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

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
