package com.example.shimadamariko.mytodoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//public class ToDoApp {
//}
//ToDoアプリ
public class ToDoApp extends AppCompatActivity {
    //定数
    private final static int WC = ViewGroup.LayoutParams.WRAP_CONTENT;  //部品表示を調整する
    private final static int REQUEST_EDIT = 0;//遷移先のアクティビティを識別するためのID
    private final static int MENU_ITEM0 = 0;//オプションメニューitemのItemID

    //UI
    private ListView listView;//リストビューを定義
    private ArrayList<ToDoItem> items;//要素群を定義

    //アクティビティ起動時に呼ばれる
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setTitle("ToDoアプリ");//タイトルをセット

        //要素群の読み込み
        items = new ArrayList<ToDoItem>();
        loadItems();

        //リストビューの生成(1)
        listView = new ListView(this);//ListViewを呼び出し
        listView.setScrollingCacheEnabled(false);//Scrolのキャッシュを調整するメソッドをset
        listView.setAdapter(new MyAdapter());//自作アダプタをset
        setContentView(listView);// ContentViewをsetしViewできるようにする
    }

    //アクティビティ停止時に呼ばれる
    @Override
    public void onStop() {
        super.onStop();

        //要素群の書き込み
        saveItems();//アクティビティが停止した時に要素群に書き込みを保存する
    }

    //オプションメニューの生成
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //追加アイテムの追加  add：メニューに新しい項目を追加する
                                              //Itemid
        MenuItem item0 = menu.add(0, MENU_ITEM0, 0, "追加");//オプションメニュー変数を宣言しIDとタイトルを設定
        item0.setIcon(android.R.drawable.ic_menu_add);//宣言した変数item0に（＋）をセット
        item0.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);//宣言した変数item0をアクションバーに常に表示
        return true;
    }

    //メニューアイテム選択イベントの処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();// どのオプションメニューか確認するためにItemIDをgetする
        if (itemId == MENU_ITEM0) {//getしたIDがMENU_ITEM0だったら
            startEditActivity(null);//ToDoItemの引数nullで編集アクティビティを起動
        }
        return true;
    }

    //編集アクティビティの起動(3)
    private void startEditActivity(ToDoItem item) {
        Intent intent = new Intent(this, EditActivity.class);
        if (item == null) {//引き数がnullの時
            intent.putExtra("pos", -1);  //positionが-1（なし）を格納
            intent.putExtra("title", "");//title  ""  格納
         //   intent.putExtra("絶対に編集画面にするかどうか","はい");
         //   intent.putExtra("枠","追加");
            intent.putExtra("checked", false);//チェックなしを格納
        } else {//引数がnull以外の場合
            intent.putExtra("pos", items.indexOf(item));//positionのindexを格納

            intent.putExtra("title", item.title); //titelを格納
            intent.putExtra("checked", item.checked);//チェックを格納
        }
        startActivityForResult(intent, REQUEST_EDIT);//(Intent intent, int requestCode)
        //推移先にintentとREQUEST_EDITを渡す
    }

    //編集アクティビティ呼び出し結果の取得(4)   EditActivityでfinishまたはキャンセルで戻って生きた時に実行
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent intent) {
        if (requestCode == REQUEST_EDIT &&//requestCodeがREQUEST_EDITと＝でかつ
                resultCode == RESULT_OK) {//結果がOKだった場合
            Bundle extras = intent.getExtras();//遷移先から格納されたintent情報を取得してextrasに代入
            if (extras != null) {//extrasがnullじゃなかったら
                String result = extras.getString("result");//"result"の情報を取得してresultに代入(add:追加、edit:編集、delete:削除）
                int pos = extras.getInt("pos");//"pos"の情報を取得してposに代入
                String title = extras.getString("title");//"title"の情報を取得してtitelに代入
                boolean checked = extras.getBoolean("checked");//チェックされているかをcheckedに代入(false,ture)

                //追加
                if (result.equals("add")) {//追加ボタンだった場合
                    ToDoItem item = new ToDoItem();//ToDoItemを作成
                    item.title = title; //titleを生成
                    item.checked = checked;//checkdを生成
                    items.add(item);//itemを追加
                }
                //編集
                else if (result.equals("edit")) {//編集ボタンだった場合
                    ToDoItem item = items.get(pos);//positionをゲット
                    item.title = title;//title生成
                    item.checked = checked;//checkedを生成
                }
                //削除
                else if (result.equals("delete")) {//削除ボタンだった場合
                    items.remove(pos);//item削除
                }

                //リストビューの更新(1)                 BaceAdapterクラスの保持するデータをビューに反映
                ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();//
            }
        }
    }

    //自作アダプタ
    private class MyAdapter extends BaseAdapter {
        //要素数の取得
        @Override
        public int getCount() {
            return items.size();
        }

        //要素の取得
        @Override
        public ToDoItem getItem(int pos) {
            return items.get(pos);
        }

        //要素IDの取得
        @Override
        public long getItemId(int pos) {
            return pos;
        }

        //セルのビューの生成(2)
        @Override
        public View getView(int pos, View view, ViewGroup parent) {
            ToDoItem item = items.get(pos);

            //レイアウトの生成
            if (view == null) {
                //レイアウトの生成
                LinearLayout layout = new LinearLayout(ToDoApp.this);
                layout.setBackgroundColor(Color.WHITE);
                layout.setPadding(
                        Util.dp2px(ToDoApp.this, 10),
                        Util.dp2px(ToDoApp.this, 10),
                        Util.dp2px(ToDoApp.this, 10),
                        Util.dp2px(ToDoApp.this, 10));
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View sender) {
                        //編集アクティビティの起動
                        int pos = Integer.parseInt((String)sender.getTag());
                        ToDoItem item = items.get(pos);
                        startEditActivity(item);
                    }
                });

                //チェックボックスの追加
                CheckBox checkBox = new CheckBox(ToDoApp.this);
                checkBox.setTextColor(Color.BLACK);
                checkBox.setId(R.id.cell_checkbox);
                checkBox.setChecked(true);
                checkBox.setLayoutParams(new LinearLayout.LayoutParams(WC, WC));
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View sender) {
                        //ToDo情報の更新
                        int pos = Integer.parseInt((String)sender.getTag());
                        ToDoItem item = items.get(pos);
                        item.checked = ((CheckBox)sender).isChecked();
                    }
                });
                layout.addView(checkBox);
                view = layout;
            }

            //値の指定
            CheckBox checkBox = (CheckBox)view.findViewById(R.id.cell_checkbox);
            checkBox.setChecked(item.checked);
            checkBox.setText(item.title);
            checkBox.setTag(""+pos);
            view.setTag(""+pos);
            return view;
        }
    }

    //要素群の書き込み
    private void saveItems() {
        //ArrayListをJSONに変換
        String json = list2json(items);

        //プリファレンスへの書き込み
        SharedPreferences pref = getSharedPreferences(
                "ToDoApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("items", json);
        editor.commit();
    }

    //要素群の読み込み
    private void loadItems() {
        //プリファレンスからの読み込み
        SharedPreferences pref = getSharedPreferences(
                "ToDoApp", MODE_PRIVATE);
        String json = pref.getString("items","");

        //JSONをArrayListに変換
        items = items2list(json);
    }

    //ArrayListをJSONに変換(5)
    private String list2json(ArrayList<ToDoItem> items) {
        try {
            JSONArray array = new JSONArray();
            for (ToDoItem item : items) {
                JSONObject obj = new JSONObject();
                obj.put("title", item.title);
                obj.put("checked", item.checked);
                array.put(obj);
            }
            return array.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    //JSONをArrayListに変換(6)
    private ArrayList<ToDoItem> items2list(String json) {
        ArrayList<ToDoItem> items = new ArrayList<ToDoItem>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                ToDoItem item = new ToDoItem();
                item.title = obj.getString("title");
                item.checked = obj.getBoolean("checked");
                items.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }
}