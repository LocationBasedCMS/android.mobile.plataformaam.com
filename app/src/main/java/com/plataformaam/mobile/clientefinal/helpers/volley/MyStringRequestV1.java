package com.plataformaam.mobile.clientefinal.helpers.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.plataformaam.mobile.clientefinal.AppController;
import com.plataformaam.mobile.clientefinal.models.User;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bernard on 02/03/2015.
 */
public class MyStringRequestV1 extends StringRequest {
    User user;
    public MyStringRequestV1(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        user = AppController.getInstance().getOnlineUser();
    }

    public MyStringRequestV1(int method,User u, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        user = u;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        headers.put("Content-type", "application/json");
        headers.put("HTTP_X_REST_USERNAME", user.getLogin());
        headers.put("HTTP_X_REST_PASSWORD", user.getPassword());
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("filter", URLEncoder.encode("[{\"property\": \"login\", \"value\" : \"" + user.getLogin() + "\", \"operator\": \"=\"},{\"property\": \"password\", \"value\" : \"" + user.getPassword() + "\", \"operator\": \"=\"}]", "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return params;
    }



}
