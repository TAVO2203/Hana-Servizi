<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link href="{{ asset('CSS/Dash.css') }}" rel="stylesheet">
  <link rel="icon" type="image" href="fondos/icon.png">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>
<body>
<header class="header">
  <div class="header_container">
    <div class="logo">
      <img src="fondos/icon.png" alt="Logo De HanaServizi" class="logo-img">
      <h2 class="name-project"> HanaServizi</h2>
    </div>
    <nav class="nav-menu">
      <a href="{{ route('home') }}"> Inicio </a>
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
<!-- end header-->
<!--start dashboard-->
<!-- inicia el dashboard-->
  <!--barra lateral-->
  <div class="sidebar">
    <h2> HanaServizi</h2>
    <ul>
      <li>
        <a href="index.html">
          <i class="fa-solid fa-house"></i>
          <div> Inicio</div>
        </a>
      </li>
      <li>
        <a href="{{ route('Usuarios.create') }}">
          <i class="fa-solid fa-user"></i>
          <div> Clientes </div>
        </a>
      </li>
      <li>
        <a href="{{ route('Proveedores.create') }}">
          <i class="fa-solid fa-truck-field"></i>
          <div>Proveedores</div>
        </a>
      </li>
      <li>
        <a href="#">
          <i class="fa-solid fa-box"></i>
          <div> Pedidos </div>
        </a>
      </li>
      <li>
        <a href="{{ route('Productos.create') }}">
          <i class="fa-solid fa-tag"></i>
          <div> Productos</div>
        </a>
      </li>
      <li>
        <a href="#">
          <i class="fa-solid fa-dollar-sign"></i>
          <div> Ventas</div>
        </a>
      </li>
      <li>
        <a href="#">
          <i class="fa-solid fa-question"></i>
          <div> Ayuda </div>
        </a>
      </li>
    </ul>
  </div>
  <!-- contenido principal-->
  <div class="main">
    <div class="cards">
      <div class="card">
         <div class="card-content">
           <div class="number"> 5.000</div>
           <div class="card-name"> Usuarios Nuevos </div>
         </div>
         <div class="icon-box">
           <i class="fa-solid fa-user"></i>
         </div>
      </div>
      <div class="card">
        <div class="card-content">
          <div class="number"> 20.000</div>
          <div class="card-name"> Ventas Exitosas </div>
        </div>
        <div class="icon-box">
          <i class="fa-solid fa-dollar-sign"></i>
        </div>
      </div>
      <div class="card">
        <div class="card-content">
          <div class="number"> 80%</div>
          <div class="card-name"> Productos de tecnología vendidos</div>
        </div>
        <div class="icon-box">
          <i class="fa-solid fa-tv"></i>
        </div>
      </div>
      <div class="card">
        <div class="card-content">
          <div class="number"> 12.000.000</div>
          <div class="card-name"> Ingresos del día de hoy</div>
        </div>
        <div class="icon-box">
          <i class="fa-solid fa-sack-dollar"></i>
        </div>
      </div>
    </div>
    <div class="charts">
      <div class="chart">
        <h2> Vista de ventas de los meses más vendidos</h2>
        <canvas id="LineChart" height="130px"></canvas>
      </div>
    </div>
  </div>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="JS/main.js"></script>



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
      <img src="images/pay.png" alt="">
      <img class="img3" src="images/Nequi.webp" alt="">
    </ul>
  </div>
</footer>
<div class="copyright">
  <p>©2024 HanaServizi. ALL Rights Reserved.</p>
</div>

</body>
</html>