package com.example.bsj.javaproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import java.util.Calendar;
import java.util.GregorianCalendar;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker.OnTimeChangedListener;

public class safehome extends Activity  implements OnDateChangedListener, OnTimeChangedListener{

        // 알람 메니저
        public AlarmManager mManager;
        // 설정 일시
        public GregorianCalendar mCalendar;
        //일자 설정 클래스
        public DatePicker mDate;
        //시작 설정 클래스
        public TimePicker mTime;

        private NotificationManager mNotification;
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        //통지 매니저를 취득
            mNotification = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //알람 매니저를 취득
            mManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //현재 시각을 취득
            mCalendar = new GregorianCalendar();
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {


                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            0);

                }
            }
        Log.i("HelloAlarmActivity",mCalendar.getTime().toString());
        //셋 버튼, 리셋버튼의 리스너를 등록
        setContentView(R.layout.safe);
        Button b = (Button)findViewById(R.id.set);
        b.setOnClickListener (new View.OnClickListener(){
        public void onClick (View v){
            Toast.makeText(getApplicationContext(), "알람 시간이 설정되었습니다.", Toast.LENGTH_SHORT).show();
                setAlarm();
            }
        });

       b = (Button)findViewById(R.id.reset);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "알람 시간이 재설정되었습니다.", Toast.LENGTH_SHORT).show();
                resetAlarm();
            }
        });

        //일시 설정 클래스로 현재 시각을 설정
            mDate =(DatePicker)findViewById(R.id.date_picker);
            mDate.init(mCalendar.get(Calendar.YEAR),mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH),this);
            mTime=(TimePicker)findViewById(R.id.time_picker);
            mTime.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
            mTime.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
            mTime.setOnTimeChangedListener(this);
        }
//알람의 설정
        private void setAlarm(){
            Intent intent = new Intent(getApplicationContext(),AlaramReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(this,0,intent,0);
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP,mCalendar.getTimeInMillis(),0,sender);
        mManager.set(AlarmManager.RTC_WAKEUP,mCalendar.getTimeInMillis(),pendingIntent());
       Log.i("HelloAlarmActivity",mCalendar.getTime().toString());
        }
        //알람의 해제
        private void resetAlarm(){
        mManager.cancel(pendingIntent());
        }
       //알람의 설정 시각에 발생하는 인텐트 작성
        private PendingIntent pendingIntent() {
        Intent i = new Intent(getApplicationContext(), AlaramReceiver.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,i,0);
            return pi;
        }
        //일자 설정 클래스의 상태변화 리스너
       public void onDateChanged (DatePicker view,int year,int monthOfYear, int dayOfMonth){
        mCalendar.set (year, monthOfYear, dayOfMonth, mTime.getCurrentHour(), mTime.getCurrentMinute());
        Log.i("HelloAlarmActivity", mCalendar.getTime().toString());
       }
         //시각 설정 클래스의 상태변화 리스너
         public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        mCalendar.set (mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(), hourOfDay, minute);
             Log.i("HelloAlarmActivity",mCalendar.getTime().toString());
        }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }

        }
}
}
