package com.ejemplo.appdocente.Util;

import android.content.Context;
import android.content.DialogInterface;

import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.ejemplo.appdocente.R;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

public class TimePicker extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    TimePicker.TimePickerInterface mListener;
    public interface TimePickerInterface {
        void OnSetTimePicker(int startHour , int endMinute, int startFinalHour, int endFinalMinute);
    }

    private Context context;
    private FragmentActivity fragmentActivity;
    private FormatDate format;

    public TimePicker(Context context, final WeakReference<FragmentActivity> mReference, TimePickerInterface timePickerInterface) {
        this.context = context;
        this.fragmentActivity = mReference.get();
        mListener =  timePickerInterface;
        format = new FormatDate();
    }

    public void getTimePicker() {
        TimeZone timeZone = TimeZone.getTimeZone("America/Montevideo");
        Calendar now = Calendar.getInstance();
        now.setTimeZone(timeZone);
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setTabIndicators(context.getResources().getString(R.string.filtro_hora_desde), context.getResources().getString(R.string.filtro_hora_hasta));
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
            }
        });
        tpd.show(fragmentActivity.getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {
        int hourString = Integer.parseInt(hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay);
        int minuteString = Integer.parseInt(minute < 10 ? "0"+minute : ""+minute);
        int hourStringEnd = Integer.parseInt(hourOfDayEnd < 10 ? "0"+hourOfDayEnd : ""+hourOfDayEnd);
        int minuteStringEnd = Integer.parseInt(minuteEnd < 10 ? "0"+minuteEnd : ""+minuteEnd);

        if(mListener!=null)
            mListener.OnSetTimePicker(hourString, minuteString, hourStringEnd, minuteStringEnd);
    }
}
