package com.plataformaam.mobile.clientefinal.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.plataformaam.mobile.clientefinal.AppController;

import com.plataformaam.mobile.clientefinal.helpers.gson.adapters.GSonBooleanAsIntAdapter;
import com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.model.get.GetUPIResponse;

import com.plataformaam.mobile.clientefinal.userinterfaces.fragments.FragmentDeleteUpiConfirmation;
import com.plataformaam.mobile.clientefinal.userinterfaces.fragments.FragmentEditUpi;
import com.plataformaam.mobile.clientefinal.StartScreen;
import com.plataformaam.mobile.clientefinal.userinterfaces.UserLoginUI;
import com.plataformaam.mobile.clientefinal.userinterfaces.UserLogoutUI;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfig;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyMessage;
import com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.model.get.GetUserResponse;
import com.plataformaam.mobile.clientefinal.helpers.gson.gsonresponsedescriptor.model.post.PostUpiResponse;
import com.plataformaam.mobile.clientefinal.helpers.volley.MyPostStringRequest;
import com.plataformaam.mobile.clientefinal.helpers.volley.MyStringRequestV1;
import com.plataformaam.mobile.clientefinal.helpers.volley.MyStringRequestV2;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class MyService extends Service {

    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(MyService.this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(MyService.this);
    }



    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        //NOT A BOUND SERVICE
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        tryAutoLogin();
        return super.onStartCommand(intent, flags, startId);
    }

    ///////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////
    //  LOGIN
    ///////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////

    public void tryAutoLogin(){
        Log.i(MyAppConfig.LOG.Service, "tryAutoLogin");
        AppController app = AppController.getInstance();
        if( app == null ){
            sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_FAIL);
        } else {
            if( app.getOnlineUser() != null ){
                sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_DONE);
            }else{
                //Aplicativo iniciado e sem usuário logado

                User test_user =  new User();
                test_user.setLogin("");
                test_user.setPassword("");
                //VERIFICA CREDENCIAIS SALVAS
                SharedPreferences sharedpreferences = getSharedPreferences(MyAppConfig.Preferences.PREFERENCE_AUTO_LOGIN, Context.MODE_PRIVATE);
                if (sharedpreferences.contains(MyAppConfig.Preferences.HTTP_X_REST_USERNAME)) {
                    test_user.setLogin(sharedpreferences.getString(MyAppConfig.Preferences.HTTP_X_REST_USERNAME, ""));
                    if (sharedpreferences.contains(MyAppConfig.Preferences.HTTP_X_REST_PASSWORD)) {
                        test_user.setPassword(sharedpreferences.getString(MyAppConfig.Preferences.HTTP_X_REST_PASSWORD, ""));
                    }
                }
                if( !test_user.getPassword().equals("") && !test_user.getLogin().equals("")){
                    doLogin(test_user);
                }else{
                    sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_FAIL);
                }

            }
        }

    }

    public void doLogout(){
        AppController.getInstance().setMyComposite(null);
        AppController.getInstance().setOnlineUser(null);
        SharedPreferences sharedpreferences = getSharedPreferences(MyAppConfig.Preferences.PREFERENCE_AUTO_LOGIN, Context.MODE_PRIVATE);
        sharedpreferences.edit().clear().commit();
        sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGOUT_DONE);
    }


    public void doLogin(final User user){
        if( user != null ) {
            //Prepare a web Request
            String request_url = null;
            try {
                request_url = "http://api.plataformaam.com/v3/index.php/api/User?filter=" + URLEncoder.encode("[{\"property\": \"login\", \"value\" : \""+user.getLogin()+"\", \"operator\": \"=\"}]", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            StringRequest stringRequest = new MyStringRequestV1(
                    Request.Method.GET,
                    user,
                    request_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            GsonBuilder builderResult = new GsonBuilder();
                            builderResult.setDateFormat("yyyy-MM-dd HH:mm:ss");
                            Gson gsonResult = builderResult.create();
                            builderResult.registerTypeAdapter(Boolean.class, new GSonBooleanAsIntAdapter()).registerTypeAdapter(boolean.class , new GSonBooleanAsIntAdapter());

                            GetUserResponse output = gsonResult.fromJson(response,GetUserResponse.class);
                            if( output.isSuccess() && output.getData() != null && output.getData().getTotalCount() > 0 ){
                                AppController.getInstance().setOnlineUser(output.getData().getUser().get(0));
                                AppController.getInstance().getOnlineUser().setPassword(user.getPassword());
                                getUPIfromWebservice();
                            }else{
                                sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_FAIL);

                            }
                        }
                    }
                    ,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_FAIL);
                        }
                    }

            );
            AppController.getInstance().addToRequestQueue(stringRequest, MyAppConfig.VOLLEY_TAG.LOGIN);
        }else{
            sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_FAIL);
        }

    }

    ///////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////
    //UPI
    ///////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////

    public void saveUpi(UPI upi){
        if( upi.getId() > 0 ){
            updateUPI(upi);
        }else{
            createUPI(upi);
        }
    }


    private void createUPI(final UPI upi){
        String request_url = MyAppConfig.getInstance().prepareWebService("UPI");
        StringRequest stringRequest = new MyPostStringRequest(
                request_url,
                upi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        GsonBuilder builderResult = new GsonBuilder();
                        builderResult.setDateFormat("yyyy-MM-dd HH:mm:ss");
                        Gson gsonResult = builderResult.create();
                        PostUpiResponse output = gsonResult.fromJson( response, PostUpiResponse.class);
                        if( output.isSuccess() && output.getData().getTotalCount() == 1   ) {
                            UPI savedUpi = output.getData().getuPI();
                            sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_SUCCESS,savedUpi);
                        }else{
                            sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL);

                        }
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(MyAppConfig.LOG.Service,MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL+":"+error.getMessage());
                        sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL);

                    }
                }
        );
        AppController.getInstance().addToRequestQueue(stringRequest, MyAppConfig.VOLLEY_TAG.MANIPULATE_UPI);
    }

    private void updateUPI(UPI upi) {
        String request_url = MyAppConfig.getInstance().prepareWebService("UPI");
        request_url += "/"+upi.getId();
        StringRequest stringRequest = new MyStringRequestV2(
                StringRequest.Method.PUT,
                user,
                request_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        GsonBuilder builderResult = new GsonBuilder();
                        builderResult.setDateFormat("yyyy-MM-dd HH:mm:ss");
                        Gson gsonResult = builderResult.create();
                        PostUpiResponse output = gsonResult.fromJson( response, PostUpiResponse.class);

                        if( output.isSuccess() && output.getData().getTotalCount() == 1   ) {
                            UPI savedUpi = output.getData().getuPI();
                            sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_SUCCESS,  savedUpi);
                        }else{
                            sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL);
                        }
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(MyAppConfig.LOG.Service,MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL+":"+error.getMessage());
                        sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL);
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(stringRequest, MyAppConfig.VOLLEY_TAG.MANIPULATE_UPI);

    }

    private void deleteUpi(UPI upi) {
        if( upi != null && upi.getId()>0 ) {
            String request_url = MyAppConfig.getInstance().prepareWebService("UPI/" + upi.getId());
            StringRequest stringRequest = new MyStringRequestV2(
                    Request.Method.DELETE,
                    AppController.getInstance().getOnlineUser(),
                    request_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            GsonBuilder builderResult = new GsonBuilder();
                            builderResult.setDateFormat("yyyy-MM-dd HH:mm:ss");
                            Gson gsonResult = builderResult.create();
                            PostUpiResponse output = gsonResult.fromJson( response, PostUpiResponse.class);
                            if( output.isSuccess() && output.getData().getTotalCount() == 1   ) {
                                UPI deletedUpi = output.getData().getuPI();
                                for(int i =0 ; i < AppController.getInstance().getOnlineUser().getUpis().size() ; i++ ){
                                    if( deletedUpi.getId() == AppController.getInstance().getOnlineUser().getUpis().get(i).getId() ){
                                        AppController.getInstance().getOnlineUser().getUpis().remove(i);
                                        i = AppController.getInstance().getOnlineUser().getUpis().size();
                                    }
                                }
                                sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_SUCCESS,deletedUpi);
                            }else{
                                sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(MyAppConfig.LOG.Service,MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL+":"+error.getMessage());
                            sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL);
                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(stringRequest, MyAppConfig.VOLLEY_TAG.MANIPULATE_UPI);

        }else{
            sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL);
        }
    }


    private boolean isUpiReloading = false;
    private void getUPIfromWebservice(){
        if( !isUpiReloading) {
            isUpiReloading = true;
            Log.i(MyAppConfig.LOG.Service,"getUPIfromWebservice()");

            final User user = AppController.getInstance().getOnlineUser();
            String request_url = MyAppConfig.getInstance().prepareWebService("UPI", "[{\"property\": \"user\", \"value\" : \"" + user.getId() + "\", \"operator\": \"=\"}]");
            StringRequest stringRequest = new MyStringRequestV2(
                    StringRequest.Method.GET,
                    user,
                    request_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            GsonBuilder builderResult = new GsonBuilder();
                            builderResult.setDateFormat("yyyy-MM-dd HH:mm:ss");
                            Gson gsonResult = builderResult.create();
                            GetUPIResponse output = gsonResult.fromJson(response, GetUPIResponse.class);

                            if (output.isSuccess() && output.getData().getTotalCount() > 0) {



                                Map<Integer, UPI> map = new HashMap<>();
                                List<UPI> upis = output.getData().getuPI();
                                for (UPI upi : upis) {
                                    map.put(upi.getId(), upi);
                                }
                                //Atualiza as UPIS do Usuário
                                List<UPI> userUpis = AppController.getInstance().getOnlineUser().getUpis();
                                if( userUpis == null ){
                                    userUpis = new ArrayList<>();
                                }
                                for (int i = 0, upiSize = userUpis.size(); i < upiSize; i++) {
                                    UPI upi = map.get(AppController.getInstance().getOnlineUser().getUpis().get(i).getId());
                                    upi.setUser(AppController.getInstance().getOnlineUser());
                                    userUpis.set(i, upi);
                                }
                                AppController.getInstance().getOnlineUser().setUpis(userUpis);

                            }

                            sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_RELOADED_SUCCESS);
                            isUpiReloading = false;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(MyAppConfig.LOG.Service, MyAppConfig.EVENT_BUS_MESSAGE.UPI_RELOADED_FAIL+":"+error.getMessage());
                            sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_RELOADED_FAIL);
                            isUpiReloading = false;
                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(stringRequest, MyAppConfig.VOLLEY_TAG.MANIPULATE_UPI);

        }
        sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_DONE);
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //  EventBus Mensages
    /////////////////////////////////////////////////////////////////////////////////////////////







    //Para criar ações no serviço, primeiramente se cria um conjunto de mensagens. MyAppConfiguration.EVENT_BUS_MESSAGE
    public void onEvent(MyMessage message){
        //LOGIN REQUEST
        if(message.getSender().equals(StartScreen.class.getSimpleName())  ||  message.getSender().equals(UserLoginUI.class.getSimpleName())   ) {
            if (message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.TRY_AUTO_LOGIN)) {
                tryAutoLogin();
            }
            if (message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.TRY_LOGIN)) {
                User u = message.getUser();
                doLogin(u);
            }
        }
        //LOGOUT REWQUEST
        if(message.getSender().equals( UserLogoutUI.class.getSimpleName() ) ) {
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.TRY_LOGOUT ) ) {
                doLogout();
            }
        }
        //UPI SAVE
        if( message.getSender().equals(FragmentEditUpi.class.getSimpleName())){
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.SAVE_UPI)){
                saveUpi(message.getUpi());
            }
        }
        //UPI DELETE
        if( message.getSender().equals(FragmentDeleteUpiConfirmation.class.getSimpleName())   )  {
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.DELETE_UPI)){
                deleteUpi(message.getUpi());
            }
        }
    }


    public void sendEventBusMessage(String strMessage, User user, List<VComComposite> composites, UPI upi){
        MyMessage message = new MyMessage(MyService.class.getSimpleName(),strMessage);
        if( user != null ){
            message.setUser(user);
        }
        if( composites != null ){
            message.setComposites(composites);
        }
        if( upi != null ){
            message.setUpi(upi);
        }
        Log.i(MyAppConfig.LOG.Service,"sendEventBusMessage(String "+strMessage+")");
        EventBus.getDefault().post(message);
    }

    private void sendMessage(String strMessage,UPI upi) {
        if( strMessage.equals(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_SUCCESS)) {
            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_SUCCESS, AppController.getInstance().getOnlineUser(), null, upi);
        }

    }

    private void sendMessage(String strMessage) {
        if( strMessage.equals(MyAppConfig.EVENT_BUS_MESSAGE.UPI_RELOADED_SUCCESS)) {
            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_RELOADED_SUCCESS, null, null, null);
        }
        if( strMessage.equals(MyAppConfig.EVENT_BUS_MESSAGE.UPI_RELOADED_FAIL) ) {
            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_RELOADED_FAIL, null, null, null);
        }
        if(strMessage.equals(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_DONE) ) {
            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_DONE, AppController.getInstance().getOnlineUser(), null, null);
        }
        if(strMessage.equals(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_FAIL) ) {
            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_FAIL, null,null, null);
        }
        if(strMessage.equals(MyAppConfig.EVENT_BUS_MESSAGE.LOGOUT_DONE) ) {
            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGOUT_DONE, null, null, null);
        }
        if(strMessage.equals(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL) ) {
            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL, AppController.getInstance().getOnlineUser(), null, null);
        }
    }




}
