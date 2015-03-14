package com.plataformaam.mobile.clientefinal.helpers.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.plataformaam.mobile.clientefinal.AppController;
import com.plataformaam.mobile.clientefinal.helpers.gson.adapters.GSonBooleanAsIntAdapter;
import com.plataformaam.mobile.clientefinal.models.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bernard on 02/03/2015.
 */
public class MyStringRequestV3<DESCRIPTOR> extends StringRequest{
    User user;
    Class<DESCRIPTOR> typeOfDescriptor;
    DESCRIPTOR descriptor;
    public final MyStringRequestV3<DESCRIPTOR> request;


    public MyStringRequestV3(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        user = AppController.getInstance().getOnlineUser();
        request = this;
    }

    public MyStringRequestV3(int method, User u, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        user = u;
        request = this;
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        headers.put("Content-type", "application/json");
        headers.put("HTTP_X_REST_USERNAME", user.getLogin() );
        headers.put("HTTP_X_REST_PASSWORD", user.getPassword() );
        return headers;
    }

    public DESCRIPTOR generateResponseObject(String response,Class<DESCRIPTOR> typeOfDescriptor){
        GsonBuilder builderResult = new GsonBuilder();
        builderResult.setDateFormat("yyyy-MM-dd HH:mm:ss");
        builderResult.registerTypeAdapter(Boolean.class, new GSonBooleanAsIntAdapter()).registerTypeAdapter(boolean.class , new GSonBooleanAsIntAdapter());
        Gson gsonResult = builderResult.create();
        DESCRIPTOR descriptor = gsonResult.fromJson(response,typeOfDescriptor);
        return descriptor;
    }



    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return super.getParams();
    }


}
