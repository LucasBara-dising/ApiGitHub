package com.example.apigithub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HistoricoUser extends AppCompatActivity {

    //BancoDeDados db=new BancoDeDados(this);
    BancoDeDados db;
    ArrayAdapter<String> adpater;
    ArrayList<UserGit> ListaUsers;
    ListView ListViewUsers;
    RecyclerView recyclerView;
    MyAdapter adpaterRecycler;

    //giroscopio
    SensorManager sensorManager;
    Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_user);

        //definição do arry que lista os users
        ListaUsers=new ArrayList<UserGit>();
        db =new BancoDeDados(this);
        ListaUsers= (ArrayList<UserGit>) db.ListaTodosUsers();

        //instancia o recycler
        adpaterRecycler= new MyAdapter(ListaUsers,HistoricoUser.this);
        recyclerView=findViewById(R.id.recyclerView);
        //configura o recycler
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adpaterRecycler);


        //instancianod giroscopio
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        //ao clicar no item da lista
        /*ListViewUsers = (ListView) findViewById(R.id.ListViewUsers);
        ListViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String conteudo=(String) ListViewUsers.getItemAtPosition(position);

                //vai pegar oq estiver depois do "Nome: " na varaivel conteudo ouseja o login
                String login=conteudo.substring(10,conteudo.indexOf("Nome"));

                Log.d("User:", login);

                //manda o login pra tela de detalhes
                Intent dadosUser = new Intent(getApplicationContext(), Dados_User.class);
                dadosUser.putExtra("User",login);
                startActivity(dadosUser);
            }
        });*/

        //volta para os dados
        ImageButton BtnVoltaDados= (ImageButton) findViewById(R.id.BtnVoltaDados);
        BtnVoltaDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VoltaParaDadosUser();
            }
        });


        //button para apagar historico
        TextView btnApagaHist= (TextView) findViewById(R.id.btnApagaHist);
        btnApagaHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //apaga historico
               db.ApagaTodosusers();
               //recarrega a pagina
                finish();
                startActivity(getIntent());
            }
        });
    }

    public  void VoltaParaDadosUser(){
        Intent NickName = getIntent();
        String UserNick = NickName.getStringExtra("UserNick");

        //envia nick para outra tela
        Intent DadosUser = new Intent(getApplicationContext(), Dados_User.class);
        DadosUser.putExtra("UserNick",UserNick);
        startActivity(DadosUser);
    }


    //sensor giroscopio
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(gyroListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(gyroListener);
    }

    public SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            float y = event.values[1];

            if(y>4){
                Toast.makeText(getApplicationContext(), "Não me balança doido >:", Toast.LENGTH_LONG).show();
            }

        }
    };

}