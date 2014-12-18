package fr.xiaomi.sampleserviceonboot;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by xiaomi on 14/11/14.
 * <p/>
 * Dialog that display a date picker and a time picker that
 */
public class TimePickerDialog extends DialogFragment implements
        View.OnClickListener,
        DatePicker.OnDateChangedListener,
        TimePicker.OnTimeChangedListener {

    //==============================================================================================
    // Constants                                                                                   =
    //==============================================================================================

    private static final String TAG = TimePickerDialog.class.getSimpleName();


    //==============================================================================================
    // Interface                                                                                   =
    //==============================================================================================

    public interface OnTimePickerListener {
        public void onCancel();

        public void onSubmit(Date date);
    }

    //==============================================================================================
    // Variables                                                                                   =
    //==============================================================================================

    private OnTimePickerListener callback;

    private Button submit;
    private Button cancel;

    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    //==============================================================================================
    // Constructor                                                                                 =
    //==============================================================================================

    public static TimePickerDialog newInstance(Date date, OnTimePickerListener callback) {
        TimePickerDialog fragment = new TimePickerDialog();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        fragment.year = c.get(Calendar.YEAR);
        fragment.month = c.get(Calendar.MONTH);
        fragment.day = c.get(Calendar.DAY_OF_MONTH);
        fragment.hour = c.get(Calendar.HOUR_OF_DAY);
        fragment.minute = c.get(Calendar.MINUTE);

        fragment.callback = callback;

        return fragment;
    }

    //==============================================================================================
    // Dialog Fragment's methods                                                                   =
    //==============================================================================================


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.time_picker_dialog, container, false);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Time Picker");

        return dialog;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        submit = (Button) view.findViewById(R.id.date_time_submit);
        cancel = (Button) view.findViewById(R.id.cancel);

        calendar = Calendar.getInstance();

        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        timePicker.setOnTimeChangedListener(this);
        datePicker.init(year, month, day, this);

    }

    @Override
    public void onStart() {
        super.onStart();

        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    //==============================================================================================
    // Callbacks                                                                                   =
    //==============================================================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                callback.onCancel();
                break;

            case R.id.date_time_submit:

                callback.onSubmit(calendar.getTime());
                break;
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.d(TAG, "year:" + year);
        Log.d(TAG, "month:" + monthOfYear);
        Log.d(TAG, "day:" + dayOfMonth);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        Log.d(TAG, "hour:" + hourOfDay);
        Log.d(TAG, "minute:" + minute);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
    }
}
