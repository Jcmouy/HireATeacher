package com.ejemplo.appdocente.DTO;

import java.util.Objects;

public class Usuario {

    private String idUsuario = "";
    private String username = "";
    private String nombreCompleto = "";
    private String Email = "";
    private int status = 0;
    private String mobile = "";
    private String direccion = "";
    private int nro = 0;
    private String esquina = "";
    private String ciudad = "";
    private String pais = "";
    private String info = "";

    public Usuario(String idUsuario, String username, String nombreCompleto, String email, int status, String mobile, String direccion, int nro, String esquina, String ciudad, String pais, String info) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.nombreCompleto = nombreCompleto;
        Email = email;
        this.status = status;
        this.mobile = mobile;
        this.direccion = direccion;
        this.nro = nro;
        this.esquina = esquina;
        this.ciudad = ciudad;
        this.pais = pais;
        this.info = info;
    }

    public Usuario() {
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getStatis() {
        return status;
    }

    public void setStatis(int status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getNro() {
        return nro;
    }

    public void setNro(int nro) {
        this.nro = nro;
    }

    public String getEsquina() {
        return esquina;
    }

    public void setEsquina(String esquina) {
        this.esquina = esquina;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return status == usuario.status &&
                nro == usuario.nro &&
                Objects.equals(idUsuario, usuario.idUsuario) &&
                Objects.equals(username, usuario.username) &&
                Objects.equals(nombreCompleto, usuario.nombreCompleto) &&
                Objects.equals(Email, usuario.Email) &&
                Objects.equals(mobile, usuario.mobile) &&
                Objects.equals(direccion, usuario.direccion) &&
                Objects.equals(esquina, usuario.esquina) &&
                Objects.equals(ciudad, usuario.ciudad) &&
                Objects.equals(pais, usuario.pais) &&
                Objects.equals(info, usuario.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, username, nombreCompleto, Email, status, mobile, direccion, nro, esquina, ciudad, pais, info);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario='" + idUsuario + '\'' +
                ", username='" + username + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", Email='" + Email + '\'' +
                ", status=" + status +
                ", mobile='" + mobile + '\'' +
                ", direccion='" + direccion + '\'' +
                ", nro=" + nro +
                ", esquina='" + esquina + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", pais='" + pais + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
