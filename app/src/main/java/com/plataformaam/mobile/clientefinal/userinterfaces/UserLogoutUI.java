package com.plataformaam.mobile.clientefinal.userinterfaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.plataformaam.mobile.clientefinal.R;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfig;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyMessage;
import com.plataformaam.mobile.clientefinal.services.MyService;

import de.greenrobot.event.EventBus;


public class UserLogoutUI extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_logout_ui);
        EventBus.getDefault().register(UserLogoutUI.this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(UserLogoutUI.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void doLogout(View v){
        MyMessage message = new MyMessage(UserLogoutUI.class.getSimpleName(), MyAppConfig.EVENT_BUS_MESSAGE.TRY_LOGOUT);
        EventBus.getDefault().post(message);
        Toast.makeText(getApplicationContext(), getString(R.string.operation_action_logout) , Toast.LENGTH_LONG).show();
    }

    public void goBack(View v){
        Intent intent =  new Intent( UserLogoutUI.this, GlobalPanelUI.class);
        startActivity(intent);
    }

    public void goToLogin(){
        Intent intent =  new Intent(UserLogoutUI.this, UserLoginUI.class);
        startActivity(intent);
    }

    public void onEvent(MyMessage message){
        if( message.getSender().equals(MyService.class.getSimpleName()) ){
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.LOGOUT_DONE)){
                goToLogin();
            }
        }
    }


}
