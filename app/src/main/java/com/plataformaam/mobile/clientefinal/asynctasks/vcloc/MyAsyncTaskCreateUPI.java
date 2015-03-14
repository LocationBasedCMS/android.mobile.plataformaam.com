package com.plataformaam.mobile.clientefinal.asynctasks.vcloc;

import android.os.AsyncTask;

import com.plataformaam.mobile.clientefinal.configurations.MyAppConfiguration;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;

/**
 * Created by bernard on 11/01/2015.
 */
public class MyAsyncTaskCreateUPI extends AsyncTask<UPI,Void,Void> {
    //WEBSERVICE + USER
    protected String webService = MyAppConfiguration.getInstance().getWebService()+"UPI";
    //protected User user = MyAppData.getInstance().getUser();

    //RESULTADOS DA OPERACÃO
    public final int OPERATION_ERROR = -1;
    public final int OPERATION_NOT_COMPLETE = 0;
    public final int OPERATION_SUCCESS = 1;
    protected int RESULT = 0;

    //INFORMACOES
    protected UPI upi;




    @Override
    protected Void doInBackground(UPI... params) {
        /*
        this.RESULT = OPERATION_NOT_COMPLETE;
        this.upi = params[0];

        if( this.user == null || this.upi == null ){
            Log.e("MyAsyncTaskCreateUPI:doInBackground", "LOGIN error/Invalid UPI");
            this.RESULT = OPERATION_ERROR;
        }else{
            String postData = this.upi.toPostJson();
            HttpResponse response = MakeRequestHelper.Generate(user,webService,postData).execute();
            if( response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED || response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
                this.RESULT = OPERATION_SUCCESS;
            }
        }
        */
        
        return null;
    }



}
