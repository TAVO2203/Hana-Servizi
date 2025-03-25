<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\Usuarios;
use Illuminate\Support\Facades\DB;

class UsuariosController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index(Request $request)
    {
        $lista = trim($request->get('lista'));
        $Usuarios = DB::table('usuarios')
        ->select('id', 'Nombre', 'Email','Contraseña', 'Telefono', 'Direccion')
        ->where('Nombre', 'LIKE', '%'. $lista .'%')
        ->orderBy('Nombre', 'asc')
        ->paginate(5);
        
        return view('Usuarios.index', compact('Usuarios', 'lista'));
        
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        return view('Usuarios.create');
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $Usuarios = new Usuarios();
        $Usuarios->Nombre = $request->input('Nombre');
        $Usuarios->Email = $request->input('Email');
        $Usuarios->Contraseña = $request->input('Contraseña');
        $Usuarios->Telefono = $request->input('Telefono');
        $Usuarios->Direccion = $request->input('Direccion');
        $Usuarios->save();
        return redirect()->route('home');
    }

    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(string $id)
    {
        $usuario = Usuarios::findOrFail($id);
        return view('Usuarios.edit', compact('usuario'));
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, string $id)
    {
        $usuario = Usuarios::findOrFail($id);
        $usuario->Nombre = $request->input('Nombre');
        $usuario->Email = $request->input('Email');
        $usuario->Telefono = $request->input('Telefono');
        $usuario->Direccion = $request->input('Direccion');
        $usuario->save();
        return redirect()->route('Usuarios.index');
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        $usuario = Usuarios::findOrFail($id);
        $usuario->delete();
        return redirect()->route('Usuarios.index');
    }
}
