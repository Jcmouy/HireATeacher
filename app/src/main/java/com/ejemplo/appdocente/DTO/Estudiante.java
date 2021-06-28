package com.ejemplo.appdocente.DTO;

public class Estudiante extends Usuario {

    private String idEstudinate;

    public Estudiante(String idUsuario, String username, String nombreCompleto, String email, int statis, String mobile, String direccion, int nro, String esquina, String ciudad, String pais, String info, String idEstudinate) {
        super(idUsuario, username, nombreCompleto, email, statis, mobile, direccion, nro, esquina, ciudad, pais, info);
        this.idEstudinate = idEstudinate;
    }

    public Estudiante(String idEstudinate) {
        this.idEstudinate = idEstudinate;
    }

    public String getIdEstudinate() {
        return idEstudinate;
    }

    public void setIdEstudinate(String idEstudinate) {
        this.idEstudinate = idEstudinate;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "idEstudinate=" + idEstudinate +
                '}';
    }
}
