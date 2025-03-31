<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\UsuariosController;
use App\Http\Controllers\ProductosController;
use App\Http\Controllers\ProveedorController;

Route::get('/', function () {
    return view('welcome'); 
})->name('home'); 

Route::get('/login', function () {
    return view('login');
})->name('login');

Route::get('/dashboard', function () {
    return view('dashboard');
})->name('dashboard');

Route::resource('/Productos', ProductosController::class);
Route::get('/Productos/create', [ProductosController::class, 'create'])->name('Productos.create');

Route::resource('/Usuarios', UsuariosController::class);
Route::get('/Usuarios/create', [UsuariosController::class, 'create'])->name('Usuarios.create');

Route::resource('/Proveedores', ProveedorController::class);
Route::get('/Proveedores/create', [ProveedorController::class, 'create'])->name('Proveedores.create');


