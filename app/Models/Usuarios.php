<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class Usuarios extends Model
{
    use HasFactory;
    protected $table = 'usuarios';
    protected $primaryKey = 'id';
    protected $fillable = [
        'Nombre',
        'Email',
        'Contraseña',
        'Telefono',
        'Direccion',
    ];
    
    protected $hidden = [
        'Contraseña',
    ];
    public $timestamps = false;
    
}
