<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\UsuariosController;

Route::get('/', function () {
    return view('welcome'); 
})->name('home'); 

Route::resource('/Usuarios', UsuariosController::class);
Route::get('/Usuarios/create', [UsuariosController::class, 'create'])->name('Usuarios.create');