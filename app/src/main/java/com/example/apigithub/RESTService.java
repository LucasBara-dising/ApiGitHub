package com.example.apigithub;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RESTService {
    //facilta a comunicação entre a aplicação ea api
    //consultar User no webservice do Github
    @GET("{login}")
    Call<UserGit> consultaUser(@Path("login")String login);
}
