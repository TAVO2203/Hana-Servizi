<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HanaServizi</title>
    <link rel="icon" type="image" href="{{ asset('fondos/icon.png') }}">
    <link rel="stylesheet" href="{{ asset('CSS/create.css') }}">
    <link rel="icon" type="image" href="{{ asset('fondos/icon.png') }}">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

</head>
<body>
<header class="header">
    <div class="header_container">
        <div class="logo">
            <img src="{{ asset('fondos/icon.png') }}" alt="Logo De HanaServizi" class="logo-img">
            <h2 class="name-project"> HanaServizi</h2>
        </div>
        <nav class="nav-menu">
            <a href="index.html"> Inicio </a>
            <a href="#"><i class="fa-solid fa-user"></i></a>
            <a href="#"><i class="fa-solid fa-bell"></i></a>
            <a href="#"><i class="fa-solid fa-cart-shopping"></i></a>
            <div class="language">
                <div class="language-selected">
                    ESP
                </div>
                <ul>
                    <li><a href="#" class="Esp"> Español </a></li>
                    <li><a href="#" class="Eng"> English </a></li>
                </ul>
            </div>
        </nav>
    </div>
</header>
<section>
    <div class="container">
        <h1>Editar Producto</h1>
        <form action="{{ route('Productos.update', $producto->id) }}" method="POST" enctype="multipart/form-data">
            @csrf
            @method('PUT')
            <div class="mb-3">
                <label for="Nombre" class="form-label">Nombre</label>
                <input type="text" class="form-control" id="Nombre" name="Nombre" required value="{{ $producto->Nombre }}">
            </div>
            <div class="mb-3">
                <label for="Precio" class="form-label">Precio</label>
                <input type="number" class="form-control" id="Precio" name="Precio" required value="{{ $producto->Precio }}">
            </div>
            <div class="mb-3">
                <label for="Fecha_agregacion" class="form-label">Fecha de Agregación</label>
                <input type="date" class="form-control" id="Fecha_agregacion" name="Fecha_agregacion" required value="{{ $producto->Fecha_agregacion }}">
            </div>
            <div class="mb-3">
                <label for="EstadoProducto" class="form-label">Estado del Producto</label>
                <select class="form-select" id="EstadoProducto" name="EstadoProducto" required>
                    <option value="">Seleccione un estado</option>
                    <option value="Disponible" {{ $producto->EstadoProducto == 'Disponible' ? 'selected' : '' }}>Disponible</option>
                    <option value="No disponible" {{ $producto->EstadoProducto == 'No disponible' ? 'selected' : '' }}>No disponible</option>
                </select>

            </div>
            <div class="mb-3">
                <label for="Descripcion" class="form-label">Descripción</label>
                <textarea class="form-control" id="Descripcion" name="Descripcion">{{ $producto->Descripcion }}</textarea>

            </div>
            <div class="mb-3">
                <label for="idCategoria" class="form-label">ID de Categoría</label>
                <input type="number" class="form-control" id="idCategoria" name="idCategoria" value="{{ $producto->idCategoria }}">
            </div>
            <div class="mb-3">
                <label for="idMarca" class="form-label">ID de Marca</label>
                <input type="number" class="form-control" id="idMarca" name="idMarca" value="{{ $producto->idMarca }}">
            </div>
            @if ($producto->imagen)
                <p>Imagen actual:</p>
                <img src="{{ asset('storage/' . $producto->imagen) }}" alt="Imagen del producto" class="img-fluid mb-3" width="100">
            @endif

            <div class="mb-3">
                <label for="imagen" class="form-label">Carga una nueva imagen</label>
                <input class="form-control" type="file" id="imagen" name="imagen">
            </div>

            <button type="submit" class="btn btn-primary">Guardar Producto</button>
            <a href="javascript:history.back()" class="btn btn-success w-100 m-1" >Volver</a>
        </form>
    </div>
</section>
<footer>
    <div class="footer-col">
        <h4>Nuestras Redes</h4>
        <div class="links">
            <a href="#"><i class="fa-brands fa-facebook"></i></a>
            <a href="#"><i class="fa-brands fa-instagram"></i></a>
            <a href="#"><i class="fa-brands fa-tiktok"></i></a>
        </div>
    </div>
    <div class="footer-col">
        <h4> Acerca De Hana Servizi</h4>
        <ul>
            <li> <a href="#">Sobre Nosotros</a></li>
            <li> <a href="#">Nuestras Políticas</a></li>
            <li> <a href="#">Términos De Servicio</a></li>
            <li> <a href="#">Políticas De Privacidad</a></li>
            <li> <a href="#">Políticas De Reembolsos y Devoluciones</a></li>
        </ul>
    </div>
    <div class="footer-col">
        <h4> Contacto </h4>
        <ul class="info">
            <li>
                <span><i class="fa-solid fa-phone"></i></span><p>+57 123 4567 890</p>
            </li>
            <li>
                <span><i class="fa-solid fa-envelope"></i></span><p>hanaservizi@gmail.com</p>
            </li>
        </ul>
    </div>
    <div class="footer-col">
        <h4> Métodos De Pago</h4>
        <ul>
            <img src="{{ asset('images/pay.png') }}" alt="">
            <img class="img3" src="{{ asset('images/Nequi.webp') }}" alt="">
        </ul>
    </div>
</footer>
<div class="copyright">
    <p>©2024 HanaServizi. ALL Rights Reserved.</p>
</div>

</body>
</html>