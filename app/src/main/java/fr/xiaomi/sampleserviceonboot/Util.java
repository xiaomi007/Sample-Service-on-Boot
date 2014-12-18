package fr.xiaomi.sampleserviceonboot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xiaomi on 14/12/18.
 */
public class Util {

    private static final String TAG = Util.class.getSimpleName();

    public static void setAlarm(Context context, Date date, boolean isStartDate){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WakeUpOnBoot.class);
        intent.putExtra("service", isStartDate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.SECOND, 0);
        Date newDate = c.getTime();

        long timeElapsed = SystemClock.elapsedRealtime() + (newDate.getTime() - Calendar.getInstance().getTimeInMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
        Log.d(TAG, format.format(new Date(Calendar.getInstance().getTimeInMillis() + timeElapsed - SystemClock.elapsedRealtime())));
        PendingIntent pendingAlarm;
        if(isStartDate) {
            pendingAlarm = PendingIntent.getBroadcast(context, 0, intent, 0);
        } else {
            pendingAlarm = PendingIntent.getBroadcast(context, 1, intent, 0);
        }

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeElapsed, pendingAlarm);
    }

}
