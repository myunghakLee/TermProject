package com.example.myunghak.everyday_live_logger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

public class Calender extends AppCompatActivity {
    Double X;
    Double Y;

    public void Add_schedule(int year, int month, int day, double X, double Y) {


        String YMD = ""+year+month+day;//나중에 각 날짜별로 db의 이름을 다르게 하기 위하여 설정해줌

        Intent intent = new Intent(this, Add_schedule.class);
        intent.putExtra("year", year);//변수들을 모두 넣어줌
        intent.putExtra("month", month);
        intent.putExtra("day",day);
        intent.putExtra("YMD", YMD);
        intent.putExtra("X", X);
        intent.putExtra("Y", Y);
        startActivity(intent);//Add_schedule class 시작
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);


        Bundle extras = getIntent().getExtras();
        if (extras == null)
            Log.e("extras", "null");
        else
            Log.e("extras", "not null");

        X = extras.getDouble("x");//double형 변수를 받아옴
        Y = extras.getDouble("y");

        Log.e("x", X + "");
        Log.e("y", Y + "");
        Toast.makeText(this, X + " + " + Y, Toast.LENGTH_SHORT).show();//재대로 받아 왔나 확인 절차


        CalendarView cal= (CalendarView) findViewById(R.id.calendarView);


        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
               // Toast.makeText(getApplicationContext(), "" + dayOfMonth,0,X,Y).show();
                Add_schedule(year, month, dayOfMonth,X,Y);//Add_schedule함수를 불러와 Add_schedule class를 열어줌
            }
        });

    }
}
