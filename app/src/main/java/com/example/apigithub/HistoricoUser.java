package com.example.apigithub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        //definição do arry que lista os projetos
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
                String login=conteudo.substring(6,conteudo.indexOf("NickName"));

                UserGit userGit= db.selecionarUser(String.valueOf(login));

                String UserNick= String.valueOf(login);

                //manda o cod pra tela de detalhes
                Intent dadosUser = new Intent(getApplicationContext(), Dados_User.class);
                dadosUser.putExtra("UserNick",UserNick);
                startActivity(dadosUser);
            }
        });
    }

    public void ListaTodosUsers() {
        List<UserGit> TodosUsers = db.ListaTodosUsers();

        //loop para mostrar tudo
        for (UserGit c : TodosUsers) {
            //corpo do item list
            arrayList.add("Nome: " + c.getLogin() + "\n" +
                    "NickName: " + c.getLogin() + "\n");

            adpater.notifyDataSetChanged();
        }
    }
}