package fr.xiaomi.sampleserviceonboot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String TAG = PlaceholderFragment.class.getSimpleName();

        Button start, stop, valid, reset;
        TextView tvStart, tvEnd;
        TimePickerDialog startDialog, endDialog;
        Date startDate, endDate;
        Intent setStartAlarm, setEndAlarm;


        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            start = (Button) rootView.findViewById(R.id.start_button);
            stop = (Button) rootView.findViewById(R.id.end_button);
            valid = (Button) rootView.findViewById(R.id.valid);
            reset = (Button) rootView.findViewById(R.id.reset);
            tvStart = (TextView) rootView.findViewById(R.id.start_text);
            tvEnd = (TextView) rootView.findViewById(R.id.end_text);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            SharedPreferences preferences = getActivity().getSharedPreferences("sample", MODE_PRIVATE);
            long startMs = preferences.getLong("start", Calendar.getInstance().getTimeInMillis());
            startDate = new Date(startMs);
            long endMs = preferences.getLong("end", Calendar.getInstance().getTimeInMillis());
            endDate = new Date(endMs);

            final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd  HH:mm");
            tvStart.setText(format.format(startDate));
            tvEnd.setText(format.format(endDate));

            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDialog = TimePickerDialog.newInstance(startDate, new TimePickerDialog.OnTimePickerListener() {
                        @Override
                        public void onCancel() {
                            startDialog.dismiss();
                        }

                        @Override
                        public void onSubmit(Date date) {

                            tvStart.setText(format.format(date));
                            startDate = date;
                            startDialog.dismiss();
                        }
                    });

                    startDialog.show(getFragmentManager(), "start");
                }
            });


            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endDialog = TimePickerDialog.newInstance(endDate, new TimePickerDialog.OnTimePickerListener() {
                        @Override
                        public void onCancel() {
                            endDialog.dismiss();
                        }

                        @Override
                        public void onSubmit(Date date) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd  HH:mm");
                            tvEnd.setText(format.format(date));
                            endDate = date;
                            endDialog.dismiss();
                        }
                    });

                    endDialog.show(getFragmentManager(), "stop");
                }
            });

            valid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("sample", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putLong("start", startDate.getTime());
                    editor.putLong("end", endDate.getTime());
                    editor.apply();

                    //setAlarm(startDate, true);
                    //setAlarm(endDate, false);

                    Util.setAlarm(getActivity(), startDate, true);
                    Util.setAlarm(getActivity(), endDate, false);

                    getActivity().finish();
                }
            });

            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDate = Calendar.getInstance().getTime();
                    endDate = Calendar.getInstance().getTime();
                    tvStart.setText(format.format(startDate));
                    tvEnd.setText(format.format(endDate));
                }
            });
        }

        private void setAlarm(Date date, boolean isStarting){

            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getActivity(), WakeUpOnBoot.class);
            intent.putExtra("service", isStarting);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.SECOND, 0);
            Date newDate = c.getTime();

            long timeElapsed = SystemClock.elapsedRealtime() + (newDate.getTime() - Calendar.getInstance().getTimeInMillis());
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
            Log.d(TAG, format.format(new Date(Calendar.getInstance().getTimeInMillis() + timeElapsed - SystemClock.elapsedRealtime())));
            PendingIntent pendingAlarm;
            if(isStarting) {
                pendingAlarm = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
            } else {
                pendingAlarm = PendingIntent.getBroadcast(getActivity(), 1, intent, 0);
            }

            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeElapsed, pendingAlarm);

        }

    }
}
