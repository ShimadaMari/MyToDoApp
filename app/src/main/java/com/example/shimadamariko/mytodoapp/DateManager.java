package com.example.shimadamariko.mytodoapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateManager {
    Calendar mCalendar;//カレンダープロジェクト

    public DateManager(){   //TineZone  Locale 引数
        mCalendar = Calendar.getInstance();//現在の時刻に基づいてカレンダーを取得  初期化
    }

    //当月の要素を取得
    public List<Date> getDays(){
        //現在の状態を保持 取得
        Date startDate = mCalendar.getTime();

        //GridViewに表示するマスの合計を計算
        int count = getWeeks() * 7 ;

        //当月のカレンダーに表示される前月分の日数を計算
        mCalendar.set(Calendar.DAY_OF_MONTH,1);//1日set    DAY_OF_WEEK:週の何番目かGET
        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK)-1;//日付から曜日を取得
        mCalendar.add(Calendar.DAY_OF_MONTH, -dayOfWeek);//前月分を引く

        List<Date> days = new ArrayList<>();//List生成

        for (int i = 0; i < count; i ++){
            days.add(mCalendar.getTime());//日の時間を取得
            mCalendar.add(Calendar.DATE, 1);//日にちを一日呼び出す
        }

        //状態を復元
        mCalendar.setTime(startDate);//最初の日をセット

        return days;
    }

    //当月かどうか確認    Current:現在
    public boolean isCurrentMonth(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM", Locale.US);
        String currentMonth = format.format(mCalendar.getTime());
        if (currentMonth.equals(format.format(date))){
            return true;
        }else {
            return false;
        }
    }

    //週数を取得
    public int getWeeks(){    //getActualMaximum:指定したフィールドの最大値を返す
        return mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);//月の何周目か
    }

    //曜日を取得
    public int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);//週の何日目か
    }

    //翌月へ
    public void nextMonth(){
        mCalendar.add(Calendar.MONTH, 1);//一月足す
    }

    //前月へ
    public void prevMonth(){
        mCalendar.add(Calendar.MONTH, -1);//一月引く
    }
}