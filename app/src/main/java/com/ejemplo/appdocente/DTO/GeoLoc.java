package com.ejemplo.appdocente.DTO;

public class GeoLoc {

    private int idGeoLoc;
    private double latitude;
    private double longitude;
    private int idUsuario;

    public GeoLoc() {
    }

    public int getIdGeoLoc() {
        return idGeoLoc;
    }

    public void setIdGeoLoc(int idGeoLoc) {
        this.idGeoLoc = idGeoLoc;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
