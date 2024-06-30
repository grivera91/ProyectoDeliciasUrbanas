package com.equipo1.prueba3.models;

import java.util.List;

public class Usuario {
    String usuarioId, nombres, apellidos, dni ,correo, contrasenia, genero,tipo;
    private List<PedidoModel> pedidos;

    public Usuario() {}

    public Usuario(String usuarioId, String nombres, String apellidos, String dni, String correo, String contrasenia, String genero, String tipo) {
        this.usuarioId = usuarioId;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.genero = genero;
        this.tipo = tipo;
    }

    public Usuario(String usuarioId, String nombres, String apellidos, String dni, String correo, String contrasenia, String genero, String tipo, List<PedidoModel> pedidos) {
        this.usuarioId = usuarioId;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.genero = genero;
        this.tipo = tipo;
        this.pedidos = pedidos;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDNI() {
        return dni;
    }

    public void setDNI(String edad) {
        this.dni = dni;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<PedidoModel> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<PedidoModel> pedidos) {
        this.pedidos = pedidos;
    }
}
