package com.example.shimadamariko.mytodoapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateManager {
    Calendar mCalendar;//カレンダープロジェクト

    public DateManager(){
        mCalendar = Calendar.getInstance();//現在の時刻に基づいてカレンダーを取得
    }

    //当月の要素を取得
    public List<Date> getDays(){
        //現在の状態を保持
        Date startDate = mCalendar.getTime();

        //GridViewに表示するマスの合計を計算
        int count = getWeeks() * 7 ;

        //当月のカレンダーに表示される前月分の日数を計算
        mCalendar.set(Calendar.DAY_OF_MONTH,1);//1日を取得
        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK)-1;//日付から曜日を取得
        mCalendar.add(Calendar.DAY_OF_MONTH, -dayOfWeek);//日付から曜日適用分を引く

        List<Date> days = new ArrayList<>();//List生成

        for (int i = 0; i < count; i ++){
            days.add(mCalendar.getTime());
            mCalendar.add(Calendar.DATE, 1);
        }

        //状態を復元
        mCalendar.setTime(startDate);

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
    public int getWeeks(){    //最大値を返す
        return mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
    }

    //曜日を取得
    public int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    //翌月へ
    public void nextMonth(){
        mCalendar.add(Calendar.MONTH, 1);
    }

    //前月へ
    public void prevMonth(){
        mCalendar.add(Calendar.MONTH, -1);
    }
}