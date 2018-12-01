package com.example.shimadamariko.mytodoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ExCalendar extends AppCompatActivity {

    private TextView titleText;
    private Button prevButton, nextButton;
    private CalendarAdapter mCalendarAdapter;
    private GridView calendarGridView;

    private ArrayList<CalendarAdapter.ViewHolder> items;  //12/1 ViewHolderを要素に

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
//                Uri uri = null;
//                Log.d("imgUri",uri.toString());
               String message = position+"日が選択されました";
               // String message = (String)parent.getItemAtPosition(position)+"が選択されました。";
                Toast.makeText(ExCalendar.this, message, Toast.LENGTH_LONG).show();
                startEditActivity(null);
            }
        });
        titleText.setText(mCalendarAdapter.getTitle());
    }
    private void startEditActivity(ToDoApp item) {//(ToDoItem item)
        Intent intent = new Intent(this, secondActivity.class);
        if (item == null) {//引き数がnullの時
            intent.putExtra("pos", 0);  //positionが-1（なし）を格納
           // intent.putExtra("title", "");//title  ""  格納
            //   intent.putExtra("絶対に編集画面にするかどうか","はい");
            //   intent.putExtra("枠","追加");
           // intent.putExtra("checked", false);//チェックなしを格納
        } else {//引数がnull以外の場合
            intent.putExtra("pos",0);//itemsのpositionのitemを格納
            //intent.putExtra("title", item.title); //titelを格納
            //intent.putExtra("checked", item.checked);//チェックを格納
        }
        startActivityForResult(intent, 100);//(Intent intent, int requestCode)
        //推移先にintentとREQUEST_EDITを渡す
    }
//    @Override
//    protected void onActivityResult(int requestCode,
//                                    int resultCode, Intent intent) {
//        if (requestCode == 100 &&//requestCodeがREQUEST_EDITと＝でかつ
//                resultCode == RESULT_OK) {//結果がOKだった場合
//            Bundle extras = intent.getExtras();//遷移先から格納されたintent情報を取得してextrasに代入
//            if (extras != null) {//extrasがnullじゃなかったら
//                String result = extras.getString("result");//"result"の情報を取得してresultに代入("add":追加、"edit":編集、"delete":削除）
//                int pos = extras.getInt("pos");//"pos"の情報を取得してposに代入
//                String title = extras.getString("title");//"title"の情報を取得してtitelに代入
//                boolean checked = extras.getBoolean("checked");//チェックされているかをcheckedに代入(false,ture)
//
//                //追加
//                if (result.equals("add")) {//追加ボタンだった場合
//                    ToDoItem item = new ToDoItem();//ToDoItemを作成
//                    item.title = title; //titleを生成
//                    item.checked = checked;//checkdを生成
//                    items.add(item);//itemを追加
//                }
//                //編集
//                else if (result.equals("edit")) {//編集ボタンだった場合
//                    ToDoItem item = items.get(pos);//itemsの// positionをゲット
//                    item.title = title;//title生成
//                    item.checked = checked;//checkedを生成
//                }
//                //削除
//                else if (result.equals("delete")) {//削除ボタンだった場合
//                    items.remove(pos);//item削除
//                }
//
//                //リストビューの更新(1)                 BaceAdapterクラスの保持するデータをビューに反映
//                ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();//
//            }
//        }
//    }


}
