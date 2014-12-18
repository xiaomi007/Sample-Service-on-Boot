package fr.xiaomi.sampleserviceonboot;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by xiaomi on 14/12/18.
 */
public class ServiceToStart extends Service {

    private static final String TAG = ServiceToStart.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.hasExtra("service")) {
            Log.d(TAG, "intent has service");
            if(intent.getBooleanExtra("service", false)){
                Log.d(TAG, "start service");
                Toast.makeText(getApplicationContext(), "Start Service", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "end service");
                Toast.makeText(getApplicationContext(), "Stop Service", Toast.LENGTH_LONG).show();
            }
            WakeUpOnBoot.completeWakefulIntent(intent);

        } else {
            Log.d(TAG, "intent hasn't service");
            WakeUpOnBoot.completeWakefulIntent(intent);
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
