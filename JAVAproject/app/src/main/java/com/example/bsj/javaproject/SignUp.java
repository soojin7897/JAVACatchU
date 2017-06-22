package com.example.bsj.javaproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
    }
    public void remit(View v) {
        Intent intent = new Intent(this, Welcome.class);
        startActivity(intent);

    }

    public void find(View v) {
        Intent intent = new Intent(this, Find.class);
        startActivity(intent);

    }

}

