package pt.psychapp.ricardomartins.lallaapp.Notification;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import pt.psychapp.ricardomartins.lallaapp.MainScreens.EntryScreen;
import com.example.ricardomartins.lallaapp.R;

import java.util.Calendar;

public class Service_Notification extends Service {

    private static final String TAG = "NotService";
    private PowerManager.WakeLock mWakeLock;

    private NotificationManager mNM;

    private static final int REQUEST_CODE = 72785;

    private int NOTIFICATION = R.string.Service_Title;
    private SharedPreferences sharedPreferences;


    public class LocalBinder extends Binder {
        Service_Notification getService() {
            return Service_Notification.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_STICKY;
    }

    @Override
    public void onCreate() {

    }

    /**
     * In onDestroy() we release our wake lock. This ensures that whenever the
     * Service stops (killed for resources, stopSelf() called, etc.), the wake
     * lock will be released.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mWakeLock.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void handleIntent(Intent intent) {
        // obtain the wake lock
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        mWakeLock.acquire();

        // check the global background data setting
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (!cm.getBackgroundDataSetting()) {
            stopSelf();
            return;
        }

        sharedPreferences = getSharedPreferences(getString(R.string.Pref_FileName),Context.MODE_PRIVATE);
        // do the actual work, in a separate thread
        new PollTask( sharedPreferences.getBoolean( getString(R.string.Pref_Notification_0), false),
                sharedPreferences.getBoolean( getString(R.string.Pref_Notification_1), false),
                sharedPreferences.getBoolean( getString(R.string.Pref_Notification_2), false),
                this,
                sharedPreferences.getString(getString(R.string.Pref_name), "Hi")).execute();
    }



    private class PollTask extends AsyncTask<Void, Void, Void> {

        private static final int MorningHour = 6;
        private static final int AnoonHour = 12;
        private static final int NightHour = 21;
        private int CurrentPeriod = -1;

        private static final long HourinMili = 1000*60*60;

        Service_Notification service;
        boolean Bmorning, BAnoon, BNight;
        String name;
        Calendar c, next;
        PollTask( boolean bmorning, boolean banoon, boolean bnight, Service_Notification service, String name){
            Bmorning = bmorning;
            BAnoon = banoon;
            BNight = bnight;
            this.service = service;
            this.name = name;
            c= Calendar.getInstance();
            next = Calendar.getInstance();
            //Log.i(TAG, " M-" + Bmorning + " . A=" + BAnoon + " . N=" + BNight);
        }


        @Override
        protected Void doInBackground(Void... params) {
            if( Bmorning || BAnoon || BNight) {
                if (isNotificationNow()) showNotification();
                setAlarm();
            }
            return null;
        }

        /**
         * In here you should interpret whatever you fetched in doInBackground
         * and push any notifications you need to the status bar, using the
         * NotificationManager. I will not cover this here, go check the docs on
         * NotificationManager.
         *
         * What you HAVE to do is call stopSelf() after you've pushed your
         * notification(s). This will:
         * 1) Kill the service so it doesn't waste precious resources
         * 2) Call onDestroy() which will release the wake lock, so the device
         *    can go to sleep again and save precious battery.
         */
        @Override
        protected void onPostExecute(Void result) {
            // handle your data
            stopSelf();
        }

        private boolean isNotificationNow(){
            int hour = c.get(Calendar.HOUR_OF_DAY);
            if( hour == MorningHour && Bmorning){
                CurrentPeriod=0;
                return true;
            }else if (hour == AnoonHour && BAnoon){
                CurrentPeriod=1;
                return true;
            }else if ( hour == NightHour && BNight){
                CurrentPeriod=2;
                return true;
            }
            return false;
        }

