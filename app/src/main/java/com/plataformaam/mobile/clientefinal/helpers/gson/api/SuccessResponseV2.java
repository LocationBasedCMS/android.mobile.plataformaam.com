package com.plataformaam.mobile.clientefinal.helpers.gson.api;

import java.util.List;

/**
 * Created by bernard on 02/03/2015.
 */
public class SuccessResponseV2<MODEL> {
    final String serializedName;
    final Class<MODEL> typeOfModel;

    boolean success;
    String message;
    DataDescriptor data;


    public SuccessResponseV2(String serializedName, Class<MODEL> typeOfModel) {
        this.serializedName = serializedName;
        this.typeOfModel = typeOfModel;

    }

    public class DataDescriptor {
        int totalCount;
        List<MODEL> list;
   }



}