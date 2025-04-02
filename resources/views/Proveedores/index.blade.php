<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Proveedores</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <h1>Gestión de Proveedores</h1>
    <div class="row">
        <div class="col-xl-12">
            <form action="{{route('Proveedores.index')}}" method="GET">
                <div class="d-flex align-items-center gap-2">
                    <input type="text" class="form-control" name="pro" value="{{$pro}}" placeholder="Buscar Proveedor">
                    <input type="submit" class="btn btn-primary" value="Buscar">
                    <a href="{{route('Proveedores.create')}}" class="btn btn-success">Nuevo</a>
                </div>
            </form>
        </div>
        <div class="col-xl-12">
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Opciones</th>
                            <th>Nombre</th>
                            <th>Email</th>
                            <th>Telefono</th>
                            <th>Cuidad</th>
                            <th>Direccion</th>
                        </tr>
                    </thead>
                    <tbody>
                        @if(count($proveedor)<=0)
                            <tr>
                                <td colspan="5">No hay resultados</td>
                            </tr>
                        @else
                        @foreach($proveedor as $pro)
                                <tr>
                                    <td><a href="{{route('Proveedores.edit', $pro->id)}}" class="btn btn-warning btn-sm">Editar</a>
                                        <button type="button" class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#modal-delete-{{$pro->id}}">
                                            Eliminar
                                        </button>
                                    </td>
                                    <td>{{ $pro->Nombre }}</td>
                                    <td>{{ $pro->Email }}</td>
                                    <td>{{ $pro->Telefono }}</td>
                                    <td>{{ $pro->Ciudad }}</td>
                                    <td>{{ $pro->Direccion }}</td>
                                </tr>
                                @include('Proveedores.delete')
                            @endforeach
                        @endif
                    </tbody>
                </table> 
                <div class="mt-4">
                    {{ $proveedor->links() }}
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy" crossorigin="anonymous"></script>


</html>