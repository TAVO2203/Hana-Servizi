<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('productos', function (Blueprint $table) {
            $table->increments('id');
            $table->string('Nombre');
            $table->Double('Precio');
            $table->date('Fecha_agregacion');
            $table->string('EstadoProducto');
            $table->string('Descripcion');
            $table->unsignedInteger('idCategoria');
            $table->unsignedInteger('idMarca');
            $table->string('Imagen')->nullable();
            $table->timestamps();
        
            $table->foreign('idCategoria')->references('id')->on('categorias');
            $table->foreign('idMarca')->references('id')->on('marca');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('productos');
    }
};
