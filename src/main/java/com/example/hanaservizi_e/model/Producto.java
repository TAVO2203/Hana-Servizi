package com.example.hanaservizi_e.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Double precio;
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_agregacion")
    private Date fechaAgregacion;
    private String estadoProducto;
    private String descripcion;
    private String imagen;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Date getFechaAgregacion() {
        return fechaAgregacion;
    }

    public void setFechaAgregacion(Date fechaAgregacion) {
        this.fechaAgregacion = fechaAgregacion;
    }

    public String getEstadoProducto() {
        return estadoProducto;
    }

    public void setEstadoProducto(String estadoProducto) {
        this.estadoProducto = estadoProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Categorias getCategorias() {
        return categorias;
    }

    public void setCategorias(Categorias categorias) {
        this.categorias = categorias;
    }

    public User getVendedor() {
        return vendedor;
    }

    public void setVendedor(User vendedor) {
        this.vendedor = vendedor;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", fechaAgregacion=" + fechaAgregacion +
                ", estadoProducto='" + estadoProducto + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagen='" + imagen + '\'' +
                ", marca=" + marca +
                ", categorias=" + categorias+
                '}';
    }

    @ManyToOne
    private Marca marca;

    @ManyToOne
    private Categorias categorias;

    @ManyToOne
    @JoinColumn(name="vendedor_id")
    private User  vendedor;
}
