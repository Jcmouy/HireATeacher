package com.ejemplo.appdocente.Remote;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EstudianteService {

    @GET
    Call<List<EstudianteService>> getEstudiantes();

    /* Funcionalidad de Prueba
    @FormUrlEncoded
    @POST("students/createStudent")
    Call<JsonObject> insertEstudiante(@Field("lat") double latitude,
                                    @Field("lng") double longitude);

     */

    @FormUrlEncoded
    @POST("students/set_location")
    Call<JsonObject> setLocationEstudiante(@Field("mobile") String mobile,
                                           @Field("lat") double latitude,
                                           @Field("lng") double longitude);

    @FormUrlEncoded
    @POST("students/changeNotifState")
    Call<JsonObject> changeNotifStateEstudiante(@Field("idNotif") int idNotif);

    @FormUrlEncoded
    @POST("students/startCallCheckout")
    Call<JsonObject> startCallCheckout(@Field("email") String email,
                                           @Field("money") int money,
                                           @Field("teacher") String teacher);

    @FormUrlEncoded
    @POST("students/setHire")
    Call<JsonObject> setHire(@Field("idCalendario") String idCalendario,
                             @Field("mobileEstudiante") String mobileEstudiante,
                             @Field("nameModalidad") String nameModalidad);

    @FormUrlEncoded
    @POST("students/setHireNotification")
    Call<JsonObject> setHireNotification(@Field("idContratacion") String idContratacion,
                                         @Field("mensaje") String mensaje,
                                         @Field("tipeNotification") int tipeNotification);

    @FormUrlEncoded
    @POST("students/setNotificationVal")
    Call<JsonObject> setNotificationVal(@Field("idContratacion") String idContratacion,
                                         @Field("tipeNotification") int tipeNotification);

    @FormUrlEncoded
    @POST("students/setFilterCosto")
    Call<List<JsonObject>> setFilterCosto(@Field("nameAsig") String nameAsig,
                                          @Field("costoInicial") int costoInicial,
                                          @Field("costoFinal") int costoFinal);

    @FormUrlEncoded
    @POST("students/setFilterHorario")
    Call<List<JsonObject>> setFilterHorario(@Field("nameAsig") String nameAsig,
                                          @Field("dateStartTime") String dateStartTime,
                                          @Field("dateEndTime") String dateEndTime);

    @FormUrlEncoded
    @POST("students/setFeedbackTeacher")
    Call<JsonObject> setFeedbackTeacher(@Field("idNotification") int idNotification,
                                            @Field("feedback") String feedback,
                                            @Field("feedbackVal") int feedbackVal);

    @GET("students/getNotificationHire/{mobileEstudiante}")
    Call<List<JsonObject>> getNotificationHire(@Path("mobileEstudiante") String mobileEstudiante);

    @GET("students/getNotificationVal/{mobileEstudiante}")
    Call<List<JsonObject>> getNotificationVal(@Path("mobileEstudiante") String mobileEstudiante);

    @GET("students/locateAsig/{nameAsig}")
    Call<List<JsonObject>> locateAsig(@Path("nameAsig") String nameAsig);



}
