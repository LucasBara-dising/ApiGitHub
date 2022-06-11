package com.example.apigithub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splach extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               Home();
            }
        },3000);//3seg
    }

    public  void Home(){
        Intent home = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(home);
    }
}