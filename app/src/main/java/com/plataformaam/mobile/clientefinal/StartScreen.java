package com.plataformaam.mobile.clientefinal;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.RequestQueue;

import com.plataformaam.mobile.clientefinal.configurations.MyAppConfiguration;
import com.plataformaam.mobile.clientefinal.services.VCLocClientService;
import com.plataformaam.mobile.clientefinal.services.VCLocClientService.MyBinder;




public class StartScreen extends ActionBarActivity {

    BroadcastReceiver receiver;


    static RequestQueue queue;

    //LIFECYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        Log.d(StartScreen.class.getSimpleName(),"onCreate");

        setContentView(R.layout.activity_start_screen);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //SE O AUTO LOGIN FUNCIONOU REDIRECIONARÁ
                if (mService.getUser() != null) {
                    Intent intentPanel = new Intent(getApplicationContext(), GlobalPanelUI.class);
                    intentPanel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentPanel);
                } else {
                    Intent intentDoLogin = new Intent(getApplicationContext(), UserLoginUI.class);
                    intentDoLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentDoLogin);
                }

            }
        };
        verifyGPS();


        //INICIALIZA O SERVIÇO
        Log.d(StartScreen.class.getSimpleName(),"onCreate -> startService(VCLOC_CLIENT_SERVICE) ");
        Intent intentService = new Intent("VCLOC_CLIENT_SERVICE");
        startService(intentService);
    }



    @Override
    protected void onStart() {
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(MyAppConfiguration.BroadCastMessage.AUTO_LOGIN_DONE));
        super.onStart();

    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    protected void onResume(){
        Intent intent = new Intent(this,VCLocClientService.class);
        bindService(intent,mConnection,Context.BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    protected void onPause(){
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        super.onPause();
    }
    //LIFECYCLE
    //LIFECYCLE




    //BUSINESS METHODS
    private void verifyGPS(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Toast.makeText(getApplicationContext(), R.string.errorGPSDisable, Toast.LENGTH_LONG).show();

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage(R.string.dialogEnableGPS)
                    .setCancelable(false)
                    .setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
                                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                    Intent intentLocation = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
                                    intentLocation.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intentLocation);
                                }
                            }
                    )
                    .setNegativeButton(R.string.btnCancel, new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }

    }




    VCLocClientService mService;
    boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyBinder binder = (MyBinder) service;
            mService = binder.getService();
            mBound = true;

            //EFETUA O AUTO LOGIN
            mService.doAutoLogin();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };


    //BUSINESS METHODS
    //BUSINESS METHODS

}
