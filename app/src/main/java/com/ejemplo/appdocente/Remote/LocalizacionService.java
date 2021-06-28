package com.ejemplo.appdocente.Remote;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by JC on 6/12/2017.
 */

public interface LocalizacionService {
    /*@Headers("Content-Type: application/json")*/
    @FormUrlEncoded
    @POST("localizacion/insert")
    Call<JsonObject> insert(@Field("Lat") double latitude,
                            @Field("Lon") double longitude,
                            @Field("idUsuario") int idusuario);

    /*
    @GET("localizaciones/getIconos")
    Call<List<JsonObject>> getAllIconos();

    @FormUrlEncoded
    @POST("localizacion_privado/insert")
    Call<JsonObject> insertPrivado(@Field("Localizacion_Id") long localizacion_id,
                                   @Field("Remitente") String remitente,
                                   @Field("Destinatario") String destinatario);




    @GET("localizacion_privado/getIconos/{myNumber}")
    Call<List<JsonObject>> getAllIconosPrivados(@Path("myNumber") String myNumber);

    @GET("localizaciones_notificacion/get/{myNumber}")
    Call<List<JsonObject>> getLocalizacionesNotificacion(@Path("myNumber") String myNumber);


     */
}
