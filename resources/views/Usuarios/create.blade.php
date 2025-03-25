<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hana Servizi</title>
  <link href="{{ asset('CSS/register.css') }}" rel="stylesheet">
  <link rel="icon" type="image" href="{{ asset('fondos/icon.png') }}">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>
<body>
<header class="header">
  <div class="header_container">
    <div class="bars">
      <div class="line"></div>
      <div class="line"></div>
      <div class="line"></div>
    </div>
    <div class="logo">
      <img src="{{ asset('fondos/icon.png') }}" alt="Logo De HanaServizi" class="logo-img">
      <h2 class="name-project"> HanaServizi</h2>
    </div>
    <nav class="nav-menu">
      <a href="index.html" class="active"> Inicio </a>
      <a href="#"><i class="fa-solid fa-user"></i></a>
      <a href="#"><i class="fa-solid fa-bell"></i></a>
      <a href="#"><i class="fa-solid fa-cart-shopping"></i></a>
      <a href="IniciarSesión.html"><button class="btn6"> Iniciar Sesión </button></a>
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

  <!-- Modal de inicio de sesión -->
  <div class="icon">
    <img src="{{ asset('fondos/icon.png') }}" alt="Logo De HanaServizi">
    <h1> HanaServizi</h1>
  </div>
<!-- Contenedor del formulario -->
<div class="form-container">
  <h2>Registro</h2>
  <form action="{{route('Usuarios.store')}}" method="POST">
    @csrf
    <label for="nombre">Nombre Completo</label>
    <input type="text" id="nombre" name="Nombre" placeholder="Ingresa tu nombre" required>

    <label for="email">E-mail</label>
    <input type="email" id="email" name="Email" placeholder="Ingresa tu correo" required>

    <label for="direccion">Dirección</label>
    <input type="text" id="direccion" name="Direccion" placeholder="Ingresa tu dirección" required>

    <label for="telefono">Teléfono</label>
    <input type="tel" id="telefono" name="Telefono" placeholder="Ingresa tu número de teléfono" required>

    <label for="password">Contraseña</label>
    <input type="password" id="password" name="Contraseña" placeholder="Crea una contraseña" required>

    <label for="confirm-password">Confirmar Contraseña</label>
    <input type="password" id="confirm-password" name="Confirm-password" placeholder="Confirma tu contraseña" required>
    <button class="btn9" id="register" type="submit">Registrarse</button>
    <a href="javascript:history.back()" class="btn_btn-success">Volver</a>
    <br>
  </form>
  <p class="terms">
    Al registrarte, aceptas nuestros <a id="open-modal">Términos y Condiciones</a>.
  </p>
</div>
  <!-- Modal de Términos y Condiciones -->
  <div id="modal" class="modal">
    <div class="modal-content">
      <h3>Términos y Condiciones</h3>
      <p>
        Bienvenido a Hana Servizi. Al utilizar nuestra plataforma, aceptas que eres mayor de edad y que los datos que proporciones serán utilizados únicamente para procesar pedidos y mejorar tu experiencia como usuario. Nos reservamos el derecho de actualizar nuestras políticas en cualquier momento. Recuerda que está prohibido el uso de la plataforma para fines ilegales o fraudulentos.
      </p>
      <p>
        Todos los productos ofrecidos están sujetos a disponibilidad. Hana Servizi no se hace responsable por demoras ocasionadas por terceros en el proceso de envío. Por favor, consulta nuestras políticas de devoluciones antes de realizar una compra.
      </p>
      <button class="close-modal">Cerrar</button>
    </div>
  </div>
<script src="{{ asset('JS/validation.js') }}"></script>
<script src="{{ asset('JS/modal.js') }}"></script>

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