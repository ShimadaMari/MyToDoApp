package com.example.shimadamariko.mytodoapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.IOException;

public class secondActivity extends AppCompatActivity {
    private static final int RESULT_PICK_IMAGEFILE = 1000;
    private static final int MENU_ITEM0 = 0;//オプションメニュー
    private static final int MENU_ITEM1 = 1;//オプションメニュー



    private ImageView imageView2;
    private int      pos;//positionのパラメーター
    CalendarAdapter.ViewHolder viewHolder;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        this.setTitle("Select");
        imageView2 = (ImageView)findViewById(R.id.imageView2);
        viewHolder = new CalendarAdapter.ViewHolder();
//        Uri uri = Uri.parse("content://com.android.providers.downloads.documents/document/raw%3A%2Fstorage%2Femulated%2F0%2FDownload%2FIMG_20180804_115205.jpg");
//
//        try {
//            Bitmap bmp = getBitmapFromUri(uri);
//            imageView.setImageBitmap(bmp);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        pos = -1;// positionなし
//        toDoItem = new ToDoItem();  //ToDoItem生成
//        Bundle extras = getIntent().getExtras();
//        //インテントから拡張データのマップを取得します
//        if (extras != null) {
//            pos = extras.getInt("pos");//posionをget
//            toDoItem.title = extras.getString("title");   //titleをget
//            toDoItem.checked = extras.getBoolean("checked");//checkedをget・



            findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
              Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
               intent.addCategory(Intent.CATEGORY_OPENABLE);
               intent.setType("image/*");


                startActivityForResult(intent, RESULT_PICK_IMAGEFILE);
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.d("imgUri",uri.toString());

                try {
                    Bitmap bmp = getBitmapFromUri(uri);
                    imageView2.setImageBitmap(bmp);
//                    ImageView imageView= null;
//                    CalendarAdapter.ViewHolder.imageView = findViewById(R.id.imageView);
//                    imageView.setImageBitmap(bmp);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//
//        //削除アイテムの追加
//        if (pos >= 0) {
//            MenuItem item0 = menu.add(0, MENU_ITEM0, 0, "削除");
//            item0.setIcon(android.R.drawable.ic_menu_delete);
//            item0.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        }
//
//        //保存アイテムの追加
//        MenuItem item1 = menu.add(0, MENU_ITEM1, 0, "保存");
//        item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        return true;
//    }
//    //メニューアイテム選択イベントの処理
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemId = item.getItemId();
//        if (itemId == MENU_ITEM0 || itemId == MENU_ITEM1) {
//            String result = null;
//            if (itemId == MENU_ITEM0) result = "delete";
//            if (itemId == MENU_ITEM1) result = pos<0?"add":"edit";
//
//            //パラメータの返信(4)
//            Intent intent = new Intent();
//            intent.putExtra("result", result);
//            intent.putExtra("pos", pos);
//            setResult(Activity.RESULT_OK, intent);
//            finish();
//        }
//        return true;
//    }



}


