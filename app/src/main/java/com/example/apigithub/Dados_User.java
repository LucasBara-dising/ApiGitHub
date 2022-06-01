package com.example.apigithub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dados_User extends AppCompatActivity {

    private TextView nameUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_user);
        nameUser = findViewById(R.id.nameUser);

        //Toast.makeText(getApplicationContext(), "User: "+UserNick, Toast.LENGTH_LONG).show();

        consultarUser();
    }

    private void consultarUser() {
        //pega nickname
        Intent NickName = getIntent();
        String UserNick = NickName.getStringExtra("UserNick");

        String URL = "https://api.github.com/users/";
        String linkNoMostradados= URL +UserNick;


        //configura os recursos do retrofit
        Retrofit retrofitGit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();

        //instanciando a interface
        RESTService restService = retrofitGit.create(RESTService.class);

        //passando os dados para consulta
        Call<UserGit> call= restService.consultaUser(UserNick);
        Log.i("Link da Consulta No mostra Dados", linkNoMostradados);


        //colocando a requisição na fila para execução
        call.enqueue(new Callback<UserGit>() {
            @Override
            public void onResponse(Call<UserGit> call, Response<UserGit> response) {
                Log.i("Teste", "Chegou aqui");
                if (response.isSuccessful()) {
                    UserGit  userGit= response.body();

                    String name=userGit.getName();
                    nameUser.setText(userGit.getName());

                    //Toast.makeText(getApplicationContext(), "User: "+userGit.getName(), Toast.LENGTH_LONG).show();
                    Log.i("Name User", userGit.getName());
                }
            }

            @Override
            public void onFailure(Call<UserGit> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro ao tentar consultar o Perfil. Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}