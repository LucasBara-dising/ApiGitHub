package com.example.apigithub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dados_User extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_user);

        consultarUser();

        TextView btnMostraHiis = (TextView) findViewById(R.id.btnMostraHiis);
        btnMostraHiis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelaHistUsers();
            }
        });

        //compartlhar
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

        Intent login = getIntent();
        String UserNick = login.getStringExtra("UserNick");

        Intent User = getIntent();
        String UserNickFromHist = User.getStringExtra("User");

        if(UserNickFromHist==null) {
            String URL = "https://api.github.com/users/";
            String linkNoMostradados = URL + UserNick;

            //configura os recursos do retrofit
            Retrofit retrofitGit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();

            //instanciando a interface
            RESTService restService = retrofitGit.create(RESTService.class);

            //passando os dados para consulta
            Call<UserGit> call = restService.consultaUser(UserNick);

            //colocando a requisição na fila para execução
            call.enqueue(new Callback<UserGit>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<UserGit> call, @NonNull Response<UserGit> response) {
                    if (response.isSuccessful()) {
                        UserGit userGit = response.body();

                        TextView nameUser = findViewById(R.id.twName);
                        TextView twloginUser = findViewById(R.id.twloginUser);
                        TextView twbio = findViewById(R.id.twbio);
                        TextView twProjetos = findViewById(R.id.twProjetos);
                        TextView twSeguidores = findViewById(R.id.twSeguidores);
                        TextView twSeguindo = findViewById(R.id.twSeguindo);
                        TextView twEmpresa = findViewById(R.id.twEmpresa);
                        TextView twLocalizacao = findViewById(R.id.twLocalizacao);
                        TextView twUltupdate = findViewById(R.id.twUltupdate);

                        ImageView imgPredio = findViewById(R.id.imgPredio);
                        ImageView imgLocal = findViewById(R.id.imgLocal);

                        Log.i("Link da Consulta No mostra Dados", linkNoMostradados);
                        //dados
                        if (userGit.getName() == null) {
                            nameUser.setVisibility(View.GONE);
                        } else {
                            nameUser.setText(userGit.getName());
                        }

                        twloginUser.setText(userGit.getLogin());

                        if (userGit.getBio() == null) {
                            twbio.setVisibility(View.GONE);
                        } else {
                            twbio.setText("Bio: " + userGit.getBio());
                        }

                        twProjetos.setText("Projetos Publicados: " + userGit.getPublic_project());
                        twSeguidores.setText("Seguidores: " + userGit.getFollowers());
                        twSeguindo.setText("Seguindo: " + userGit.getFollowing());


                        if (userGit.getCompany() == null) {
                            twEmpresa.setVisibility(View.GONE);
                            imgPredio.setVisibility(View.GONE);
                        } else {
                            twEmpresa.setText("Compania: " + userGit.getCompany());
                        }

                        if (userGit.getLocation() == null) {
                            twLocalizacao.setVisibility(View.GONE);
                            imgLocal.setVisibility(View.GONE);
                        } else {
                            twLocalizacao.setText("Localização: " + userGit.getLocation());
                        }

                        String data = userGit.getUltimoComit();
                        String datanova = data.substring(0, 10);
                        String dataComBarra = datanova.replace("-", "/");
                        twUltupdate.setText("Ultimo Update: " + dataComBarra);
                    } else {
                        //alert para dizer que deu erro
                        //erro de limiti de pesquisas- Limite de 60 por hora
                        AlertDialog.Builder alert = new AlertDialog.Builder(Dados_User.this);
                        alert.setTitle("Ops :/");
                        alert.setMessage("Ocorreu um erro");
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    }
                }

                @Override
                public void onFailure(Call<UserGit> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro ao tentar consultar o Perfil. Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else{
            String URL = "https://api.github.com/users/";
            String linkNoMostradados = URL + UserNickFromHist;

            Log.d("Log Foi Do hist",linkNoMostradados);

            //configura os recursos do retrofit com a api
            Retrofit retrofitGit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();

            //instanciando a interface
            RESTService restService = retrofitGit.create(RESTService.class);

            //passando os dados para consulta
            Call<UserGit> call = restService.consultaUser(UserNickFromHist);

            //colocando a requisição na fila para execução
            call.enqueue(new Callback<UserGit>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<UserGit> call, @NonNull Response<UserGit> response) {
                    if (response.isSuccessful()) {
                        UserGit userGit = response.body();

                        TextView nameUser = findViewById(R.id.twName);
                        TextView twloginUser = findViewById(R.id.twloginUser);
                        TextView twbio = findViewById(R.id.twbio);
                        TextView twProjetos = findViewById(R.id.twProjetos);
                        TextView twSeguidores = findViewById(R.id.twSeguidores);
                        TextView twSeguindo = findViewById(R.id.twSeguindo);
                        TextView twEmpresa = findViewById(R.id.twEmpresa);
                        TextView twLocalizacao = findViewById(R.id.twLocalizacao);
                        TextView twUltupdate = findViewById(R.id.twUltupdate);

                        ImageView imgPredio = findViewById(R.id.imgPredio);
                        ImageView imgLocal = findViewById(R.id.imgLocal);

                        Log.i("Link da Consulta No mostra Dados", linkNoMostradados);
                        //dados
                        if (userGit.getName() == null) {
                            nameUser.setVisibility(View.GONE);
                        } else {
                            nameUser.setText(userGit.getName());
                        }

                        twloginUser.setText(userGit.getLogin());

                        if (userGit.getBio() == null) {
                            twbio.setVisibility(View.GONE);
                        } else {
                            twbio.setText("Bio: " + userGit.getBio());
                        }

                        twProjetos.setText("Projetos Publicados: " + userGit.getPublic_project());
                        twSeguidores.setText("Seguidores: " + userGit.getFollowers());
                        twSeguindo.setText("Seguindo: " + userGit.getFollowing());


                        if (userGit.getCompany() == null) {
                            twEmpresa.setVisibility(View.GONE);
                            imgPredio.setVisibility(View.GONE);
                        } else {
                            twEmpresa.setText("Compania: " + userGit.getCompany());
                        }

                        if (userGit.getLocation() == null) {
                            twLocalizacao.setVisibility(View.GONE);
                            imgLocal.setVisibility(View.GONE);
                        } else {
                            twLocalizacao.setText("Localização: " + userGit.getLocation());
                        }

                        String data = userGit.getUltimoComit();
                        String datanova = data.substring(0, 10);
                        String dataComBarra = datanova.replace("-", "/");
                        twUltupdate.setText("Ultimo Update: " + dataComBarra);
                    }

                    else {
                        Log.d("corpo", String.valueOf(response.code()));
                        //alert para dizer que deu erro
                        //erro de limiti de pesquisas- Limite de 60 por hora
                        AlertDialog.Builder alert = new AlertDialog.Builder(Dados_User.this);
                        alert.setTitle("Ops :/");
                        alert.setMessage("Ocorreu um erro");
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    }
                }

                @Override
                public void onFailure(Call<UserGit> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro ao tentar consultar o Perfil. Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public  void TelaHistUsers(){
        TextView  twloginUser = findViewById(R.id.twloginUser);
        String UserNick =twloginUser.getText().toString();

        Intent HistUsers = new Intent(getApplicationContext(), HistoricoUser.class);
        HistUsers.putExtra("UserNick",UserNick);
        startActivity(HistUsers);
    }
}