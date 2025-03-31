<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use App\Models\Productos;
use Illuminate\Support\Facades\Storage;


class ProductosController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index(Request $request){
        $prt = trim($request->get('prt'));
        $producto = DB::table('productos')
        ->select('id', 'Nombre', 'Precio','Fecha_agregacion', 'EstadoProducto', 'Descripcion', 'idCategoria', 'idMarca', 'imagen')
        ->where('Nombre', 'LIKE', '%'. $prt .'%')
        ->orderBy('Nombre', 'asc')
        ->paginate(5);
        
        return view('Productos.index', compact('producto', 'prt'));
    }
    

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        return view('Productos.create');
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $request->validate([
            'Nombre' => 'required|string|max:255',
            'Precio' => 'required|numeric',
            'Fecha_agregacion' => 'required|date',
            'EstadoProducto' => 'required|string|max:255',
            'Descripcion' => 'required|string|max:255',
            'idCategoria' => 'required|integer',
            'idMarca' => 'required|integer',
            'imagen' => 'nullable|image|mimes:jpeg,png,jpg,gif|max:2048',
        ]);
        $producto = new Productos();
        $producto->Nombre = $request-> Nombre;
        $producto->Precio = $request-> Precio;
        $producto->Fecha_agregacion = $request-> Fecha_agregacion;
        $producto->EstadoProducto = $request-> EstadoProducto;
        $producto->Descripcion = $request-> Descripcion;
        $producto->idCategoria = $request-> idCategoria;
        $producto->idMarca = $request-> idMarca;

        if ($request->hasFile('imagen')) {
            $path = $request->file('imagen')->store('images', 'public');
            $producto->imagen = $path;
        }
        $producto->save();

        return redirect()->route('Productos.index');
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
        $producto = Productos::findOrFail($id);
        return view('Productos.edit', compact('producto'));
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, string $id)
    {
        $producto = Producto::findOrFail($id);
        $producto->Nombre = $request->input('Nombre');
        $producto->Precio = $request->input('Precio');
        $producto->Fecha_agregacion = $request->input('Fecha_agregacion');
        $producto->EstadoProducto = $request->input('EstadoProducto');
        $producto->Descripcion = $request->input('Descripcion');
        $producto->idCategoria = $request->input('idCategoria');
        $producto->idMarca = $request->input('idMarca');
        
        if ($request->hasFile('imagen')) {
            $path = $request->file('imagen')->store('images', 'public');
            $producto->imagen = $path;
        }
        $producto->save();

        return redirect()->route('Productos.index');
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        $producto = Productos::findOrFail($id);

        if ($producto->imagen) {
            Storage::disk('public')->delete($producto->imagen);
        }
        $producto->delete();
    
        return redirect()->route('Productos.index');
    }
}
