package pt.psychapp.ricardomartins.lallaapp.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by ricardomartins on 23/01/2017.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, Service_Notification.class);
            PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
            am.cancel(pi);
            // by my own convention, minutes <= 0 means notifications are disabled

            am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + 5*60*1000,
                        5*60*1000, pi);

        }else {
            System.out.println("Alarm Ringing");
            Intent alarmintent =new Intent(context,Service_Notification.class);
            alarmintent.putExtra( "intent_operation",1 );
            context.startService( alarmintent);
        }

    }
}

