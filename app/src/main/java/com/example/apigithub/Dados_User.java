package com.example.apigithub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dados_User extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_user);

        //recebe os dados da outra tela
        Intent NickName = getIntent();
        String UserNick = NickName.getStringExtra("UserNick");

        Toast.makeText(getApplicationContext(), "User: "+UserNick, Toast.LENGTH_LONG).show();
    }

}