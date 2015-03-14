package com.plataformaam.mobile.clientefinal.helpers;

import com.plataformaam.mobile.clientefinal.models.User;


import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


/**
 * Created by bernard on 18/01/2015.
 */
public class MakeRequestHelper {
    User user;
    String url;
    String json;

    public MakeRequestHelper() {

    }

    public static MakeRequestHelper Generate(User user,String uri,String json){
        MakeRequestHelper makeRequestHelper = new MakeRequestHelper();

        makeRequestHelper.user = user;
        makeRequestHelper.url =uri;
        makeRequestHelper.json = json;

        return makeRequestHelper;
    }

    public HttpResponse execute(){

        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json));

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("HTTP_X_REST_USERNAME", this.user.getLogin());
            httpPost.setHeader("HTTP_X_REST_PASSWORD", this.user.getPassword() );

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



    public HttpResponse executeGet()  {
        HttpGet httpGet=null;
        HttpResponse response=null;
        AndroidHttpClient defaultHttpClient=null;
        try {
            Uri.Builder b = Uri.parse(url).buildUpon();
            if( json != null ) {
                b.appendQueryParameter("filter", json);
            }
            httpGet = new HttpGet(b.build().toString());


            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            httpGet.setHeader("HTTP_X_REST_USERNAME", this.user.getLogin());
            httpGet.setHeader("HTTP_X_REST_PASSWORD", this.user.getPassword());

            
            defaultHttpClient = AndroidHttpClient.newInstance(url);
            Log.d(MakeRequestHelper.class.getSimpleName(),"\n\texecuteGet : "+defaultHttpClient.toString());
            response =defaultHttpClient.execute(httpGet);
            if( defaultHttpClient != null ) {
                defaultHttpClient.close();
            }
            return response;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         finally {
            if( defaultHttpClient != null ) {
                defaultHttpClient.close();
            }
        }
        return null;
    }


    public HttpResponse executeGetWithoutUser()  {
        HttpGet httpGet;
        HttpResponse response=null;
        AndroidHttpClient defaultHttpClient=null;
        try {
            Uri.Builder b = Uri.parse(url).buildUpon();
            if( json != null ) {
                b.appendQueryParameter("filter", json);
            }
            httpGet = new HttpGet(b.build().toString());


            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            httpGet.setHeader("HTTP_X_REST_USERNAME", "root");
            httpGet.setHeader("HTTP_X_REST_PASSWORD", "qw");


            defaultHttpClient = AndroidHttpClient.newInstance(url);
            response =defaultHttpClient.execute(httpGet);
            Log.d(MakeRequestHelper.class.getSimpleName(),"\n\texecuteGet : "+response.toString());
            if( defaultHttpClient != null ) {
                defaultHttpClient.close();
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if( defaultHttpClient != null ) {
                defaultHttpClient.close();
            }
        }
        return response;
    }

}
