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
        if ( app != null && app.getOnlineUser() == null ) {

            User test_user =  new User();
            SharedPreferences sharedpreferences = getSharedPreferences(MyAppConfig.Preferences.PREFERENCE_AUTO_LOGIN, Context.MODE_PRIVATE);
            if (sharedpreferences.contains(MyAppConfig.Preferences.HTTP_X_REST_USERNAME)) {
                test_user.setLogin(sharedpreferences.getString(MyAppConfig.Preferences.HTTP_X_REST_USERNAME, ""));
                if (sharedpreferences.contains(MyAppConfig.Preferences.HTTP_X_REST_PASSWORD)) {
                    test_user.setPassword(sharedpreferences.getString(MyAppConfig.Preferences.HTTP_X_REST_PASSWORD, ""));
                }
            }else{
                test_user.setLogin("");
                test_user.setPassword("");
            }
            if( !test_user.getPassword().equals("") && !test_user.getLogin().equals("")){
                doLogin(test_user);
            }else{
                sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_FAIL, null,null, null);
            }
        }else{
            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_DONE, AppController.getInstance().getOnlineUser(),null,null);
        }
    }

    public void doLogout(){
        AppController.getInstance().setMyComposite(null);
        AppController.getInstance().setOnlineUser(null);
        SharedPreferences sharedpreferences = getSharedPreferences(MyAppConfig.Preferences.PREFERENCE_AUTO_LOGIN, Context.MODE_PRIVATE);
        sharedpreferences.edit().clear().commit();
        sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGOUT_DONE, null, null, null);
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
                                sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_FAIL, null,null, null);
                            }
                        }
                    }
                    ,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_FAIL, null,null, null);
                        }
                    }

            );
            AppController.getInstance().addToRequestQueue(stringRequest, MyAppConfig.VOLLEY_TAG.LOGIN);
        }else{
            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_FAIL, null,null, null);
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
                            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_SUCCESS, AppController.getInstance().getOnlineUser(), null, savedUpi);
                        }else{
                            sendEventBusMessage( MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL, AppController.getInstance().getOnlineUser(),null, null);
                        }
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL, AppController.getInstance().getOnlineUser(), null, null);
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
                            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_SUCCESS, AppController.getInstance().getOnlineUser(), null, savedUpi);
                        }else{
                            sendEventBusMessage( MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL, AppController.getInstance().getOnlineUser(),null, null);
                        }
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL, AppController.getInstance().getOnlineUser(), null, null);
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

                                sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_SUCCESS, AppController.getInstance().getOnlineUser(), null, deletedUpi);
                            }else{
                                sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL, AppController.getInstance().getOnlineUser(), null, null);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL, AppController.getInstance().getOnlineUser(), null, null);
                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(stringRequest, MyAppConfig.VOLLEY_TAG.MANIPULATE_UPI);

        }else{
            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL, AppController.getInstance().getOnlineUser(), null, upi);
        }
    }


    private boolean isUpiReloading = false;
    private void getUPIfromWebservice(){
        if( !isUpiReloading) {
            isUpiReloading = true;
            User user = AppController.getInstance().getOnlineUser();
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
                                Map<Integer, UPI> map = new HashMap<Integer, UPI>();
                                List<UPI> upis = output.getData().getuPI();
                                for (UPI upi : upis) {
                                    map.put(upi.getId(), upi);
                                }
                                //Atualiza as UPIS do Usuário
                                List<UPI> userUpi = AppController.getInstance().getOnlineUser().getUpis();
                                for (int i = 0, upiSize = userUpi.size(); i < upiSize; i++) {
                                    UPI upi = map.get(AppController.getInstance().getOnlineUser().getUpis().get(i).getId());
                                    AppController.getInstance().getOnlineUser().getUpis().set(i, upi);

                                }

                            }
                            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_RELOADED_SUCCESS, null, null, null);
                            Log.d(MyAppConfig.LOG.Service, MyAppConfig.EVENT_BUS_MESSAGE.UPI_RELOADED_SUCCESS);
                            isUpiReloading = false;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.UPI_RELOADED_FAIL, null, null, null);
                            isUpiReloading = false;
                            Log.e(MyAppConfig.LOG.Service, MyAppConfig.EVENT_BUS_MESSAGE.UPI_RELOADED_FAIL+":"+error.getMessage());
                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(stringRequest, MyAppConfig.VOLLEY_TAG.MANIPULATE_UPI);

        }
        sendEventBusMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_DONE, AppController.getInstance().getOnlineUser(), null, null);
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    //  EventBus Mensages
    /////////////////////////////////////////////////////////////////////////////////////////////

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
        Log.i(MyAppConfig.LOG.Service,MyService.class.getSimpleName()+"->sendEventBusMessage(String "+strMessage+")");
        EventBus.getDefault().post(message);
    }






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




}
