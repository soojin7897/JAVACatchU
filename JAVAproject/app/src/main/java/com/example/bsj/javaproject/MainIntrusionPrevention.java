package com.example.bsj.javaproject;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.RunnableFuture;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class MainIntrusionPrevention extends AppCompatActivity {
    TextView txtView;
    ListItem Item;
    ArrayList<ListItem> listItem;
    phpDown task;
    boolean go;
    int sum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        go = false;
        sum = 0;
        setContentView(R.layout.ip);
        WebView WebView01 = (WebView) findViewById(R.id.WebView01);
        WebView01.setWebViewClient(new WebViewClient());
        WebSettings webSettings = WebView01.getSettings();
        txtView = (TextView)findViewById(R.id.txtView);
        WebView01.loadUrl("http://210.117.182.120:8080/stream_simple.html");
    }
    public class a implements Runnable{
        public void add(int m) {
            while (true) {
                m += 1;
            }

        }
        @Override
        public void run() {
            add(10);
        }
    }
    public void func(View v) {
        Log.d("test", "액티비티-서비스 시작버튼클릭");
        Intent intent = new Intent(
                getApplicationContext(),//현재제어권자
                MyService.class); // 이동할 컴포넌트
        startService(intent); // 서비스 시작
    }




        /*task = new phpDown();
        task.execute("http://210.117.182.120/getdata2.php");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/



    public void report (View v){
        // Intent intent = new Intent(this, MainIntrusionPrevention.class);
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("tel:1123"));
        startActivity(intent);
    }

    public void ButtonClick2(View a){

        finish();
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
            listItem = new ArrayList<ListItem>();
            int len = 0;
            try {
                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("result");
                len = ja.length();
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    imgurl = jo.getString("diff");
                    listItem.add(new ListItem(imgurl));
                }
                txtView.setText("diff :" + listItem.get(len-1).getData(0));
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

    static class ListItem {
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

