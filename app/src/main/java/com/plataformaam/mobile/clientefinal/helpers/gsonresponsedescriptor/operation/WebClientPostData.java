package com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.operation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfiguration;
import com.plataformaam.mobile.clientefinal.helpers.MakeRequestHelper;
import com.plataformaam.mobile.clientefinal.services.usercontrol.IUserController;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by bernard on 01/02/2015.
 */
public class WebClientPostData<MODEL,DESCRIPTOR> {
        final IUserController userControl;
        final Class<MODEL> typeOfModel;
        final Class<DESCRIPTOR> typeOfDescriptor;

        List<MODEL> data;
        DESCRIPTOR descriptor;
        MODEL postObject;

        String webservice;

    public WebClientPostData(Class<MODEL> typeOfModel, Class<DESCRIPTOR> typeOfDescriptor, MODEL postObject,IUserController iUserControl) {
        this.typeOfModel = typeOfModel;
        this.typeOfDescriptor = typeOfDescriptor;
        this.postObject = postObject;
        this.userControl =iUserControl;
    }


    private void prepareWebService(){
        this.webservice = MyAppConfiguration.getInstance().getWebService()+typeOfModel.getSimpleName();
    }



    public void setPostObject(MODEL postObject) {
        this.postObject = postObject;
    }

    /***
     *
     * @return DESCRIPTOR of Class
     * @throws NullPointerException
     * @information Erro Comun - JSON usando "=" ao invéz de ":"
     */
    public DESCRIPTOR  execute() throws NullPointerException{
        if( postObject == null ){
            throw new NullPointerException("WebClientPostData -> execute()  :: Post Data cannot be a Null ");
        }
        this.prepareWebService();

        final GsonBuilder builder = new GsonBuilder();
        //2015-02-01 16:57:17
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");

        Gson gson = builder.create();

        String postData = postObject.toString();
        HttpResponse response = MakeRequestHelper
                .Generate(
                        userControl.getUser() ,
                        this.webservice,
                        postData
                )
                .execute();
        if( response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED || response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
            try {
                String result = EntityUtils.toString(response.getEntity());
                Gson gsonResult = builder.create();
                descriptor = gsonResult.fromJson(result,typeOfDescriptor);

                return descriptor;

            } catch (IOException e) {
                e.printStackTrace();
            }



        }
        return null;
    }

}

