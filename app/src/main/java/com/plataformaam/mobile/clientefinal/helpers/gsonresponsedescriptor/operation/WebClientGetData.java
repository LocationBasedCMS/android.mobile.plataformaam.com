package com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.operation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfiguration;
import com.plataformaam.mobile.clientefinal.helpers.MakeRequestHelper;
import com.plataformaam.mobile.clientefinal.helpers.MyFilter;
import com.plataformaam.mobile.clientefinal.helpers.MyFilterItem;
import com.plataformaam.mobile.clientefinal.services.usercontrol.IUserController;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by bernard on 31/01/2015.
 */
public class WebClientGetData<MODEL,DESCRIPTOR> {
    final IUserController userControl;
    final Class<MODEL> typeOfModel;
    final Class<DESCRIPTOR> typeOfDescriptor;

    List<MODEL> data;
    DESCRIPTOR descriptor;

    String webservice;
    MyFilter filter = new MyFilter();

    public WebClientGetData(Class<MODEL> typeOfModel,Class<DESCRIPTOR> typeOfDescriptor, MyFilter filter, IUserController userControl) {
        this.typeOfModel = typeOfModel;
        this.typeOfDescriptor = typeOfDescriptor;
        this.filter = filter;
        this.prepareWebService();
        this.userControl=userControl;
    }

    public WebClientGetData(Class<MODEL> typeOfModel,Class<DESCRIPTOR> typeOfDescriptor, IUserController userControl ) {
        this.typeOfModel = typeOfModel;
        this.typeOfDescriptor = typeOfDescriptor;
        this.prepareWebService();
        this.userControl=userControl;
    }

    public WebClientGetData(Class<MODEL> typeOfModel,Class<DESCRIPTOR> typeOfDescriptor,  MyFilter filter, IUserController userControl,boolean TestUser) {
        this.typeOfModel = typeOfModel;
        this.typeOfDescriptor = typeOfDescriptor;
        this.filter = filter;
        this.prepareWebService();
        this.userControl=userControl;
        if( TestUser)
            this.setOnlineUserFilter();

    }

    public WebClientGetData(Class<MODEL> typeOfModel,Class<DESCRIPTOR> typeOfDescriptor, IUserController userControl,  boolean TestUser) {
        this.typeOfModel = typeOfModel;
        this.typeOfDescriptor = typeOfDescriptor;
        this.prepareWebService();
        this.userControl=userControl;
        if( TestUser)
            this.setOnlineUserFilter();
    }


    private void prepareWebService(){
        this.webservice = MyAppConfiguration.getInstance().getWebService()+typeOfModel.getSimpleName();
    }

    //MANIPULCACAO DE FILTROS
    public void setOnlineUserFilter(){
        MyFilterItem filterUser = new MyFilterItem("user",String.valueOf(  userControl.getUser().getId() ) ,"=");
        filter.addFilter(filterUser);
    }

    public void addFilter(MyFilterItem myFilterItem){
        filter.addFilter(myFilterItem);
    }


    public DESCRIPTOR  execute(){
        HttpResponse response =null;
        this.prepareWebService();
        GsonBuilder builder = new GsonBuilder();
        //2015-02-01 16:57:17
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");

        Gson gson = builder.create();

        String jsonFilter = gson.toJson(filter.getFilter(),    new TypeToken<List<MyFilterItem>>(){}.getType()  );


        if( userControl != null ) {
            if( userControl.getUser() != null  ) {
                response = MakeRequestHelper.Generate(userControl.getUser(), webservice, jsonFilter).executeGet();
            }else{
                response = MakeRequestHelper.Generate(null, webservice, jsonFilter).executeGetWithoutUser();
            }
        }else{
            response = MakeRequestHelper.Generate(null, webservice, jsonFilter).executeGetWithoutUser();
        }


        if( response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED || response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
            try {

                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, HTTP.UTF_8);

                GsonBuilder builderResult = new GsonBuilder();
                builderResult.setDateFormat("yyyy-MM-dd HH:mm:ss");
                Gson  gsonResult = builderResult.create();

                //Gson gsonResult = new Gson();
                descriptor = gsonResult.fromJson(result,typeOfDescriptor);

                return descriptor;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

