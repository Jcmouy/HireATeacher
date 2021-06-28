package com.ejemplo.appdocente.DTO;

public class LocalizacionUsuario {

    private String idLocalizacionUsuario = "";
    private double lat = 0;
    private double lng = 0;
    private Usuario usuario;

    public LocalizacionUsuario(String idLocalizacionUsuario, double lat, double lng, Usuario usuario) {
        this.idLocalizacionUsuario = idLocalizacionUsuario;
        this.lat = lat;
        this.lng = lng;
        this.usuario = usuario;
    }

    public String getIdLocalizacionUsuario() {
        return idLocalizacionUsuario;
    }

    public void setIdLocalizacionUsuario(String idLocalizacionUsuario) {
        this.idLocalizacionUsuario = idLocalizacionUsuario;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "LocalizacionUsuario{" +
                "idLocalizacionUsuario='" + idLocalizacionUsuario + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", usuario=" + usuario +
                '}';
    }
}
