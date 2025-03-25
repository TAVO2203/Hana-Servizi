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
        Schema::create('marca', function (Blueprint $table) {
            $table->increments('id');
            $table->string('NombreMarca', 50);
            $table->date('Fecha_agregacion');
            $table->string('pais_marca');
            $table->string('Descripcion', 255);
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('marca');
    }
};
