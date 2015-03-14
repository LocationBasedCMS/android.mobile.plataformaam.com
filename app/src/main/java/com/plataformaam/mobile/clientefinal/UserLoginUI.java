package com.plataformaam.mobile.clientefinal;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.plataformaam.mobile.clientefinal.configurations.MyAppConfiguration;
import com.plataformaam.mobile.clientefinal.exceptions.InvalidServiceStateException;
import com.plataformaam.mobile.clientefinal.exceptions.UserNotAllowedException;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.services.VCLocClientService;
import com.plataformaam.mobile.clientefinal.services.statecontrol.ServiceStateController;


public class UserLoginUI extends ActionBarActivity {
    String login;
    String password;
    EditText etxLogin;
    EditText etxPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_ui);

        //CARREGANDO DADOS ANTERIORES
        SharedPreferences sharedpreferences = getSharedPreferences(MyAppConfiguration.Preferences.PREFERENCE_AUTO_LOGIN, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(MyAppConfiguration.Preferences.HTTP_X_REST_USERNAME)) {
            ((EditText) findViewById(R.id.etxLoginUserLoginUI)).setText(sharedpreferences.getString(MyAppConfiguration.Preferences.HTTP_X_REST_USERNAME, ""));
            ;
            if (sharedpreferences.contains(MyAppConfiguration.Preferences.HTTP_X_REST_PASSWORD)) {
                ((EditText) findViewById(R.id.etxPasswordUserLoginUI)).setText(sharedpreferences.getString(MyAppConfiguration.Preferences.HTTP_X_REST_PASSWORD, ""));
                ;
            }
        }

        Button btnDoLogin = (Button) findViewById(R.id.btnLoginUserLoginUI);
        btnDoLogin.setEnabled(false);

    }




    @Override
    protected void onStart() {
        //LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(MyAppConfiguration.BroadCastMessage.SERVICE_READY));
        super.onStart();

    }

    @Override
    protected void onStop() {
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }



    @Override
    public void onResume() {
        doBindService();
        super.onResume();
    }

    @Override
    public void onPause() {
        doUnbindService();
        super.onPause();
    }

    //SERVICE BIND
    VCLocClientService mService;
    boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            VCLocClientService.MyBinder binder = (VCLocClientService.MyBinder) service;
            mService = binder.getService();
            if (mService != null) {
                mBound = true;
                callbackInterface();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    void doBindService() {
        Intent intent = new Intent(UserLoginUI.this, VCLocClientService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        if (mBound) {
            mBound = false;
            unbindService(mConnection);
            mService = null;
        }
    }


    public void callbackInterface() {
        if( mService != null ){
            if( mService.getState() == ServiceStateController.LOGGED_USER ){
                //Usuário já logado, redirecionapara o painel ... somente após logout e primeiro uso que a lógica de login funciona...
                // Evita o erro de apertar o back após o login
                startActivity(new Intent(UserLoginUI.this,GlobalPanelUI.class));

            }
        }

        Button btnDoLogout = (Button) findViewById(R.id.btnLoginUserLoginUI);
        btnDoLogout.setEnabled(true);
    }


    public void goCreateNewUser(View view) {
        Intent intent = new Intent(UserLoginUI.this, RegisterUserUI.class);
        startActivity(intent);
    }


    ProgressDialog progressBar;
    private Handler progressBarbHandler = new Handler();
    private int progressBarStatus = 0;
    User user;
    public void doLogin(View view) {
        //OBTENDO LOGIN E PASSWORD
        etxLogin = (EditText) (findViewById(R.id.etxLoginUserLoginUI));
        login = etxLogin.getText().toString();
        etxPassword = (EditText) findViewById(R.id.etxPasswordUserLoginUI);
        password = etxPassword.getText().toString();

        //SALVANDO OS VALORES EM SHAREDPREFERENCES PARA FACILITAR O LOGIN DO USUÁRIO NO FUTURO.
        SharedPreferences sharedpreferences = getSharedPreferences(MyAppConfiguration.Preferences.PREFERENCE_AUTO_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(MyAppConfiguration.Preferences.HTTP_X_REST_USERNAME, login);
        editor.putString(MyAppConfiguration.Preferences.HTTP_X_REST_PASSWORD, password);
        editor.commit();

        //CRIANDO USUÁRIO


        //mService.doAutoLogin();

        progressBar = new ProgressDialog(view.getContext());
        progressBar.setCancelable(false);
        progressBar.setMessage("Efetuando Login ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();


        new Thread(new Runnable() {
            private void increaseProgressBar(int value,int time){
                progressBarStatus = progressBarStatus+value;
                progressBarbHandler.post(new Runnable() { public void run() { progressBar.setProgress(progressBarStatus); }});
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void run() {
                boolean loginDone = false;
                user  = new User();
                increaseProgressBar(5,200);
                user.setPassword(password);
                increaseProgressBar(5,200);
                user.setLogin(login);
                increaseProgressBar(10,500);

                try {
                    loginDone = mService.doLogin(user);
                    increaseProgressBar(60,1000);
                } catch (InvalidServiceStateException e) {
                    e.printStackTrace();
                } catch (UserNotAllowedException e) {
                    e.printStackTrace();
                }
                if( loginDone ) {
                    increaseProgressBar(20,500);
                    goToGlobalPanelUI();
                }else{
                    progressBar.setMessage("Erro no Login ...");
                    for(int i=0 ; i<3 ; i++) {
                        increaseProgressBar(-20,500);
                    }
                }
                progressBar.dismiss();
            }
        }).start();

    }



    public void goToGlobalPanelUI(){
        Intent intent = new Intent(getApplicationContext(),GlobalPanelUI.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);

    }
}