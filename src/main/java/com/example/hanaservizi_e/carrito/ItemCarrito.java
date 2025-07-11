package com.example.hanaservizi_e.carrito;


import com.example.hanaservizi_e.model.Producto;

public class ItemCarrito {
    private Producto producto;
    private int cantidad;
    private String talla; // solo si es ropa
    private String color; // solo si es accesorio u otro

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

