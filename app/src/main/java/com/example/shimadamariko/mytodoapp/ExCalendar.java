package com.example.shimadamariko.mytodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class ExCalendar extends AppCompatActivity {

    private TextView titleText;
    private Button prevButton, nextButton;
    private CalendarAdapter mCalendarAdapter;
    private GridView calendarGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("カレンダー");
        setContentView(R.layout.main);

        titleText = findViewById(R.id.titleText);
        prevButton = findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarAdapter.prevMonth();
                titleText.setText(mCalendarAdapter.getTitle());
            }
        });
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarAdapter.nextMonth();
                titleText.setText(mCalendarAdapter.getTitle());
            }
        });
        calendarGridView = findViewById(R.id.calendarGridView);
        mCalendarAdapter = new CalendarAdapter(this);
        calendarGridView.setAdapter(mCalendarAdapter);
        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String message = position+"日が選択されました";
               // String message = (String)parent.getItemAtPosition(position)+"が選択されました。";
                Toast.makeText(ExCalendar.this, message, Toast.LENGTH_LONG).show();
                startEditActivity(null);
            }
        });
        titleText.setText(mCalendarAdapter.getTitle());
    }
    private void startEditActivity(ToDoItem item) {
        Intent intent = new Intent(this, secondActivity.class);
        if (item == null) {//引き数がnullの時
            intent.putExtra("pos", -1);  //positionが-1（なし）を格納
            intent.putExtra("title", "");//title  ""  格納
            //   intent.putExtra("絶対に編集画面にするかどうか","はい");
            //   intent.putExtra("枠","追加");
            intent.putExtra("checked", false);//チェックなしを格納
        } else {//引数がnull以外の場合
            intent.putExtra("pos",0);//itemsのpositionのitemを格納

            intent.putExtra("title", item.title); //titelを格納
            intent.putExtra("checked", item.checked);//チェックを格納
        }
        startActivityForResult(intent, 0);//(Intent intent, int requestCode)
        //推移先にintentとREQUEST_EDITを渡す
    }

}
