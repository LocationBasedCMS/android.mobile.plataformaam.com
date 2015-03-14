package com.plataformaam.mobile.clientefinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.plataformaam.mobile.clientefinal.configurations.MyAppConfiguration;


public class SplashScreen extends ActionBarActivity {
    //BroadCast Reciever
    BroadcastReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d(SplashScreen.class.getSimpleName(), "OnReceiver");

                Intent intentPanel = new Intent( getApplicationContext() , GlobalPanelUI.class);
                intentPanel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentPanel);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(MyAppConfiguration.BroadCastMessage.SERVICE_READY));
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(MyAppConfiguration.BroadCastMessage.SERVICE_READY));
        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }
}
