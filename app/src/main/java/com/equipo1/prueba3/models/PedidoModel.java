package com.equipo1.prueba3.models;

import java.util.Date;
import java.util.List;

public class PedidoModel {
    private String pedidoId;
    private Date fecha;
    private double total;
    private String estado;
    private List<Producto> productos;
    private double importeTotal;
    private String usuarioId;
    private String usuarioNombres;
    private String usuarioApellidos;
    public PedidoModel() {}

    public PedidoModel(String pedidoId, Date fecha, double total, String estado, List<Producto> productos, double importeTotal) {
        this.pedidoId = pedidoId;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
        this.productos = productos;
        this.importeTotal = importeTotal;
    }

    public PedidoModel(String pedidoId, Date fecha, double total, String estado, List<Producto> productos, double importeTotal, String usuarioId, String usuarioNombres, String usuarioApellidos) {
        this.pedidoId = pedidoId;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
        this.productos = productos;
        this.importeTotal = importeTotal;
        this.usuarioId = usuarioId;
        this.usuarioNombres = usuarioNombres;
        this.usuarioApellidos = usuarioApellidos;
    }

    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombres() {
        return usuarioNombres;
    }

    public void setUsuarioNombres(String usuarioNombres) {
        this.usuarioNombres = usuarioNombres;
    }

    public String getUsuarioApellidos() {
        return usuarioApellidos;
    }

    public void setUsuarioApellidos(String usuarioApellidos) {
        this.usuarioApellidos = usuarioApellidos;
    }
}
