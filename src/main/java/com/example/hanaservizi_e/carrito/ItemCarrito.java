package com.example.hanaservizi_e.carrito;

public class ItemCarrito {
    private String id;
    private String nombre;
    private double precio;
    private String imagen;
    private int cantidad;
    private String talla;
    private String clave; // <- esta es la nueva propiedad

    public ItemCarrito(String id, String nombre, double precio, String imagen, int cantidad, String talla) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.cantidad = cantidad;
        this.talla = talla;

        // Generamos la clave combinando id + talla
        this.clave = (talla != null && !talla.isEmpty()) ? id + "-" + talla : id;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }

    public String getClave() { return clave; }  // <- getter para tu controlador
    public void setClave(String clave) { this.clave = clave; }
}
