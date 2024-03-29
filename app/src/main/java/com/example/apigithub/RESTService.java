package com.example.apigithub;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RESTService {
    //facilta a comunicação entre a aplicação ea api
    //consultar User no webservice do Github API oficial
    @GET("{login}")
    Call<UserGit> consultaUser(@Path("login")String login);

    //APi propria
    /*@GET("{Login_Usu}")
    Call<UserGitApiPropia> consultaUserAPIProp(@Path("Login_Usu")String login);


    */

    @POST("DadosUser")
    Call<UserGit> CadastraNaApi(@Body UserGit userGit);
};


