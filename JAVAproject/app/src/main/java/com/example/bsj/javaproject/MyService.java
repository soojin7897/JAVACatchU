package com.example.bsj.javaproject;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MyService extends Service {
    TextView txtView;

    MainIntrusionPrevention.ListItem Item;
    ArrayList<MainIntrusionPrevention.ListItem> listItem;
    MainIntrusionPrevention.phpDown task;
    boolean go;
    int sum;
    public void onCreate() {
        super.onCreate();
        boolean go = false;
        go = false;
        sum = 0;
        Log.d("test", "서비스의 onCreate");

    }

    public MyService() {
        boolean go = false;
        go = false;
        sum = 0;
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        func();
        Log.d("test", "서비스의 onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    public void func() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    phpDown task = new phpDown();
                    try {
                        sum+=1;
                        task.execute("http://210.117.182.120/getdata3.php");
                        Log.e("Activity", "sum: " + sum);
                        Thread.sleep(5000);
                        //task = null;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

public class phpDown extends AsyncTask<String, Integer,String> {

    @Override
    public String doInBackground(String... urls) {
        if(go == true) return "";
        go = true;
        StringBuilder jsonHtml = new StringBuilder();
        try{
            // 연결 url 설정
            URL url = new URL(urls[0]);
            // 커넥션 객체 생성
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            // 연결되었으면.
            if(conn != null){
                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);
                // 연결되었음 코드가 리턴되면.
                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    for(;;){
                        // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                        String line = br.readLine();
                        if(line == null) break;
                        // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                        jsonHtml.append(line + "\n");
                    }
                    br.close();
                }
                conn.disconnect();
            }
        } catch(Exception ex){
            ex.printStackTrace();
        }
        return jsonHtml.toString();

    }



    public void onPostExecute(String str) {
        String imgurl;
        listItem = new ArrayList<MainIntrusionPrevention.ListItem>();
        int len = 0;
        try {
            JSONObject root = new JSONObject(str);
            JSONArray ja = root.getJSONArray("result");
            len = ja.length();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                imgurl = jo.getString("diff");
                listItem.add(new MainIntrusionPrevention.ListItem(imgurl));
            }
            if(!(listItem.get(len - 1).getData(0)).equals("0")){
                Toast.makeText(MyService.this, "모션 인식이 확인되었습니다. 카메라를 확인해주세요.", Toast.LENGTH_LONG).show();


            }

            //txtView.setText("face :" + listItem.get(len-1).getData(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //txtView.setText("face :" + listItem.get(i-1).getData(0));
        go = false;
    }
    @Override
    protected void onCancelled() {
        go=false;
        super.onCancelled();
    }
}

class ListItem {
    public String[] mData;
    public ListItem(String[] data ){
        mData = data;
    }

    public ListItem(String imgurl){
        mData = new String[3];
        mData[0] = imgurl;
    }

    public String[] getData(){
        return mData;
    }

    public String getData(int index){
        return mData[index];
    }

    public void setData(String[] data){
        mData = data;
    }
}
}

