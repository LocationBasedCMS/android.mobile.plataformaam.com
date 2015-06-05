package com.plataformaam.mobile.clientefinal.services;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.plataformaam.mobile.clientefinal.AppController;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfig;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyMessage;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyPublishMessage;
import com.plataformaam.mobile.clientefinal.helpers.gson.adapters.GSonBooleanAsIntAdapter;
import com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.model.get.GetUPIAggregationRuleResponseOfResponse;
import com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.model.get.GetUPIAggregationRuleStartResponse;
import com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.model.get.GetVComBaseResponse;
import com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.model.get.GetVComCompositeResponse;
import com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.model.get.GetVComUPIPublicationResponse;
import com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.model.post.PostUserResponse;
import com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.model.post.PostVComUPIPublicationResponse;
import com.plataformaam.mobile.clientefinal.helpers.volley.MyPostStringRequest;
import com.plataformaam.mobile.clientefinal.helpers.volley.MyStringRequestV2;
import com.plataformaam.mobile.clientefinal.helpers.volley.MyStringRequestV3;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.location.UserPosition;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComBase;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.models.vcloc.roles.VComUserRole;
import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleResponseOf;
import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleStart;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;
import com.plataformaam.mobile.clientefinal.userinterfaces.listfragments.FragmentVComCompositeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class MyVComService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(MyVComService.this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(MyVComService.this);
    }

    public MyVComService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(MyAppConfig.LOG.VComService,"onStartCommand ");
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        loadVComComposite();
                        loadResponseRule();
                        loadPublicationRules();
                    }
                }
        ).start();
        return super.onStartCommand(intent, flags, startId);
    }

    ///////////////////////////////////////////////////////////////////////////////
    //  COMPOSITES
    ////////////////////////////////////////////////////////////////////////////




    public void loadVComComposite(){
        //CRIA USUÁRIO PARA OBTER OS DADOS
        User user = new User();
        user.setLogin(MyAppConfig.getInstance().getLoginBase());
        user.setPassword(MyAppConfig.getInstance().getPasswordBase());
        String request_url = MyAppConfig.getInstance().prepareWebService("VComComposite");
        //request_url = "http://api.plataformaam.com/v3/index.php/api/VComComposite";

        StringRequest stringRequest = new MyStringRequestV3(
                Request.Method.GET,
                user,
                request_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        GsonBuilder builderResult = new GsonBuilder();
                        builderResult.setDateFormat("yyyy-MM-dd HH:mm:ss");
                        builderResult.registerTypeAdapter(Boolean.class, new GSonBooleanAsIntAdapter()).registerTypeAdapter(boolean.class , new GSonBooleanAsIntAdapter());

                        Gson gsonResult = builderResult.create();
                        GetVComCompositeResponse output = gsonResult.fromJson(response,GetVComCompositeResponse.class);
                        if( output.isSuccess() && output.getData() != null && output.getData().getTotalCount() > 0  ){
                            AppController.getInstance().setAllComposites(output.getData().getvComComposite());
                            detectUserComposite();
                        }else{
                            AppController.getInstance().setAllComposites(new ArrayList<VComComposite>());
                        }
                        sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.COMPOSITE_LOADED_SUCCESS);
                        loadBases();
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.COMPOSITE_LOADED_FAIL);
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(stringRequest, MyAppConfig.VOLLEY_TAG.MANIPULATE_VCOM);

    }




    public void detectUserComposite(){
        AppController app = AppController.getInstance();
        if( app != null ){
            //Map<Integer,VComComposite> myComposites =new HashMap<Integer,VComComposite>();
            Map<Integer,VComComposite> myComposites =new HashMap<>();
            List<VComComposite> composites = app.getAllComposites();

            User u = app.getOnlineUser();
            if( u!= null && u.getRoles() != null && composites != null ) {
                for (VComUserRole role : u.getRoles()) {
                    for (VComComposite c : composites) {
                        if (!myComposites.containsKey(c.getId()) && c.hasRole(role)) {
                            myComposites.put(c.getId(), c);
                        }
                    }
                }
            }
            AppController.getInstance().setMyComposite(myComposites);

        }

    }


    ///////////////////////////////////////////////////////////////////////////////
    //  BASES
    ////////////////////////////////////////////////////////////////////////////
    boolean loadedBase = false;
    public synchronized void loadBases(){
        //CRIA USUÁRIO PARA OBTER OS DADOS

        if (!loadedBase ) {
            loadedBase = true;
            User user = new User();
            user.setLogin(MyAppConfig.getInstance().getLoginBase());
            user.setPassword(MyAppConfig.getInstance().getPasswordBase());
            String request_url = MyAppConfig.getInstance().prepareWebService("VComBase");

            StringRequest stringRequest = new MyStringRequestV2(
                    Request.Method.GET,
                    user,
                    request_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            GsonBuilder builderResult = new GsonBuilder();
                            builderResult.setDateFormat("yyyy-MM-dd HH:mm:ss");
                            builderResult.registerTypeAdapter(Boolean.class, new GSonBooleanAsIntAdapter()).registerTypeAdapter(boolean.class , new GSonBooleanAsIntAdapter());
                            Gson gsonResult = builderResult.create();
                            GetVComBaseResponse output = gsonResult.fromJson(response, GetVComBaseResponse.class);
                            if( output.isSuccess() && output.getData() != null && output.getData().getTotalCount() > 0  ) {
                                Map<Integer,VComBase> mapBases = new HashMap<>();
                                for( VComBase base: output.getData().getvComBase() ){
                                    mapBases.put(base.getId(),base);
                                }

                                for( int i = 0, sizeComposites= AppController.getInstance().getAllComposites().size()  ; i < sizeComposites ; i++ ){
                                    VComComposite testComposite = AppController.getInstance().getAllComposites().get(i);

                                    for( int j=0, sizeBases=AppController.getInstance().getAllComposites().get(i).getvComBases().size() ; j < sizeBases; j++ ){
                                        VComBase base = AppController.getInstance().getAllComposites().get(i).getvComBases().get(j);
                                        AppController.getInstance().getAllComposites().get(i).getvComBases().set(j,mapBases.get(base.getId()));
                                    }
                                }


                            }else{
                                loadedBase = false;
                            }
                            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.BASE_LOADED_SUCCESS);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.BASE_LOADED_FAIL);
                            loadedBase = false;
                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(stringRequest, MyAppConfig.VOLLEY_TAG.MANIPULATE_VCOM );
        }

    }


    //CONNECT TO WEBSERVICE FOR REGISTER THE PUBLICATION
    private void savePublication(final VComUPIPublication publication) {
        String request_url = MyAppConfig.getInstance().prepareWebService("VComUPIPublication");
        StringRequest stringRequest = new MyPostStringRequest(
                request_url,
                publication,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        GsonBuilder builderResult = new GsonBuilder();
                        builderResult.registerTypeAdapter(Boolean.class, new GSonBooleanAsIntAdapter()).registerTypeAdapter(boolean.class , new GSonBooleanAsIntAdapter());
                        builderResult.setDateFormat("yyyy-MM-dd HH:mm:ss");

                        Gson gsonResult = builderResult.create();
                        PostVComUPIPublicationResponse output= gsonResult.fromJson( response, PostVComUPIPublicationResponse.class);
                        if( output.isSuccess() && output.getData().getTotalCount() == 1   ) {
                            VComUPIPublication savedPublication = output.getData().getvComUPIPublication();
                            savedPublication.setUpi(publication.getUpi());
                            savedPublication.setUser(publication.getUser());
                            savedPublication.setPublishRule(publication.getPublishRule());
                            savedPublication.setResponseRule(publication.getResponseRule());
                            AppController.getInstance().getOnlineUser().getPublications().add(savedPublication);
                            //TODO - implementar saída
                            Log.i(MyAppConfig.LOG.VComService, "createPublication: Sucesso -> " + savedPublication.toString());
                            sendEventBusMessage( MyAppConfig.EVENT_BUS_MESSAGE.PUBLISH_UPI_SUCCESS);
                            reloadPublication();
                        }else{
                            //TODO - implementar saída de erro
                            Log.i(MyAppConfig.LOG.VComService,"createPublication: Falha ");
                            sendEventBusMessage( MyAppConfig.EVENT_BUS_MESSAGE.PUBLISH_UPI_FAIL);
                        }

                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.PUBLISH_UPI_FAIL);
                        Log.i(MyAppConfig.LOG.VComService,"createPublication: "+error.getMessage());
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(stringRequest, MyAppConfig.VOLLEY_TAG.MANIPULATE_UPI);
    }


    private void createPublication(VComBase base, UPI upi, UPIAggregationRuleStart publishRule, LatLng position){
        VComUPIPublication publication = new VComUPIPublication(upi,AppController.getInstance().getOnlineUser(), base, publishRule,position);
        savePublication(publication);
    }


    private void createResponse(VComBase base, UPI upi, UPIAggregationRuleResponseOf responseRule, LatLng position){
        VComUPIPublication publication = new VComUPIPublication(upi,AppController.getInstance().getOnlineUser(),base,responseRule,position);
        savePublication(publication);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // PUBLICATIONS RULES
    /////////////////////////////////////////////////////////////////////////////////////////////

    private void loadPublicationRules(){
        User user = new User();
        user.setLogin(MyAppConfig.getInstance().getLoginBase());
        user.setPassword(MyAppConfig.getInstance().getPasswordBase());
        String request_url = MyAppConfig.getInstance().prepareWebService("UPIAggregationRuleStart");

        StringRequest stringRequest = new MyStringRequestV2(
                Request.Method.GET,
                user,
                request_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        GsonBuilder builderResult = new GsonBuilder();
                        builderResult.setDateFormat("yyyy-MM-dd HH:mm:ss");
                        builderResult.registerTypeAdapter(Boolean.class, new GSonBooleanAsIntAdapter()).registerTypeAdapter(boolean.class , new GSonBooleanAsIntAdapter());
                        Gson gsonResult = builderResult.create();
                        GetUPIAggregationRuleStartResponse output = gsonResult.fromJson(response, GetUPIAggregationRuleStartResponse.class);
                        Map<Integer,UPIAggregationRuleStart> publishRules = new HashMap<>();
                        if( output.isSuccess() && output.getData() != null && output.getData().getTotalCount() > 0  ) {
                            List<UPIAggregationRuleStart> list = output.getData().getuPIAggregationRuleStart();
                            for( int i =0; i< list.size(); i  ++ ){
                                publishRules.put(   list.get(i).getId(),list.get(i)  );
                            }
                        }
                        AppController.getInstance().setAllPublicationRules(publishRules);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(MyAppConfig.LOG.VComService,"loadPublicationRules :"+error.getMessage());
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(stringRequest, MyAppConfig.VOLLEY_TAG.MANIPULATE_VCOM);
    }


    private void loadResponseRule(){
        User user = new User();
        user.setLogin(MyAppConfig.getInstance().getLoginBase());
        user.setPassword(MyAppConfig.getInstance().getPasswordBase());
        String request_url = MyAppConfig.getInstance().prepareWebService("UPIAggregationRuleResponseOf");

        StringRequest stringRequest = new MyStringRequestV2(
                Request.Method.GET,
                user,
                request_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        GsonBuilder builderResult = new GsonBuilder();
                        builderResult.setDateFormat("yyyy-MM-dd HH:mm:ss");
                        builderResult.registerTypeAdapter(Boolean.class, new GSonBooleanAsIntAdapter()).registerTypeAdapter(boolean.class , new GSonBooleanAsIntAdapter());
                        Gson gsonResult = builderResult.create();
                        GetUPIAggregationRuleResponseOfResponse output = gsonResult.fromJson(response, GetUPIAggregationRuleResponseOfResponse.class);
                        Map<Integer,UPIAggregationRuleResponseOf> responseRules = new HashMap<>();
                        if( output.isSuccess() && output.getData() != null && output.getData().getTotalCount() > 0  ) {
                            List<UPIAggregationRuleResponseOf> list = output.getData().getuPIAggregationRuleResponseOf();
                            for( int i =0; i< list.size(); i  ++ ){
                                responseRules.put(list.get(i).getId(), list.get(i));
                            }
                        }
                        AppController.getInstance().setAllResponseRules(responseRules);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(MyAppConfig.LOG.VComService,"loadResponseRule :"+error.getMessage());
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(stringRequest, MyAppConfig.VOLLEY_TAG.MANIPULATE_VCOM );
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////
    // PUBLICATION
    /////////////////////////////////////////////////////////////////////////////////////////////

    private boolean isPublicationReloading = false;
    private void reloadPublication(){
        if( !isPublicationReloading ){
            isPublicationReloading = true;
            final User user = AppController.getInstance().getOnlineUser();
            if(user.getUserPositions() != null && !user.getUserPositions().isEmpty() ) {

                //ULTIMA POSIÇÂO CONHECIDA
                final UserPosition position =user.getUserPositions().get(user.getUserPositions().size()-1);

                //https://gis.stackexchange.com/questions/8650/how-to-measure-the-accuracy-of-latitude-and-longitude/8674#8674?newreg=f2b54a75c8ae4c8fa450184001cd5fcd
                //The second decimal place is worth up to 1.1 km: it can separate one village from the next.
                String filter = "[" +
                                    "{\"property\": \"latitude\",   \"value\" : " + (position.getLatitude() - 0.1) + ",   \"operator\": \">\"}," +
                                    "{\"property\": \"latitude\",   \"value\" : " + (position.getLatitude() + 0.1) + ",   \"operator\": \"<\"}," +
                                    "{\"property\": \"longitude\",  \"value\" : " + (position.getLongitude() - 0.1) + ",   \"operator\": \">\"}," +
                                    "{\"property\": \"longitude\",  \"value\" : " + (position.getLongitude() + 0.1) + ",   \"operator\": \"<\"}," +
                        "]";


                String request_url = MyAppConfig.getInstance().prepareWebService("VComUPIPublication", filter);
                StringRequest stringRequest = new MyStringRequestV2(
                        StringRequest.Method.GET,
                        user,
                        request_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                GsonBuilder builderResult = new GsonBuilder();
                                builderResult.registerTypeAdapter(Boolean.class, new GSonBooleanAsIntAdapter()).registerTypeAdapter(boolean.class , new GSonBooleanAsIntAdapter());
                                builderResult.setDateFormat("yyyy-MM-dd HH:mm:ss");
                                Gson gsonResult = builderResult.create();
                                GetVComUPIPublicationResponse output = gsonResult.fromJson(response, GetVComUPIPublicationResponse.class);

                                List<VComUPIPublication> publications = null;
                                if (output.isSuccess() && output.getData().getTotalCount() > 0) {
                                    publications = output.getData().getvComUPIPublication();
                                }

                                sendPublishMessage(MyAppConfig.EVENT_BUS_MESSAGE.PUBLICATIONS_RELOADED, publications);
                                isPublicationReloading = false;
                            }


                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.PUBLICATIONS_RELOADED_FAIL);
                                isPublicationReloading = false;
                            }
                        }
                );
                AppController.getInstance().addToRequestQueue(stringRequest, MyAppConfig.VOLLEY_TAG.MANIPULATE_UPI);
            }
        }
    }


    private void subsbribeComposite(VComUserRole role) {
        User user = AppController.getInstance().getOnlineUser();
        if( user != null && role != null ){
            String request_url = MyAppConfig.getInstance().prepareWebService("User");
            request_url += "/"+user.getId()+"/vComUserRoles/"+role.getId();


            StringRequest stringRequest = new MyStringRequestV2(
                    StringRequest.Method.PUT,
                    user,
                    request_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            GsonBuilder builderResult = new GsonBuilder();
                            builderResult.registerTypeAdapter(Boolean.class, new GSonBooleanAsIntAdapter()).registerTypeAdapter(boolean.class , new GSonBooleanAsIntAdapter());
                            builderResult.setDateFormat("yyyy-MM-dd HH:mm:ss");
                            Gson gsonResult = builderResult.create();

                            PostUserResponse output = gsonResult.fromJson(response, PostUserResponse.class);
                            if( output.isSuccess() ) {
                                sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.SUBSCRIBE_SUCCESS);
                            }else{
                                sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.SUBSCRIBE_FAIL);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(MyAppConfig.LOG.VComService,"EVENT_BUS_MESSAGE.SUBSCRIBE_FAIL"+error.getMessage());
                            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.SUBSCRIBE_FAIL);
                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(stringRequest, MyAppConfig.VOLLEY_TAG.MANIPULATE_VCOM);
        }else{
            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.SUBSCRIBE_FAIL);
        }


    }


    //////////////////////////////////////////////////////////////////////
    // Event BUS
    ///////////////////////////////////////////////////////////////////


    public void onEvent(MyMessage message) {
        Log.i(MyAppConfig.LOG.VComService,"onEvent(MyMessage "+message.getMessage()+")");
        if (message.getSender().equals(MyService.class.getSimpleName())) {
            if (message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_DONE)) {
                detectUserComposite();
            }
        }
        if( message.getSender().equals(FragmentVComCompositeList.class.getSimpleName())){
            if(message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.SUBSCRIBE_COMPOSITE)){
                if(message.getRole() != null  ){
                    subsbribeComposite(message.getRole());
                }
            }
        }
        if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.RELOAD_BASE)){
            loadBases();
        }
        if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.LOAD_PUBLICATIONS)){
            reloadPublication();
        }


    }

    public void onEvent(MyPublishMessage message){
        Log.i(MyAppConfig.LOG.VComService,"onEvent(MyPublishMessage "+message.getMessage()+")");
        if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.PUBLISH_UPI )){
            if(message.getPublishRule() != null ){
                savePublication(message.getPublication());
                /*
                //TODO - Remover excesso de parâmetros - Usar somente o publication
                this.createPublication(
                        message.getBase(),
                        message.getUpi(),
                        message.getPublishRule(),
                        message.getPosition()
                );
                */
            }else{
                this.createResponse(
                        message.getBase(),
                        message.getUpi(),
                        message.getResponseRule(),
                        message.getPosition()
                );
            }
        }


    }


    public void sendEventBusMessage(String strMessage){
        Log.i(MyAppConfig.LOG.VComService, strMessage);
        AppController app =AppController.getInstance();
        MyMessage message = new MyMessage(MyVComService.class.getSimpleName(),strMessage);
        if( app != null ) {
            if (app.getMyComposite() != null) {
                message.setMyComposites(AppController.getInstance().getMyComposite());
            }
            if (AppController.getInstance().getAllComposites() != null) {
                message.setComposites(AppController.getInstance().getAllComposites());
            }
        }
        message.setSender(MyVComService.class.getSimpleName());
        EventBus.getDefault().post(message);
    }

    private void sendPublishMessage(String strMessage, List<VComUPIPublication> publications) {

        Log.i(MyAppConfig.LOG.VComService,strMessage);

        if( strMessage.equals(MyAppConfig.EVENT_BUS_MESSAGE.PUBLICATIONS_RELOADED)) {
            MyPublishMessage message = new MyPublishMessage(MyVComService.class.getSimpleName(), MyAppConfig.EVENT_BUS_MESSAGE.PUBLICATIONS_RELOADED);
            message.setPublications(publications);
            EventBus.getDefault().post(message);
        }
    }



}
