package com.example.myunghak.everyday_live_logger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.GoogleMap;

import java.util.Calendar;

/**
 * Created by myunghak on 2015-12-19.
 */


public class Add_schedule_sub extends DialogFragment  implements TimePickerDialog.OnTimeSetListener {


    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, this,hour,minute, DateFormat.is24HourFormat(getActivity()));

        TextView tvTitle = new TextView(getActivity());
        tvTitle.setText("TimepickerDialog 타이틀");
        tvTitle.setBackgroundColor(Color.parseColor("#ffEEE8AA"));
        tvTitle.setPadding(5, 3, 5, 3);
        tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        tpd.setCustomTitle(tvTitle);

        return tpd;



    }


    public void onTimeSet(TimePicker view, int hourofDay, int minute) {

        TextView tv =(TextView) getActivity().findViewById(R.id.t_time);

        Button bt =(Button) getActivity().findViewById(R.id.bt_time);


        final Calendar c = Calendar.getInstance();
        int Hour = c.get(Calendar.HOUR_OF_DAY);
        int Minute = c.get(Calendar.MINUTE);


        if(bt.getText().toString()=="끝난시간") {
            tv.setText(tv.getText()+String.valueOf(hourofDay)+"시"+String.valueOf(minute)+"분\n");
            bt.setText("시작시간");
        }
        else{
            tv.setText(String.valueOf(hourofDay) + "시" + String.valueOf(minute) + "분" + "~");
            bt.setText("끝난시간");
        }

        }
}


