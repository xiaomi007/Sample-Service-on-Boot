package fr.xiaomi.sampleserviceonboot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by xiaomi on 14/12/18.
 *
 * Wakeful Broadcast Receiver which received intent from boot completed or from the alarm manager.
 */
public class WakeUpOnBoot extends WakefulBroadcastReceiver {

    private static final String TAG = WakeUpOnBoot.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent .getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "wake up on boot");
            Toast.makeText(context, "wake up on boot", Toast.LENGTH_LONG).show();

            //Here the alarm is reset, we have to restore it

            SharedPreferences preferences = context.getSharedPreferences("sample", Context.MODE_PRIVATE);
            long startMs = preferences.getLong("start", 0);
            long endMs = preferences.getLong("end", 0);

            Log.d(TAG, "start ms:" + startMs);
            Log.d(TAG, "end ms:" + endMs);

            if (startMs != 0 && endMs != 0 && endMs > Calendar.getInstance().getTimeInMillis()) {
                Util.setAlarm(context, new Date(startMs), true);
                Util.setAlarm(context, new Date(endMs), false);
            }

        } else {
            intent.setClass(context, ServiceToStart.class);
            startWakefulService(context, intent);
        }
    }


}
