package com.plataformaam.mobile.clientefinal.userinterfaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.plataformaam.mobile.clientefinal.R;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfiguration;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyMessage;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.services.MyService;

import de.greenrobot.event.EventBus;


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

        //Event Buss register
        EventBus.getDefault().register(UserLoginUI.this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(UserLoginUI.this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_login_ui, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goCreateNewUser(View view) {
        Intent intent = new Intent(UserLoginUI.this, RegisterUserUI.class);
        startActivity(intent);
    }

    public void goToGlobalPanelUI(){
        Intent intent = new Intent(getApplicationContext(),GlobalPanelUI.class);
        startActivity(intent);
        /*
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        */
    }

    public void doLogin(View view) {
        Log.i(MyAppConfiguration.LOG.Activity,"doLogin");

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

        //Criando Usuário
        User test_user = new User();
        test_user.setLogin(login);
        test_user.setPassword(password);


        MyMessage message =new MyMessage();
        message.setSender(UserLoginUI.class.getSimpleName());
        message.setUser(test_user);
        message.setMessage(MyAppConfiguration.EVENT_BUS_MESSAGE.TRY_LOGIN);
        EventBus.getDefault().post(message);


    }



    public void onEvent(MyMessage message){
        // THE SENDER IS THE SERVICE
        if( message.getSender().equals( MyService.class.getSimpleName() ) ){
            Log.i(MyAppConfiguration.LOG.Activity, "onEvent(MyMessage message):" + message.toString());
            if( message.getMessage().equals(MyAppConfiguration.EVENT_BUS_MESSAGE.LOGIN_FAIL )){
                onLoginError();
            }
            if( message.getMessage().equals(MyAppConfiguration.EVENT_BUS_MESSAGE.LOGIN_DONE )) {
                Intent intent = new Intent(UserLoginUI.this,GlobalPanelUI.class);
                startActivity(intent);
            }
        }
    }


    public void onLoginError(){
        Toast.makeText(UserLoginUI.this,R.string.errorLoginFail, Toast.LENGTH_LONG ).show();
        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(UserLoginUI.this  );
        builder.setTitle("Erro de Login");
        builder.setMessage(R.string.errorLoginFail);
        builder.setCancelable(true);

        final AlertDialog dlg = builder.create();

        dlg.show();

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                dlg.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 5000); // after 2 second (or 2000 miliseconds), the task will be active.
        */
    }


}
