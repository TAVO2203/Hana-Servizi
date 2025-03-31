<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\Proveedor;
use Illuminate\Support\Facades\DB;


class ProveedorController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index(Request $request)
    {
        $pro = trim($request->get('pro'));
        $proveedor = DB::table('proveedor')
        ->select('id', 'Nombre', 'Email', 'Telefono','Ciudad', 'Direccion',  'idInventario')
        ->where('Nombre', 'LIKE', '%'. $pro .'%')
        ->orderBy('Nombre', 'asc')
        ->paginate(5);
        
        return view('Proveedores.index', compact('proveedor', 'pro'));
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        return view('Proveedores.create');
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $request->validate([
            'Nombre' => 'required|string|max:255',
            'Email' => 'required|String|max:255',
            'Telefono' => 'required|numeric',
            'Ciudad' => 'required|string|max:255',
            'Direccion' => 'required|string|max:255',
            'idInventario' => 'required|integer',
        ]);
        $proveedor = new Proveedor();
        $proveedor->Nombre = $request-> Nombre;
        $proveedor->Email = $request-> Email;
        $proveedor->Telefono = $request-> Telefono;
        $proveedor->Ciudad = $request-> Ciudad;
        $proveedor->Direccion = $request-> Direccion;
        $proveedor->idInventario = $request-> idInventario;

        $proveedor->save();

        return redirect()->route('Proveedores.index');
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
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, string $id)
    {
        $proveedor = Proveedor::findOrFail($id);
        $proveedor->Nombre = $request->input('Nombre');
        $proveedor->Email = $request->input('Email');
        $proveedor->Telefono = $request->input('Telefono');
        $proveedor->Ciudad = $request->input('Ciudad');
        $proveedor->Direccion = $request->input('Direccion');
        $proveedor->idInventario = $request->input('idInventario');

        $proveedor->save();

        return redirect()->route('Proveedores.index');
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        $proveedor = Proveedor::findOrFail($id);

        $proveedor->delete();
    
        return redirect()->route('Proveedores.index');
    }
}
