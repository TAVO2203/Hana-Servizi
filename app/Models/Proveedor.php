<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Proveedor extends Model
{
    protected $table = 'proveedor';
    protected $primaryKey = 'id';
    protected $fillable = ['Nombre', 'Email', 'Telefono', 'Ciudad', 'Direccion'];

}
