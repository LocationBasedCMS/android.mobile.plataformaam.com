package com.plataformaam.mobile.clientefinal.services.runnables;

import com.plataformaam.mobile.clientefinal.helpers.MyFilterItem;
import com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.model.get.GetUPIAggregationRuleResponseOfResponse;
import com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.model.get.GetUPIAggregationRuleStartResponse;
import com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.model.get.GetUPIResponse;
import com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.model.get.GetVComBaseResponse;
import com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.model.get.GetVComCompositeResponse;
import com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.model.get.GetVComUPIAggregationRuleStartResponse;
import com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.model.get.GetVComUPIPublicationResponse;
import com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.operation.WebClientGetData;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComBase;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleResponseOf;
import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleStart;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;
import com.plataformaam.mobile.clientefinal.services.VCLocClientService;
import com.plataformaam.mobile.clientefinal.services.broadcastsender.VClocClientServiceBroadcasterSender;
import com.plataformaam.mobile.clientefinal.services.vcomcontrol.VComController;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by bernard on 19/02/2015.
 */
public class LoadGlobalDataRunnable implements Runnable {
    public static boolean isRunning = false;
    final VCLocClientService service;

    public LoadGlobalDataRunnable(VCLocClientService service) {
        this.service = service;
    }

    @Override
    public void run() {
        isRunning = !isRunning;
        if( isRunning ) {
            //loadData();
        }
        isRunning = !isRunning;
    }



    Map<Integer,VComComposite> outputComposite = new HashMap<Integer,VComComposite>();
    Map<Integer,VComComposite> mapComposites  = new HashMap<Integer,VComComposite>();
    Map<Integer,VComBase> mapBases = new HashMap<Integer,VComBase>();
    Map<Integer,UPIAggregationRuleResponseOf> mapResponseRules = new HashMap<Integer,UPIAggregationRuleResponseOf>();
    Map<Integer,UPIAggregationRuleStart> mapPublicationRules = new HashMap<Integer,UPIAggregationRuleStart>();
    Map<Integer,UPI> mapUpis= new HashMap<Integer,UPI>();
    Map<Integer,VComUPIPublication> mapPublications = new HashMap<Integer,VComUPIPublication>();

    private void loadData(){
        webRequestComposites();
        /*
        webRequestBases();
        webRequestPublicationRules();
        webRequestResponseRules();
        webRequestUPI();
        webRequestPublications();
        //TODO - LIGAR OS DADOS  ADEQUADAMENTE

        prepareOutput();
        */
        service.getVComController().setAllVComComposite( outputComposite );


    }


    private Map<Integer,VComComposite> webRequestComposites(){
        WebClientGetData<VComComposite, GetVComCompositeResponse> getComposite = new WebClientGetData<VComComposite, GetVComCompositeResponse>(VComComposite.class,GetVComCompositeResponse.class,null);
        getComposite.addFilter(new MyFilterItem("id", "1", ">"));
        GetVComCompositeResponse result = getComposite.execute();
        if ( result.isSuccess() && result.getData().getTotalCount() > 0  ) {
            List<VComComposite> composites = result.getData().getvComComposite();
            for( VComComposite c: composites ){
                mapComposites.put(c.getId(),c);
            }
        }
        return mapComposites;

    }

    private Map<Integer,VComBase> webRequestBases(){
        WebClientGetData<VComBase,GetVComBaseResponse> getBase = new WebClientGetData<VComBase, GetVComBaseResponse>(VComBase.class,GetVComBaseResponse.class,null);
        GetVComBaseResponse result = getBase.execute();
        if ( result.isSuccess() && result.getData().getTotalCount() > 0  ) {
            List<VComBase> bases = result.getData().getvComBase();
            for( VComBase b : bases){
                mapBases.put(b.getId(),b);
            }
        }
        return mapBases;
    }

    private Map<Integer,UPIAggregationRuleStart> webRequestPublicationRules(){
        WebClientGetData<UPIAggregationRuleStart,GetUPIAggregationRuleStartResponse> getPublicationsRules
                = new WebClientGetData<UPIAggregationRuleStart, GetUPIAggregationRuleStartResponse>(UPIAggregationRuleStart.class, GetUPIAggregationRuleStartResponse.class, null);
        GetUPIAggregationRuleStartResponse result = getPublicationsRules.execute();
        if ( result.isSuccess() && result.getData().getTotalCount() > 0  ) {
            List<UPIAggregationRuleStart> data = result.getData().getuPIAggregationRuleStart();
            for( UPIAggregationRuleStart item : data){
                mapPublicationRules.put(item.getId(),item);
            }
        }
        return mapPublicationRules;
    }


    private Map<Integer,UPIAggregationRuleResponseOf> webRequestResponseRules(){

        WebClientGetData<UPIAggregationRuleResponseOf,GetUPIAggregationRuleResponseOfResponse> getResponseRules
                = new WebClientGetData<UPIAggregationRuleResponseOf, GetUPIAggregationRuleResponseOfResponse>(UPIAggregationRuleResponseOf.class, GetUPIAggregationRuleResponseOfResponse.class, null);
        GetUPIAggregationRuleResponseOfResponse result = getResponseRules.execute();
        if ( result.isSuccess() && result.getData().getTotalCount() > 0  ) {

            List<UPIAggregationRuleResponseOf> data = result.getData().getuPIAggregationRuleResponse();
            for( UPIAggregationRuleResponseOf item : data){
                mapResponseRules.put(item.getId(),item);
            }

        }
        return mapResponseRules;
    }

    private Map<Integer,UPI> webRequestUPI(){
        WebClientGetData<UPI,GetUPIResponse> getBase = new WebClientGetData<UPI, GetUPIResponse>(UPI.class,GetUPIResponse.class,null);
        GetUPIResponse result = getBase.execute();
        if ( result.isSuccess() && result.getData().getTotalCount() > 0  ) {
            List<UPI> data = result.getData().getuPI();
            for( UPI item : data){
                mapUpis.put(item.getId(),item);
            }
        }
        return mapUpis;
    }


    private Map<Integer, VComUPIPublication> webRequestPublications(){
        WebClientGetData<VComUPIPublication, GetVComUPIPublicationResponse > getBase = new WebClientGetData<VComUPIPublication, GetVComUPIPublicationResponse>(VComUPIPublication.class,GetVComUPIPublicationResponse.class,null);
        GetVComUPIPublicationResponse result = getBase.execute();
        if ( result.isSuccess() && result.getData().getTotalCount() > 0  ) {
            List<VComUPIPublication> data = result.getData().getvComUPIPublication();
            for( VComUPIPublication item : data){
                mapPublications.put(item.getId(),item);
            }
        }
        return mapPublications;
    }


    void prepareOutput(){
        Iterator<Integer> iterator = mapComposites.keySet().iterator();
        while( iterator.hasNext() ){

            VComComposite object =  prepareComposite(  mapComposites.get(iterator.next()) );
            outputComposite.put( object.getId(),object  );

        }
    }

    VComComposite prepareComposite(VComComposite object){
        List<VComBase> list = object.getvComBases();
        //Limpa as Lista
        object.getvComBases().clear();
        for( VComBase item: list ){

            VComBase prepared = mapBases.get(item.getId());
            prepared = prepareBase( prepared);

            object.getvComBases().add(prepared);
        }
        return object;
    }

    VComBase prepareBase(VComBase object){

        return object;
    }
}
