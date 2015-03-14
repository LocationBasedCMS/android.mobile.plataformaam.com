package com.plataformaam.mobile.clientefinal.helpers.volley;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.plataformaam.mobile.clientefinal.AppController;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfiguration;
import com.plataformaam.mobile.clientefinal.models.User;


/**
 * Created by bernard on 07/03/2015.
 */
public class MyStringRequestBuilder<MODEL,DESCRIPTOR> {



    public StringRequest build(MODEL model,String filter, Response.Listener<String> listener, Response.ErrorListener errorListener){
        User user = AppController.getInstance().getOnlineUser();
        String request_url = MyAppConfiguration.getInstance().prepareWebService(model.getClass().getSimpleName(),filter);
        StringRequest stringRequest = new MyStringRequestV3<DESCRIPTOR>(
                Request.Method.GET,
                user,
                request_url,
                listener,
                errorListener
        );
        return stringRequest;
    }


}
