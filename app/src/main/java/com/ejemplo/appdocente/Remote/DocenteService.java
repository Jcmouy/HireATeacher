package com.ejemplo.appdocente.Remote;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DocenteService {

    @GET("teachers")
    Call<List<JsonObject>> getDocentes();

    @GET("teachers/teachCalTime/{id}/{nameAsig}/{nameNivel}")
    Call<List<JsonObject>> teachCalTime(@Path("id") String id,
                                        @Path("nameAsig") String nameAsig,
                                        @Path("nameNivel") String nameNivel);

    @FormUrlEncoded
    @POST("teachers/authenticate")
    Call<JsonObject> authenticateTeacher(@Field("username") String username,
                                         @Field("password") String password,
                                         @Field("mobile") boolean mobile);

    @GET("teachers/teachNotifCont/{id}")
    Call<List<JsonObject>> teachNotifCont(@Path("id") String id);

}
