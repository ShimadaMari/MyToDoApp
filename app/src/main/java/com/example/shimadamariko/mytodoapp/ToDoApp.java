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
import android.widget.Adapter;
import android.widget.AdapterView;
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
        this.setTitle("ToDoアプリ");//タイトルをセットBundle

        //要素群の読み込み
        items = new ArrayList<ToDoItem>();
        loadItems();//

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
            intent.putExtra("pos", items.indexOf(item));//itemsのpositionのitemを格納

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
                String result = extras.getString("result");//"result"の情報を取得してresultに代入("add":追加、"edit":編集、"delete":削除）
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
                    ToDoItem item = items.get(pos);//itemsの// positionをゲット
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
        }//Listの数を返すメソッド

        //要素の取得
        @Override
        public ToDoItem getItem(int pos) {
            return items.get(pos);
        }//itemのポジション返すメソッド

        //要素IDの取得
        @Override
        public long getItemId(int pos) { return pos;}//

        //セルのビューの生成(2)
        @Override
        public View getView(int pos, View view, ViewGroup parent) {
            ToDoItem item = items.get(pos);//itemsのポジションをitemに代入

            //レイアウトの生成
            if (view == null) {//viewがない場合はレイアウトを生成
                //レイアウトの生成
                LinearLayout layout = new LinearLayout(ToDoApp.this);
                layout.setBackgroundColor(Color.WHITE);
                layout.setPadding(//padding：要素の内側の余白をセット
                        Util.dp2px(ToDoApp.this, 10),
                        Util.dp2px(ToDoApp.this, 10),
                        Util.dp2px(ToDoApp.this, 10),
                        Util.dp2px(ToDoApp.this, 10));
                layout.setOnClickListener(new View.OnClickListener() {//layoutにリスナを設定
                    @Override
                    public void onClick(View sender) {//タップされた時の処理
                        //編集アクティビティの起動
                        int pos = Integer.parseInt((String)sender.getTag());//Viewに格納されたオプジェクトをTagにして返す
                        ToDoItem item = items.get(pos);//itemにpositonの値を代入
                        startEditActivity(item);//item値に基づいてEditActivityを開く

                    }
                });

                //チェックボックスの追加
                CheckBox checkBox = new CheckBox(ToDoApp.this);//チェックボックスノ生成
                checkBox.setTextColor(Color.BLACK);//色の指定
                checkBox.setId(R.id.cell_checkbox);//idの設定
                //checkBox.setChecked(false);//チェックボックスにチェックを設定
                checkBox.setLayoutParams(new LinearLayout.LayoutParams(WC, WC));//表示を調整
                checkBox.setOnClickListener(new View.OnClickListener() {//チェックボックスにリスナを設定
                    @Override
                    public void onClick(View sender) {//タップされた時の処理
                       // ToDo情報の更新
                        int pos = Integer.parseInt((String)sender.getTag());//ViewのオプジェクトをTagにしてposに代入
                       ToDoItem item = items.get(pos);//itemにpositionの値を代入
                        item.checked = ((CheckBox)sender).isChecked();//View型のsenderをチェックボックス型にキャストしチェックされているかどうかを調べる
                   }
                });
                layout.addView(checkBox);//レイアウトにチェックボックスを追加
                view = layout;//viewにlayoutを代入
            }

            //既存のチェックボックへの値の指定
            CheckBox checkBox = (CheckBox)view.findViewById(R.id.cell_checkbox);//チェックボックにViewとidを設定
            checkBox.setChecked(item.checked);//item,checkの状態を更新するcheckedを設定
            checkBox.setText(item.title);//itemのタイトルを設定
            checkBox.setTag(""+pos);//ポジションとcheckBoxをタグ付 Stringに変えている
            view.setTag(""+pos);//ボジションとViewをタグ付
            return view;//getVieｗの内容を返す
        }
    }

    //要素群の書き込み  save
    private void saveItems() {
        //ArrayListをJSONに変換
        String json = list2json(items);//Stringのjsonに要素群の読み込み

        //プリファレンスへの書き込み
        SharedPreferences pref = getSharedPreferences(
                "ToDoApp", MODE_PRIVATE);//デフォルトモード。
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("items", json);
        editor.commit();//保存
    }

    //要素群の読み込み  読み込んでJSONをArraylistに変換
    private void loadItems() {
        //プリファレンスからの読み込み
        SharedPreferences pref = getSharedPreferences(
                "ToDoApp", MODE_PRIVATE);//デフォルトモード。作成されたファイルは呼び出し元のアプリケーション
        String json = pref.getString("items","");

        //JSONをArrayListに変換
        items = items2list(json);
    }

    //ArrayListをJSONに変換(5)     引数ArrayListのtimes
    private String list2json(ArrayList<ToDoItem> items) {
        try {
            JSONArray array = new JSONArray();//JSON配列オプジェクトをarrayに代入し生成
            for (ToDoItem item : items) {//拡張for文でitemsをまわす（ToDoTiem i = 0 ; i < items.length ; i++)
                JSONObject obj = new JSONObject();//JSONオプジェクトをobjに代入し生成
                                           //ouject
                obj.put("title", item.title);//JSONオプジェクトにitemのtitleを入れる
                                           //ouject
                obj.put("checked", item.checked);//JSONオプジェクトにitemのcheckedを入れる
                array.put(obj);//JSON配列にJSONオプジェクトを入れる

            }
            return array.toString();//JSON配列ををStringで実行
        } catch (JSONException e) {
            e.printStackTrace();//errだった場合はスタックトレースを出力する
        }
        return "";//String型のJSON配列を返す
    }

    //JSONをArrayListに変換(6)              引数Stringのjson
    private ArrayList<ToDoItem> items2list(String json) {
        ArrayList<ToDoItem> items = new ArrayList<ToDoItem>();//ArrayListを生成
        try {
            JSONArray array = new JSONArray(json);//JSON配列をarrayに代入し生成
            for (int i = 0; i < array.length(); i++) {//配列を回す
                JSONObject obj = array.getJSONObject(i);//JSONopujekuをobjに代入し生成
                ToDoItem item = new ToDoItem();//ToDoItemをitemに代入し生成
                item.title = obj.getString("title");//itemのtitleにオプジェクトをget
                item.checked = obj.getBoolean("checked");//itemのcheckedをget
                items.add(item);//getしたitemをlistに追加
            }
        } catch (JSONException e) {
            e.printStackTrace();//errだった場合はスタックトレースを出力する
        }
        return items;//＜TodoItem＞のitemsを返す
    }
}