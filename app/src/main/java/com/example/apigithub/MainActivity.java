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

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{
    //Link api
    private final String URL = "https://api.github.com/users/";
    private final String URLAPIPropria = "https://192.168.0.104:44368/api/User/MostraUser?Login_Usu=";

    BancoDeDados db=new BancoDeDados(this);

    private Retrofit retrofitGit;
    private Retrofit retrofitGitApi;
    private Retrofit retrofitGitPropria;
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


    /*
    private void consultaUserAPIProp() {

        //configura os recursos do retrofit
        retrofitGitApi = new Retrofit.Builder()
                .baseUrl(URLAPIPropria)                                       //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

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

            String linkPropria= URLAPIPropria+slogin;

            //instanciando a interface
            RESTService restService1 = retrofitGitPropria.create(RESTService.class);

            //passando os dados para consulta
            Call<UserGitApiPropia> call= restService1.consultaUserAPIProp(slogin);
            //Log.i("Link da Consulta", link);

            nameUser.setVisibility(View.VISIBLE);
            //colocando a requisição na fila para execução
            call.enqueue(new Callback<UserGitApiPropia>() {
                @Override
                public void onResponse(Call<UserGitApiPropia> call, Response<UserGitApiPropia> response) {
                    if (response.isSuccessful()) {
                        UserGitApiPropia  userGitApiPropia= response.body();

                        //mostra foto via url
                        Picasso.get().load(userGitApiPropia.getAvatar_url()).transform(new CropCircleTransformation()).into(ViewFotoUser);

                        if(userGitApiPropia.getName()==null){
                            nameUser.setText(userGitApiPropia.getLogin());
                        }
                        else {
                            nameUser.setText(userGitApiPropia.getName());
                        }
                        Toast.makeText(getApplicationContext(), "User encontrado", Toast.LENGTH_LONG).show();
                    }

                    else{
                        //alert para dizer que deu erro
                        //chamo api ofical
                        consultarUser();

                    }
                }


                @Override
                public void onFailure(Call<UserGitApiPropia> call, Throwable t) {
                //se der errado chamo api ofical
                    consultarUser();
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Sem Conexão", Toast.LENGTH_LONG).show();
        }
    }*/

    private void consultarUser() {

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

                        //mostra foto via url
                        Picasso.get().load(userGit.getAvatar_url()).transform(new CropCircleTransformation()).into(ViewFotoUser);

                        if(userGit.getName()==null){
                            nameUser.setText(userGit.getLogin());
                        }
                        else {
                            nameUser.setText(userGit.getName());
                        }
                        Toast.makeText(getApplicationContext(), "User encontrado", Toast.LENGTH_LONG).show();

                        //puxa da api oficail e manda pra parelela
                        /*CadastraNaApi(userGit.getLogin(), userGit.getAvatar_url(), userGit.getName(), userGit.getBio(), userGit.getPublic_project(),
                                userGit.getFollowers(), userGit.getFollowing(), userGit.getUltimoComit());

                         */
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


    //Inseri no banco
    public  void InseriUser(){
        String UserNick = NicknameEdit.getText().toString().toLowerCase(Locale.ROOT);

        if(UserNick.equals("")){
            Toast.makeText(getApplicationContext(), "Preencha o campo", Toast.LENGTH_LONG).show();
        }
        else {
            consultarUser();

            String UserName = nameUser.getText().toString();

            //seleciona user
            UserGit userGit = db.selecionarUser(UserNick);

            //se não existir no banco cadastra
            if (userGit.getLogin().equals("naoExiste")) {
                //insert
                if(UserName.equals("")){
                    UserName= userGit.getLogin();
                }
                db.addUser(new UserGit(UserNick, UserName, userGit.getAvatar_url()));
                TelaDados();
            }
            //se ja existir abre a outra tela direto
            else {
                TelaDados();
            }
        }

    }

    //inseri na api
    public  void CadastraNaApi(String login, String avatar_url, String name, String bio, String public_project, String followers, String following, String ultimoComit){

        //configura os recursos do retrofit
        retrofitGitApi = new Retrofit.Builder()
                .baseUrl(URLAPIPropria)                                       //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        RESTService retrofitAPI = retrofitGitPropria.create(RESTService.class);

        // passing data from our text fields to our modal class.
        UserGit GitApi = new UserGit(login, avatar_url, name, bio, public_project, followers, following, ultimoComit);

        // calling a method to create a post and passing our modal class.
        Call<UserGit> call = retrofitAPI.CadastraNaApi(GitApi);

        call.enqueue(new Callback<UserGit>() {
            @Override
            public void onResponse(Call<UserGit> call, Response<UserGit> response) {
                // this method is called when we get response from our api.
                Toast.makeText(MainActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();

                UserGit responseFromAPI = response.body();

            }

            @Override
            public void onFailure(Call<UserGit> call, Throwable t) {
                // mensagem de erro
                Toast.makeText(MainActivity.this, "erro"+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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