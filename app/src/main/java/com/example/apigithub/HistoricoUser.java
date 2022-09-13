package com.example.apigithub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoricoUser extends AppCompatActivity {

    BancoDeDados db;
    ArrayList<UserGit> ListaUsers;
    RecyclerView recyclerView;
    MyAdapter adpaterRecycler;

    //giroscopio
    SensorManager sensorManager;
    Sensor sensor;

    //localização
    LocationManager locationMangaer = null;
    LocationListener locationListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_user);

        //definição do arry que lista os users
        ListaUsers = new ArrayList<UserGit>();
        db = new BancoDeDados(this);
        ListaUsers = (ArrayList<UserGit>) db.ListaTodosUsers();

        //instancia o recycler
        adpaterRecycler = new MyAdapter(ListaUsers, HistoricoUser.this);
        recyclerView = findViewById(R.id.recyclerView);
        //configura o recycler
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
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
        ImageButton BtnVoltaDados = (ImageButton) findViewById(R.id.BtnVoltaDados);
        BtnVoltaDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VoltaParaDadosUser();
            }
        });


        //button para apagar historico
        TextView btnApagaHist = (TextView) findViewById(R.id.btnApagaHist);
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


        //Localização
        locationMangaer = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    public void VoltaParaDadosUser() {
        Intent NickName = getIntent();
        String UserNick = NickName.getStringExtra("UserNick");

        //envia nick para outra tela
        Intent DadosUser = new Intent(getApplicationContext(), Dados_User.class);
        DadosUser.putExtra("UserNick", UserNick);
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
            float x = event.values[0];

            // se jogar  celular de lado
            if (y > 4) {
                Toast.makeText(getApplicationContext(), "Não me balança doido >:", Toast.LENGTH_LONG).show();
            }

            //se mover para cma ou baixo
            if(x>3 || x<-3){
                Toast.makeText(getApplicationContext(), "Isso pode demorar", Toast.LENGTH_LONG).show();
                Boolean flag = displayGpsStatus();
                if (flag) {

                    if(ContextCompat.checkSelfPermission(HistoricoUser.this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(HistoricoUser.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                    PackageManager.PERMISSION_GRANTED) {

                        LocationListener locationListener = new MyLocation();
                        locationMangaer.requestLocationUpdates(LocationManager
                                .GPS_PROVIDER, 5000, 10, locationListener);

                    } else {
                        Toast.makeText(getApplicationContext(), "não tem permissão ", Toast.LENGTH_LONG).show();
                        checkLocationPermission();
                    }

                } else {
                    Log.i("Gps Status!!", "Your GPS is: OFF");
                }
            }

        }
    };

    //verifica se o gps ta ligado
    public Boolean displayGpsStatus(){
        ContentResolver contentResolver= getBaseContext().getContentResolver();
        boolean gpsStaus= Settings.Secure.isLocationProviderEnabled(contentResolver,LocationManager.GPS_PROVIDER);

        return gpsStaus;
    }

    //recebe as cordenadas
    public  class  MyLocation implements LocationListener{

        @Override
        public void onLocationChanged(@NonNull Location location) {

            String longitude = "Longitude: " +location.getLongitude();
            Log.i("Longitude: ", longitude);
            String latitude = "Latitude: " +location.getLatitude();
            Log.v("Latitude: ", latitude);

            String cidade=null;
            Geocoder geocoder= new Geocoder(getBaseContext(), Locale.getDefault());

            List<Address> addresses;
            try{
                addresses=geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                if(addresses.size()>0){
                    cidade=addresses.get(0).getLocality();
                    Log.v("cidade: ", "city: "+cidade);
                    String scity="city: "+cidade;

                    //alert para dizer coodenadas
                    AlertDialog.Builder alert = new AlertDialog.Builder(HistoricoUser.this);
                    alert.setTitle("Sua Coordanada :)");
                    alert.setMessage(longitude+"\n "+latitude + "\n"+scity);
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //checa permissao
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Permissão de Localização nessesaria")
                        .setMessage("Esta Função nessesita da localização para funcionar")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(HistoricoUser.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();
            } else {
                //soliita permissao
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

}