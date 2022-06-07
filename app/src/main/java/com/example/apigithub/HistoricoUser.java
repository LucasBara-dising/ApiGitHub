package com.example.apigithub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HistoricoUser extends AppCompatActivity {

    BancoDeDados db=new BancoDeDados(this);
    ArrayAdapter<String> adpater;
    ArrayList<String> arrayList;
    ListView ListViewUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_user);

        //definição do arry que lista os users
        arrayList = new ArrayList<String>();
        adpater = new ArrayAdapter<String>(HistoricoUser.this, android.R.layout.simple_list_item_1, arrayList);
        ListViewUsers = (ListView) findViewById(R.id.ListViewUsers);
        ListViewUsers.setAdapter(adpater);

        ListaTodosUsers();

        //ao clicar no item da lista
        ListViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String conteudo=(String) ListViewUsers.getItemAtPosition(position);

                //vai pegar oq estiver depois do "Nome: " na varaivel conteudo ouseja o login
                String login=conteudo.substring(10,conteudo.indexOf("Nome"));

                //UserGit userGit= db.selecionarUser(String.valueOf(login));
                //String UserNick= String.valueOf(login);


                //manda o login pra tela de detalhes
                Intent dadosUser = new Intent(getApplicationContext(), Dados_User.class);
                dadosUser.putExtra("UserNick",login);
                startActivity(dadosUser);
            }
        });

        ImageButton BtnVoltaDados= (ImageButton) findViewById(R.id.BtnVoltaDados);
        BtnVoltaDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NickName = getIntent();
                String UserNick = NickName.getStringExtra("UserNick");

                //envia nick para outra tela
                Intent DadosUser = new Intent(getApplicationContext(), Dados_User.class);
                DadosUser.putExtra("UserNick",UserNick);
                startActivity(DadosUser);
            }
        });


        TextView btnApagaHist= (TextView) findViewById(R.id.btnApagaHist);
        btnApagaHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //apaga historico
               db.ApagaTodosusers();
                finish();
                startActivity(getIntent());
            }
        });
    }

    public void ListaTodosUsers() {
        List<UserGit> TodosUsers = db.ListaTodosUsers();
        //loop para mostrar tudo
        for (UserGit c : TodosUsers) {
            //corpo do item list
            arrayList.add("NickName: " + c.getLogin() + "\n" +
                          "Nome: " + c.getName() + "\n");

            adpater.notifyDataSetChanged();
        }
    }

}