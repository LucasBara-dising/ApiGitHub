package com.example.apigithub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{
    //Link api
    private final String URL = "https://api.github.com/users/";

    BancoDeDados db=new BancoDeDados(this);

    private Retrofit retrofitGit;
    private Button btnConsultarUserGit;
    private EditText NicknameEdit;
    private TextView nameUser;
    private ImageView ViewFotoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NicknameEdit = findViewById(R.id.NicknameEdit);
        ViewFotoUser = findViewById(R.id.ViewFotoUser);
        nameUser = findViewById(R.id.nameUser);
        btnConsultarUserGit = findViewById(R.id.btnConsultarUserGit);


        //configura os recursos do retrofit
        retrofitGit = new Retrofit.Builder()
                .baseUrl(URL)                                       //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        //metodo para capatr mundança de texto
        NicknameEdit.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                //metodo obrigatorio
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
              //metodo obrigatorio
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Quando o texto for mudado
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nameUser.setVisibility(View.VISIBLE);
                        consultarUser();
                    }
                },2000);//2seg
            }
        });


        Button btnConsultarUserGit = (Button) findViewById(R.id.btnConsultarUserGit);
        btnConsultarUserGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelaDados();
            }
        });
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


        //colocando a requisição na fila para execução
        call.enqueue(new Callback<UserGit>() {
            @Override
            public void onResponse(Call<UserGit> call, Response<UserGit> response) {
                if (response.isSuccessful()) {
                    UserGit  userGit= response.body();
                    nameUser.setText(userGit.getName());
                    //Toast.makeText(getApplicationContext(), "User encontrado", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserGit> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro ao tentar consultar o Perfil. Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public  void TelaDados(){
        String UserNick = NicknameEdit.getText().toString();
        String UserName = nameUser.getText().toString();

        //insert
        db.addUser(new UserGit(UserNick, UserName));

        Intent dadosUser = new Intent(getApplicationContext(), Dados_User.class);
        dadosUser.putExtra("UserNick",UserNick);
        startActivity(dadosUser);
        finish();
    }
}