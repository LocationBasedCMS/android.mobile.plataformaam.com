package com.plataformaam.mobile.clientefinal.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by bernard on 20/01/2015.
 */
public class ReadyServiceBroadcastReceiver extends BroadcastReceiver {
    //DEVE INICIALIZAR O PAINEL QUANDO O SERVICE ESTIVER PRONTO.
    public void onReceive(Context context, Intent intent) {
       // Intent intentPanel = new Intent(context.getApplicationContext(), GlobalPanelUI.class);
       // intentPanel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       // context.startActivity(intentPanel);
    }
}
