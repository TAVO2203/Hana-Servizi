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
        Schema::create('detalles_pedidos', function (Blueprint $table) {
            $table->increments('id');
            $table->string('Nombre_productos');
            $table->float('cantidad');
            $table->float('precio');
            $table->float('total');
            $table->unsignedInteger('producto_id');
            $table->unsignedInteger('pedido_id');
            $table->timestamps();

            $table->foreign('producto_id')->references('id')->on('productos')->onDelete('cascade');
            $table->foreign('pedido_id')->references('id')->on('pedidos')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('detalles_pedidos');
    }
};
