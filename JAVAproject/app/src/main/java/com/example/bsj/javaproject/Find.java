package com.example.bsj.javaproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class Find extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fire);
    }
    public void commit(View v) {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);

    }

}