        private boolean getNextPeriod(){
            int hour = c.get(Calendar.HOUR_OF_DAY);
            long MilisTillAllarm=0;
            if( hour<MorningHour){
                CurrentPeriod=0;    // Next is morning allarm
                next.set( c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), MorningHour, 0,0);
                MilisTillAllarm= next.getTimeInMillis()-c.getTimeInMillis();
            }else if (hour<AnoonHour){
                CurrentPeriod=1;    // Next is afternoon allarm
                next.set( c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), AnoonHour, 0,0);
                MilisTillAllarm= next.getTimeInMillis()-c.getTimeInMillis();
            }else if (hour< NightHour){
                CurrentPeriod=2;    // Next is night allarm
                next.set( c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), NightHour, 0,0);
                MilisTillAllarm= next.getTimeInMillis()-c.getTimeInMillis();
            }else{
                CurrentPeriod=0; // nest is morning [21,24]
                next.set( c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59,59);
                MilisTillAllarm= HourinMili*MorningHour +  (next.getTimeInMillis()-c.getTimeInMillis() + 1000) ;
            }

            long newtime = c.getTimeInMillis()+ MilisTillAllarm;
            next.setTimeInMillis(newtime);
            //Log.i(TAG, " We are at "+ CurrentPeriod +" and next allarm is" + next.getTime());
            //Log.i(TAG, "Timediff = "+ c.getTimeInMillis() + " + " +MilisTillAllarm);
            //Log.i(TAG, "c = "+ c.getTime());


            int[] notificationOn = new int[]{0,0,0};
            int[] hours = new int[] {6,6,12};
            if( Bmorning){
                notificationOn[0]=1;    // morning allarm is active
            }
            if (BAnoon){
                notificationOn[1]=1;    // noon allarm is active
            }
            if (BNight){
                notificationOn[2]=1;    // night allarm is active
            }
            int j, i;
            for( i = CurrentPeriod, j=0; j<3; j++, i=(i+1)%3 ) {
                if (notificationOn[i] == 1) {
                    //Log.i(TAG, "Next allarm is " + i + " (" + j + ")");
                    break;
                }
            }

            for( i=0; i<j ; i++ ){
                //Log.i(TAG, "Adding for (" + ((i+j)%3)+ ") = " + hours[ (CurrentPeriod+i)%3 ]);
                MilisTillAllarm+= hours[ (CurrentPeriod+i)%3 ]*HourinMili;
            }
            newtime = c.getTimeInMillis()+ MilisTillAllarm;
            next.setTimeInMillis(newtime);
            //Log.i(TAG, "The allarm will sound at" + next.getTime());
            //Log.i(TAG, "Timediff = "+ c.getTimeInMillis() + " + " +MilisTillAllarm);
            //Log.i(TAG, "c = "+ c.getTime());

            return false;
        }

        private String getPeriodText(){
            switch (CurrentPeriod){
                case 0:
                    return getString(R.string.Add_Morning);
                case 1:
                    return getString(R.string.Add_Anoon);
                case 2:
                    return getString(R.string.Add_Night);
                default:
                    return "";
            }
        }


        /**
         * Show a notification while this service is running.
         */
        private void showNotification() {

            Calendar c = Calendar.getInstance();

            // In this sample, we'll use the same text for the ticker and the expanded notification
            CharSequence text = getString(R.string.Service_Text,name, getPeriodText() );

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(service)
                            .setSmallIcon(R.drawable.circle)
                            .setContentTitle(getText(R.string.Service_Title))
                            .setContentText(text);

            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(service, EntryScreen.class);

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(service);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(EntryScreen.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(NOTIFICATION, mBuilder.build());

        }



        private void setAlarm(){
            getNextPeriod();

            Intent intent = new Intent(service, BootReceiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(service, REQUEST_CODE, intent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            alarmManager.cancel(pendingIntent);

            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (next.getTimeInMillis()-System.currentTimeMillis()) , pendingIntent);
            System.out.println("Time Total ----- "+ System.currentTimeMillis() + (next.getTimeInMillis()-System.currentTimeMillis()) );

            next.setTimeInMillis( System.currentTimeMillis() + (next.getTimeInMillis()-System.currentTimeMillis()));
            System.out.println("Allarm will set at "+ next.getTime() );
        }
    }




}
