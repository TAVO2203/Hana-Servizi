<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Productos extends Model
{
    protected $table = 'productos';
    protected $primaryKey = 'id';
    protected $fillable = ['Nombre', 'Precio', 'Fecha_agregacion', 'EstadoProducto', 'Descripcion', 'idCategoria', 'idMarca','imagen'];
}
