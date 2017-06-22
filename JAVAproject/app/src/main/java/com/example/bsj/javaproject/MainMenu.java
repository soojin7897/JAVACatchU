package com.example.bsj.javaproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
    }
        public void ButtonClick2(View v){

            Intent intent = new Intent(this, MainIntrusionPrevention.class);
            startActivity(intent);

        }
        public void ButtonClick3(View v){

        Intent intent = new Intent(this, safehome.class);
        startActivity(intent);

         }
         public void ButtonClick4(View v){

        Intent intent = new Intent(this, Fire.class);
        startActivity(intent);

         }
         public void OnClick1(View v) {
             finish();
          }

}

