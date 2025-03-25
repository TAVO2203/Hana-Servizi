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
        Schema::create('pedidos', function (Blueprint $table) {
            $table->increments('id');
            $table->string('Nombre_productos');
            $table->date('FechaPedido');
            $table->string('EstadoPedido');
            $table->double('totalPedido');
            $table->unsignedInteger('idUsuarios');
            $table->unsignedInteger('idProductos');
            $table->timestamps();

            $table->foreign('idUsuarios')->references('id')->on('usuarios')->onDelete('cascade');
            $table->foreign('idProductos')->references('id')->on('productos')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('pedidos');
    }
};
