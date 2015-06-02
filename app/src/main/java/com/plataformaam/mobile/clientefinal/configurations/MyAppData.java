package com.plataformaam.mobile.clientefinal.configurations;

import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPIType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bernard on 17/01/2015.
 */

public class MyAppData {




    //DADOS DO SISTEMA
    private static Map<Integer,VComComposite> allVComComposite = new HashMap<Integer, VComComposite>();    //VEÍCULOS DE COMUNICAÇÂO DISPONÍVEIS PARA ESCOLHA
    private static final List<UPIType> upiTypes = MyAppConfig.loadUpiTypes();
    public List<UPIType> getUpiTypes() {
        return upiTypes;
    }
    public UPIType getUpiType(int UPI_CODE){
        return   upiTypes.get(UPI_CODE);
    }


    //SINGLETON
    private static MyAppData myAppData;
    public static MyAppData getInstance(){
        if( myAppData == null ){
            myAppData = new MyAppData();
        }
        return myAppData;
    }


    public MyAppData() {
    }

    public static void setVComComposite(List<VComComposite> list){
        for(VComComposite v: list){
            allVComComposite.put(v.getId(),v);
        }
    }

    public static void setVComComposite(Map<Integer,VComComposite> map){
        allVComComposite =map;
    }
    public static Map<Integer, VComComposite> getAllVComComposite() {
        return allVComComposite;
    }
}




