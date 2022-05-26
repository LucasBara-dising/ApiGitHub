package com.example.apigithub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String URL = "https://api.github.com/users/";

    private Retrofit retrofitGit;
    private Button btnConsultarUserGit;
    private EditText NicknameEdit;
    private TextView nameUser;
    private ImageView ViewFotoUser;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NicknameEdit = findViewById(R.id.NicknameEdit);
        ViewFotoUser = findViewById(R.id.ViewFotoUser);
        nameUser = findViewById(R.id.nameUser);
        btnConsultarUserGit = findViewById(R.id.btnConsultarUserGit);
        progressBar = findViewById(R.id.progressBar);

        //configurando como invisível
        progressBar.setVisibility(View.GONE);

        //configura os recursos do retrofit
        retrofitGit = new Retrofit.Builder()
                .baseUrl(URL)                                       //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        btnConsultarUserGit.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        consultarUser();

    }


    private void consultarUser() {

        //pega nickname
        String slogin = NicknameEdit.getText().toString().trim();

        String link= URL+slogin;

        //instanciando a interface
        RESTService restService = retrofitGit.create(RESTService.class);

        //passando os dados para consulta
        Call<UserGit> call= restService.consultaUser(slogin);
        Log.i("Link da Consulta", link);

        //exibindo a progressbar
        progressBar.setVisibility(View.VISIBLE);

        //colocando a requisição na fila para execução
        call.enqueue(new Callback<UserGit>() {
            @Override
            public void onResponse(Call<UserGit> call, Response<UserGit> response) {
                if (response.isSuccessful()) {
                    UserGit  userGit= response.body();
                    nameUser.setText(userGit.getName());

                    Toast.makeText(getApplicationContext(), "User encontrado", Toast.LENGTH_LONG).show();

                    //escondendo a progressbar
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<UserGit> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro ao tentar consultar o Perfil. Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();

                //escondendo a progressbar
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}