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
        Schema::create('inventario', function (Blueprint $table) {
            $table->increments('id');
            $table->string('Nombre');
            $table->string('Categoria');
            $table->Double('Costo_producto');
            $table->float('Stock');
            $table->Date('Fecha_agregacion');
            $table->string('EstadoProducto');
            $table->unsignedInteger('idProducto');
            $table->unsignedInteger('idProveedor');
            $table->timestamps();
        
            $table->foreign('idProducto')->references('id')->on('productos');
            $table->foreign('idProveedor')->references('id')->on('proveedor');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('inventario');
    }
};
