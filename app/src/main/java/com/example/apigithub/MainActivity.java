package com.example.apigithub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Locale;

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


        Button btnConsultarUserGit = (Button) findViewById(R.id.btnConsultarUserGit);
        btnConsultarUserGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarUser();
            }
        });


        Button btnVerMais = (Button) findViewById(R.id.btnVerMais);
        btnVerMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InseriUser();
            }
        });
    }


    private void consultarUser() {

        // Verifica o status da conexão de rede
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()){
            //pega nickname
            String slogin = NicknameEdit.getText().toString().trim();

            String link= URL+slogin;

            //instanciando a interface
            RESTService restService = retrofitGit.create(RESTService.class);

            //passando os dados para consulta
            Call<UserGit> call= restService.consultaUser(slogin);
            //Log.i("Link da Consulta", link);

            nameUser.setVisibility(View.VISIBLE);
            //colocando a requisição na fila para execução
            call.enqueue(new Callback<UserGit>() {
                @Override
                public void onResponse(Call<UserGit> call, Response<UserGit> response) {
                    if (response.isSuccessful()) {
                        UserGit  userGit= response.body();

                        //Uri uri= Uri.parse("https://avatars.githubusercontent.com/u/82176900?v=4");
                        //ViewFotoUser.setImageURI(uri);

                        if(userGit.getName()==null){
                            nameUser.setText(userGit.getLogin());
                        }
                        else {
                            nameUser.setText(userGit.getName());
                        }
                        Toast.makeText(getApplicationContext(), "User encontrado", Toast.LENGTH_LONG).show();
                    }

                    else{
                        //alert para dizer que deu erro
                        //erro de limiti de pesquisas- Limite de 60 por hora
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("Ops :/");
                        alert.setMessage("User não encontrado");
                        alert.setPositiveButton("OK",null);
                        alert.show();

                        onPause();
                    }
                }

                @Override
                public void onFailure(Call<UserGit> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro ao tentar consultar o Perfil. Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Sem Conexão", Toast.LENGTH_LONG).show();
        }
    }

    public  void InseriUser(){
        String UserNick = NicknameEdit.getText().toString().toLowerCase(Locale.ROOT);

        if(UserNick.equals("")){
            Toast.makeText(getApplicationContext(), "Freencha o campo", Toast.LENGTH_LONG).show();
        }
        else {
            consultarUser();

            String UserName = nameUser.getText().toString();

            //seleciona user
            UserGit userGit = db.selecionarUser(UserNick);

            //se não existir no banco cadastra
            if (userGit.getLogin().equals("naoExiste")) {
                //insert
                db.addUser(new UserGit(UserNick, UserName));
                TelaDados();
            }
            //se ja existir abre a outra tela direto
            else {
                TelaDados();
            }
        }

    }

    //abre tela dados
    public  void TelaDados(){
        String UserNick = NicknameEdit.getText().toString();

        //envia nick para outra tela
        Intent dadosUser = new Intent(getApplicationContext(), Dados_User.class);
        dadosUser.putExtra("UserNick",UserNick);
        startActivity(dadosUser);
    }
}