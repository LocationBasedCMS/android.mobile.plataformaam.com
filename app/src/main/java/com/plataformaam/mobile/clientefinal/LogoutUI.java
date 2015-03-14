package com.plataformaam.mobile.clientefinal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.plataformaam.mobile.clientefinal.services.VCLocClientService;


public class LogoutUI extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_ui);
        Button btnDoLogout = (Button) findViewById(R.id.btnDoLogout);
        btnDoLogout.setEnabled(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout_ui, menu);
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
        public void onServiceConnected(ComponentName className,IBinder service) {
            VCLocClientService.MyBinder binder = (VCLocClientService.MyBinder) service;
            mService = binder.getService();
            if( mService != null ){
                mBound = true;
               callbackInterface();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    void doBindService(){
        Intent intent = new Intent( LogoutUI.this , VCLocClientService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        if (mBound) {
            mBound = false;
            unbindService(mConnection);
            mService = null;
        }
    }

    public void doLogout(View v){

        mService.doLogout();
        Toast.makeText(getApplicationContext(), "Efetuando Logout." ,Toast.LENGTH_LONG).show();
        Intent intent =  new Intent(LogoutUI.this, StartScreen.class );
        startActivity(intent);

    }

    public void goBack(View v){
        Intent intent =  new Intent(LogoutUI.this, GlobalPanelUI.class);
        startActivity(intent);
    }

    public void callbackInterface(){
        Button btnDoLogout = (Button) findViewById(R.id.btnDoLogout);
        btnDoLogout.setEnabled(true);
        btnDoLogout.setBackgroundColor(Color.RED);

        Button btnGoback = (Button) findViewById(R.id.btnLogoutUIBack);
        btnGoback.setBackgroundColor(Color.GREEN);
    }


}
