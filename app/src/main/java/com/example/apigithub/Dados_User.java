package com.example.apigithub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dados_User extends AppCompatActivity {

    private TextView nameUser;
    private String UserNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_user);
        nameUser = findViewById(R.id.txtviewName);

        consultarUser();

        TextView btnMostraHiis = (TextView) findViewById(R.id.btnMostraHiis);
        btnMostraHiis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelaHistUsers();
            }
        });


        ImageButton ImbBtnCompartilhar = (ImageButton) findViewById(R.id.ImbBtnCompartilhar);
        ImbBtnCompartilhar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //pega nickname da outra tela
                Intent NickName = getIntent();
                String UserNick = NickName.getStringExtra("UserNick");

                //abre intent para compartilagr link do git
                Intent comp = new Intent(Intent.ACTION_SEND);
                comp.setType("text/plain");
                comp.putExtra(Intent.EXTRA_SUBJECT, "Link do GitHub");
                comp.putExtra(Intent.EXTRA_TEXT,"https://github.com/" + UserNick);
                startActivity(Intent.createChooser(comp, "Compartilhar para"));
            }
        });
    }

    private void consultarUser() {
        //pega nickname da outra tela
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
                if (response.isSuccessful()) {
                    UserGit  userGit= response.body();

                    //dados
                    nameUser.setText("Nome: "+userGit.getName());

                }
            }

            @Override
            public void onFailure(Call<UserGit> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro ao tentar consultar o Perfil. Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public  void TelaHistUsers(){
        Intent HistUsers = new Intent(getApplicationContext(), HistoricoUser.class);
        HistUsers.putExtra("UserNick",UserNick);
        startActivity(HistUsers);
    }
}