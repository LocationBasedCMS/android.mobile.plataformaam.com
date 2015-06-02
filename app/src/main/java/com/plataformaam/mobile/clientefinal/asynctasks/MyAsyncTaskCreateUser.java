package com.plataformaam.mobile.clientefinal.asynctasks;


import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfig;
import com.plataformaam.mobile.clientefinal.models.User;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by bernard on 04/01/2015.
 */
public class MyAsyncTaskCreateUser extends AsyncTask<User,Class<User>,Void> {

    protected String webService = MyAppConfig.getInstance().getWebService()+"User";
    protected String defaultLogin =  MyAppConfig.getInstance().getLoginBase();
    protected  String defaultPassword = MyAppConfig.getInstance().getPasswordBase();
    //ReSULTADOS
    public final int OPERATION_ERROR = -1;
    public final int OPERATION_NOT_COMPLET = 0;
    public final int OPERATION_SUCCESS = 1;
    protected int RESULT = 0;




    @Override
    protected Void doInBackground(User... params) {

        //PREPARANDO REQUISIÇÃO - CONSTRUÇÃO DOS FILTROS
        Gson gson = new GsonBuilder().create();
        String postData = gson.toJson(params[0] , User.class);
        Log.v( "CreateNewUserUI","  Enviando dados : "+postData );
        //EFETUANDO A EXECUCAO
        HttpResponse response =  makeRequest(webService, postData);

        if( response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED || response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
            //JsonApiResponse apiResponse = gson.fromJson(result, JsonApiResponse.class);


            RESULT = OPERATION_SUCCESS;
        }

            return null;
    }


    public HttpResponse makeRequest(String uri, String json) {
        try {

            HttpPost httpPost = new HttpPost(uri);

            httpPost.setEntity(new StringEntity(json));

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("HTTP_X_REST_USERNAME", defaultLogin);
            httpPost.setHeader("HTTP_X_REST_PASSWORD", defaultPassword);

            return new DefaultHttpClient().execute(httpPost);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
