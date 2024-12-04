package com.example.TuAmigoFiel;

public class ProductoMascota {

    public String id;
    public String nombre_producto;
    public String marca;
    public double precio;
    public String categoria;
    public String descripcion;

    public ProductoMascota() {
        // Constructor vacío requerido por Firebase
    }

    public ProductoMascota(String id, String nombre_producto, String marca, double precio, String categoria, String descripcion) {
        this.id = id;
        this.nombre_producto = nombre_producto;
        this.marca = marca;
        this.precio = precio;
        this.categoria = categoria;
        this.descripcion = descripcion;
    }
}