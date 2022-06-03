package com.example.apigithub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BancoDeDados  extends SQLiteOpenHelper {
    //tabela
    public static final String Tabela_User = "tbUsers";
    public static final String Coluna_LoginUser = "LoginUser";
    public static final String Coluna_NomeUser = "nomeUser";

    private static final String DATABASE_Nome = "BDGitUsers.db";
    private static final int DATABASE_VERSION = 1;

    public BancoDeDados(Context context) {
        super(context, DATABASE_Nome, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Criacao_tabelaUser = "create table " + Tabela_User + "( "
                + Coluna_LoginUser + " text primary key, "
                + Coluna_NomeUser + " text not null);";

        //executa a criaça da tb user sqlite
        db.execSQL(Criacao_tabelaUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //====================================== User GitHub==========================
    //Insert User
    void addUser(UserGit userGit){
        //estancia para escrita no banco
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(Coluna_LoginUser, userGit.getLogin());
        values.put(Coluna_NomeUser, userGit.getName());

        //inseri no banco
        db.insert(Tabela_User, null, values);
        db.close();
    }

    //select por login
    UserGit selecionarUser(String login){
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.query(Tabela_User,
                new String[]{Coluna_LoginUser, Coluna_NomeUser},
                Coluna_LoginUser+"=?",new String[]{String.valueOf(login)},null, null, null,null);
        if(cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
        }

        else if(cursor.getCount() == 0){
            UserGit userGitEspecifico= new UserGit("naoExiste", "naoExiste");
            return userGitEspecifico;
        }


        UserGit userGitEspecifico= new UserGit(cursor.getString(0), cursor.getString(1));
        return userGitEspecifico;

    }

    //lista Todos
    public List<UserGit> ListaTodosUsers(){
        List<UserGit>  ListaUsers= new ArrayList<UserGit>();

        String query= "SELECT * FROM " + Tabela_User;

        SQLiteDatabase db=this.getReadableDatabase();

        Cursor c =db.rawQuery(query,null);

        if(c.moveToFirst()){
            do{
                UserGit userGit= new UserGit();
                userGit.setLogin(c.getString(0));
                userGit.setLogin(c.getString(1));

                ListaUsers.add(userGit);

            }while (c.moveToNext());
        }
        return  ListaUsers;
    }


}
