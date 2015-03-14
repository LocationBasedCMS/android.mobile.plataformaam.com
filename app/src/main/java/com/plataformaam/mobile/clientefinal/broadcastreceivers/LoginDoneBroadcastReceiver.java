package com.plataformaam.mobile.clientefinal.broadcastreceivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.plataformaam.mobile.clientefinal.GlobalPanelUI;

/**
 * Created by bernard on 18/01/2015.
 */
public class LoginDoneBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotification(context,intent);
    }


    public void createNotification(Context context,Intent intent){
        //TO-DO :
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, GlobalPanelUI.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("Plataforma AM")
                        .setContentText(" Login Efetuado com sucesso ! ");

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }


}
