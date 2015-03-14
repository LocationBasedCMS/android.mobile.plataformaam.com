package com.plataformaam.mobile.clientefinal.asynctasks;

import android.os.AsyncTask;

import com.plataformaam.mobile.clientefinal.configurations.MyAppConfiguration;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.location.UserPosition;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MyAsyncTaskDoLogin extends AsyncTask<User,Void,Void> {
    //WEBSERVICE
    protected String webService = MyAppConfiguration.getInstance().getWebService()+"User";//filter=";

    //INFORMACOES
    public final int OPERATION_ERROR = -1;
    public final int OPERATION_NOT_COMPLETE = 0;
    public final int OPERATION_SUCCESS = 1;
    protected int RESULT = OPERATION_NOT_COMPLETE;
    protected User user;
    protected UserPosition userPosition;


    @Override
    protected Void doInBackground(User... params) {
        /*
        this.user = params[0];
        if( this.user != null ){

            //android.os.Debug.waitForDebugger();

            MyFilterItem filterLogin = new MyFilterItem("login",this.user.getLogin(),"=");
            MyFilterItem filterPassword = new MyFilterItem("password",this.user.getPassword(),"=");
            MyFilter filter = new MyFilter();
            filter.addFilter(filterLogin);
            filter.addFilter(filterPassword);

            Gson gson = new GsonBuilder().create();
            String jsonFilter = gson.toJson(filter.getFilter(),    new TypeToken<List<MyFilterItem>>(){}.getType()  );

            HttpResponse response = makeRequest(webService,jsonFilter);
            if( response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED || response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ){
                try {

                    String result = EntityUtils.toString(response.getEntity());
                    Gson gsonResult = new Gson();
                    MyJsonResponseSuccessUser userSuccessResponse = gsonResult.fromJson(result, MyJsonResponseSuccessUser.class);

                    User u =  userSuccessResponse.getData().getUser().get(1);
                    MyAppData.getInstance().setUser(u);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RESULT = OPERATION_SUCCESS;


            } else {
                RESULT = OPERATION_ERROR;
            }
        } else {
            RESULT = OPERATION_ERROR;
        }
        */
        return null;
    }

    public HttpResponse makeRequest(String uri, String json) {
        try {
            //android.os.Debug.waitForDebugger();
            HttpGet httpPost = new HttpGet(uri);
            httpPost.getParams().setParameter("filter", json);

            /// if( json != null )     	httpPost.setEntity(new StringEntity(json));

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("HTTP_X_REST_USERNAME", user.getLogin() );
            httpPost.setHeader("HTTP_X_REST_PASSWORD", user.getPassword() );

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
