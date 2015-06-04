package com.plataformaam.mobile.clientefinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.plataformaam.mobile.clientefinal.configurations.MyAppConfig;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyMessage;
import com.plataformaam.mobile.clientefinal.services.MyLocationService;
import com.plataformaam.mobile.clientefinal.services.MyService;
import com.plataformaam.mobile.clientefinal.services.MyVComService;
import com.plataformaam.mobile.clientefinal.userinterfaces.GlobalPanelUI;
import com.plataformaam.mobile.clientefinal.userinterfaces.UserLoginUI;

import de.greenrobot.event.EventBus;


public class StartScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        //EventBus
        EventBus.getDefault().register(StartScreen.this);
        verifyGPS();

        Intent intent = new Intent(StartScreen.this, MyService.class);
        startService(intent);


        Intent intentLocation = new Intent(StartScreen.this, MyLocationService.class);
        startService(intentLocation);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if( AppController.getInstance().getOnlineUser() != null      ){
            goToPanel();
        }else {
            selectLoginMethod();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(StartScreen.this);
    }




    //BUSINESS METHODS
    private void verifyGPS(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            //Dialog para solictiar o acionamento do GPS
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage(R.string.dialogEnableGPS)
                    .setTitle(getString(R.string.errorGPSDisable))
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
        Log.i(MyAppConfig.LOG.Activity,StartScreen.class.getSimpleName() + " verifyGPS ");
    }

//    private void verifyInternet(){
//        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//        if ( connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ) {
//            Toast.makeText(StartScreen.this,getString(R.string.successInternetDetect),Toast.LENGTH_SHORT ).show();
//        }
//        else if ( connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED || connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
//            //Dialog para solictiar o acionamento do Wifi
//            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder
//                    .setMessage(R.string.dialogEnableInternet)
//                    .setTitle(getString(R.string.errorInternetDisable))
//                    .setCancelable(false)
//                    .setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
//                                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
//                                    Intent intentLocation = new Intent(Settings.ACTION_WIFI_SETTINGS);
//                                    intentLocation.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intentLocation);
//                                }
//                            }
//                    )
//                    .setNegativeButton(R.string.btnCancel, new DialogInterface.OnClickListener() {
//                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
//                            dialog.cancel();
//                        }
//                    });
//            final AlertDialog alert = builder.create();
//            alert.show();
//        }
//        Log.i(MyAppConfig.LOG.Activity,StartScreen.class.getSimpleName() + " verifyInternet ");
//    }


    private void goToUserLogin() {
        Intent intent = new Intent(StartScreen.this, UserLoginUI.class);
        startActivity(intent);
        Intent intentVcom  = new Intent(StartScreen.this, MyVComService.class);
        startService(intentVcom);

    }

    public void goToPanel(){
        Intent intent = new Intent(StartScreen.this,GlobalPanelUI.class);
        startActivity(intent);
        Intent intentVcom  = new Intent(StartScreen.this, MyVComService.class);
        startService(intentVcom);

    }

    public void selectLoginMethod(){
        Log.i(MyAppConfig.LOG.Activity,"selectLoginMethod");
        MyMessage myMessage = new MyMessage();
        myMessage.setSender(StartScreen.class.getSimpleName());
        myMessage.setMessage(MyAppConfig.EVENT_BUS_MESSAGE.TRY_AUTO_LOGIN);
        EventBus.getDefault().post(myMessage);

    }

    public void onEvent(MyMessage message){
        // THE SENDER IS THE SERVICE
        if( message.getSender().equals( MyService.class.getSimpleName() ) ){
            Log.i(MyAppConfig.LOG.Activity, StartScreen.class.getSimpleName()+"->onEvent(MyMessage message):"+message.toString());
            //GO TO USER LOGIN
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_FAIL )){
                goToUserLogin();
            }
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.LOGIN_DONE )) {
                goToPanel();
            }
        }
    }


}
