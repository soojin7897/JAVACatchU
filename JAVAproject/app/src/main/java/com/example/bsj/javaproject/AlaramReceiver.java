package com.example.bsj.javaproject;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;


public class AlaramReceiver extends BroadcastReceiver {
    double lat;
    double lng;
    Double latitude;
    Double longitude;
    Context tc;
    @Override
    public void onReceive(final Context context, Intent intent) {
        tc = context;
        Toast.makeText(context, "보호자에게 메시지가 전송되었습니다.", Toast.LENGTH_SHORT).show();
        Log.e("HelloAlarmActivity", "LOG리시버");

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("+821044136261", null, "귀가시간이 늦어지고 있습니다. 위치를 확인해 주세요" , null,null);
        startLocationService();
    }
    private void startLocationService() {
        // 위치 관리자 객체 참조
        LocationManager manager = (LocationManager) tc.getSystemService(Context.LOCATION_SERVICE);

        // 위치 정보를 받을 리스너 생성
        GPSListener gpsListener = new GPSListener();
        long minTime = 60000;
        float minDistance = 0;

        try {
            // GPS를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 네트워크를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 위치 확인이 안되는 경우에도 최근에 확인된 위치 정보 먼저 확인
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                latitude = lastLocation.getLatitude();
                longitude = lastLocation.getLongitude();

                String msg = "http://maps.google.com/?q="+latitude+","+longitude;
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("+821044136261", null, msg , null,null);
                Toast.makeText(tc.getApplicationContext(), "Last Known Location : " + "Latitude : " + latitude + "\nLongitude:" + longitude, Toast.LENGTH_LONG).show();
            }
        } catch(SecurityException ex) {
            ex.printStackTrace();
        }

        Toast.makeText(tc.getApplicationContext(), "위치 확인이 시작되었습니다.", Toast.LENGTH_SHORT).show();

    }

    /**
     * 리스너 클래스 정의
     */
    private class GPSListener implements LocationListener {
        /**
         * 위치 정보가 확인될 때 자동 호출되는 메소드
         */
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            String msg = "http://maps.google.com/?q="+latitude+","+longitude;
            Log.i("GPSListener", msg);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+821044136261", null, msg , null,null);

            Toast.makeText(tc.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }

}
