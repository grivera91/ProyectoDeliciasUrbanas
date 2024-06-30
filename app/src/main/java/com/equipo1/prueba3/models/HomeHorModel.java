package com.equipo1.prueba3.models;

public class HomeHorModel {

    int imageIcono;
    String name;
    String tipo;
    int imageAnimada;
    public HomeHorModel() {

    }
    public HomeHorModel(String name,String tipo) {
        this.name = name;
        this.tipo = tipo;
    }

    public HomeHorModel(int imageIcono, String name, String tipo, int imageAnimada) {
        this.imageIcono = imageIcono;
        this.name = name;
        this.tipo = tipo;
        this.imageAnimada = imageAnimada;
    }

    public int getImageIcono() {
        return imageIcono;
    }

    public void setImageIcono(int imageIcono) {
        this.imageIcono = imageIcono;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getImageAnimada() {
        return imageAnimada;
    }

    public void setImageAnimada(int imageAnimada) {
        this.imageAnimada = imageAnimada;
    }
}
