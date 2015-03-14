package com.plataformaam.mobile.clientefinal.helpers.volley;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.plataformaam.mobile.clientefinal.AppController;
import com.plataformaam.mobile.clientefinal.models.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bernard on 04/03/2015.
 */
public class MyPostStringRequest extends StringRequest{
    User user;
    IObjectToPost objectToPost;
    String postJson;


    public MyPostStringRequest(String url,User user, IObjectToPost objectToPost, Response.Listener<String> listener, Response.ErrorListener errorListener ) {
        super(Request.Method.POST , url, listener, errorListener);
        this.user = user;
        this.objectToPost = objectToPost;
    }

    public MyPostStringRequest(String url,  IObjectToPost objectToPost, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST , url, listener, errorListener);
        this.user = AppController.getInstance().getOnlineUser();
        this.objectToPost = objectToPost;
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

    @Override
    public byte[] getBody() throws AuthFailureError {
        postJson = objectToPost.generatePostJson();
        return  objectToPost.generatePostJson().getBytes();
    }

}
