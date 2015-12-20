package com.example.myunghak.everyday_live_logger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by myunghak on 2015-12-15.
 */
public class Add_schedule extends AppCompatActivity {

    SQLiteDatabase db;
    String dbName = "scheduler.db";
    String tableName = "schedulerTable";
    int dbMode = Context.MODE_PRIVATE;

    Button bt_time;
    Button bt_add;
    Button bt_view;
    EditText et_add;
    EditText et_time;
    ListView mList;         //ListView 를 가리키는 변수를 하나 만듬
    TextView t_time;
    TextView date;
    ArrayAdapter<String> baseAdapter;   //배열로 부터 데이터를 가져올때 사용하는 것이 arrayadapter
    ArrayList<String> nameList; //객체들을 삽입, 삭제 검색할 수 있는 컨테이너 class, linkedlist 비슷

    double X;
    double Y;

    void self_refresh() //새로고침
    {
        nameList.clear();
        selectAll();
        baseAdapter.notifyDataSetChanged();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);
/////////////////////////////////////////////////////////////////////////////////////////////////////////

//        String name = this.getIntent().getStringExtra("x");

        Bundle extras = getIntent().getExtras();
        if (extras == null)
            Log.e("extras", "null");
        else
            Log.e("extras", "not null");

        X = extras.getDouble("X");
        Y = extras.getDouble("Y");
        int year =extras.getInt("year");
        int month =extras.getInt("month");
        month++;
        int day =extras.getInt("day");

        Log.e("x", X + "");
        Log.e("y", Y + "");
        Toast.makeText(this, X + " + " + Y, Toast.LENGTH_SHORT).show();

        String YMD = this.getIntent().getStringExtra("YMD");
        dbName += YMD;

//////////////////////////////////////////////////////////////////////////////////////////////////


        // // Database 생성 및 열기
        db = openOrCreateDatabase(dbName, dbMode, null);
        // 테이블 생성
        createTable();
        bt_add = (Button) findViewById(R.id.bt_add);
        et_add = (EditText) findViewById(R.id.et_add);
        bt_time = (Button) findViewById(R.id.bt_time);
        date =(TextView) findViewById(R.id.YMD);
        mList = (ListView) findViewById(R.id.list_view);
        TextView work = (TextView) findViewById(R.id.textView1);

        date.setText(year + "년 "+month+"월 " + day+"일");
        t_time = (TextView) findViewById(R.id.t_time);

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        t_time.setText(hour + "시" + minute + "분~" + hour + "시" + minute + "분");

        work.setText("한일 : ");


        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       //button mBtinsert 를 클릭한경우 아래 코드를 실행하라

                String work = et_add.getText().toString();
                String time = t_time.getText().toString();
                String location = "lat : " + X + " lng : " + Y;
                insertData(work, time, location);
                self_refresh();
            }
        });
        bt_view = (Button) findViewById(R.id.bt_view);

        bt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       //button mBtinsert 를 클릭한경우 아래 코드를 실행하라
                self_refresh();
            }
        });
        mList.setOnItemClickListener(new Click_mList());

        mList.setOnItemLongClickListener(new Long_click_mList());

        //////////////////////////////////////////////////////back button////////////////////////////////////////////////////////////////////////
        Button bt_back = (Button) findViewById(R.id.bt_back);

        bt_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("TEST", "BACK!!!");
                finish();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        nameList = new ArrayList<String>();
        baseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nameList);
        mList.setAdapter(baseAdapter);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    }

    public void onBtnSClicked(View v) {
        Add_schedule_sub newFragment = new Add_schedule_sub();
        newFragment.show(getFragmentManager(), "TimePicker");
    }

    public void createTable() {//크기 설정은 안해도 되나???
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, name text not null)";
            db.execSQL(sql);//sql문을 실행시켜주는 역할
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite", "error: " + e);//디버그 If you want to print out a bunch of messages so you can log the exact flow of your program, use this. If you want to keep a log of variable values, use this.
        }
    }

    // Table 삭제
    public void removeTable() {

        String sql = "drop table " + tableName;
        db.execSQL(sql);
    }

    // Data 추가
    public void insertData(String work, String time, String location) {
        String sql = "insert into " + tableName + " values(NULL, '" + time + "   " + work + location + "');";//NuLL 값을 외 넣는지....
        db.execSQL(sql);
    }

    // Data 업데이트
    public void updateData(int index, String name) {
        String sql = "update " + tableName + " set name = '" + name + "' where id = " + index + ";";
        db.execSQL(sql);
    }


    // Data 삭제
    public void removeData(int index) {


        String sql = "delete from " + tableName + " where id = " + index + ";";

        Cursor results = db.rawQuery(sql, null);
        results.move(index);
        results.close();


        db.execSQL(sql);
    }
    public int index_move(int index){

        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        for(int i=0;i<index;i++)
        {
            results.moveToNext();
        }
        int id = results.getInt(0);


        index = id;

        return index;
    }

    // Data 읽기(꺼내오기)
    public String selectData(int index) {
        String sql = "select * from " + tableName + index + ";";
        Cursor result = db.rawQuery(sql, null);
        String name = "";
        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            int id = result.getInt(0);
            name = result.getString(1);
//            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "\"index= \" + id + \" name=\" + name ");
        }
        result.close();
        return name;
    }

    // 모든 Data 읽기
    public void selectAll() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String name = results.getString(1);
            String num = results.getString(0);
//            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", id + ". " + name);
            String add = name;
            nameList.add(add);
            results.moveToNext();
        }
        results.close();
    }

    public void sort() {
        String sql = "select * from " + tableName + " order by id desc;";
        Cursor results = db.rawQuery(sql, null);    //. Cursor는 DB에서 값을 가져와서 마치 실제 Table의 한 행(Row), 한 행(Row) 을 참조하는 것 처럼 사용 할 수 있게 해주는 것이다.
        results.moveToFirst();  //results cursor 의 제일 첫번째 행으로 이동

        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String name = results.getString(1);
            String num = results.getString(0);
//            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "index= " + id + " name=" + name);//log.d("생명주기", "onCreate")
            String add = name;
            nameList.add(add);     //arryalist형 변수
            results.moveToNext();   //다음 행으로 이동
        }
        results.close();

    }

    void data_toast(int position) {
        Toast.makeText(this, "" + nameList.get(position)+ " "+mList.getDividerHeight(), Toast.LENGTH_LONG).show();
    }

    private class Click_mList extends AppCompatActivity implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            data_toast(position);
        }

    }

    private class Long_click_mList implements AdapterView.OnItemLongClickListener {//
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            final int selectePos = position;
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
            alertDlg.setTitle("질문");

            alertDlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    int index = position;
                    index++;
                    index=index_move(index);
                    index--;


                    removeData(index);
                    nameList.clear();
                    selectAll();
                    baseAdapter.notifyDataSetChanged();

                }


            });

            alertDlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            alertDlg.setMessage(String.format("삭제하시겠습니까?", nameList.get(position)));
            alertDlg.show();
            return false;
        }

    }
}
