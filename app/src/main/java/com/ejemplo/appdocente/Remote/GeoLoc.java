package com.ejemplo.appdocente.Remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GeoLoc {

    @GET("/geoloc/docente")
    Call<List<GeoLoc>> getGeoLocs();
}
