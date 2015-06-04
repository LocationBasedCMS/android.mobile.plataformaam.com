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
    //SINGLETON
    private static MyAppData myAppData;
    public static MyAppData getInstance(){
        if( myAppData == null ){
            myAppData = new MyAppData();
        }
        return myAppData;
    }


    //DADOS DO SISTEMA
    private Map<Integer,VComComposite> allVComComposite;
    private final List<UPIType> upiTypes;


    //TIPOS DO APLICATIVO
    public List<UPIType> getUpiTypes() {
        return upiTypes;
    }
    public UPIType getUpiType(int UPI_CODE){
        return   upiTypes.get(UPI_CODE);
    }

    public MyAppData() {
        this.upiTypes= MyAppConfig.loadUpiTypes();
        this.allVComComposite = new HashMap<Integer, VComComposite>();    //VEÍCULOS DE COMUNICAÇÂO DISPONÍVEIS PARA ESCOLHA
    }

    public void setVComComposite(List<VComComposite> list){
        for(VComComposite v: list){
            this.allVComComposite.put(v.getId(),v);
        }
    }

    public void setVComComposite(Map<Integer,VComComposite> map){
        this.allVComComposite =map;
    }
    public Map<Integer, VComComposite> getAllVComComposite() {
        return this.allVComComposite;
    }
}




