<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Project HanaServizi</title>
  <link rel="stylesheet" href="{{ asset('CSS/style.css') }}">
  <link rel="icon" type="image" href="fondos/icon.png">
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
      <img src="fondos/icon.png" alt="Logo De HanaServizi" class="logo-img">
      <h2 class="name-project"> HanaServizi</h2>
    </div>
    <nav class="nav-menu">
      <a href="#" class="active"><strong>Inicio</strong> </a>
      <a href="error500.html"><i class="fa-solid fa-user"></i></a>
      <a href="#"><i class="fa-solid fa-bell"></i></a>
      <a href="#"><i class="fa-solid fa-cart-shopping"></i></a>
      <a href="{{ route('login') }}"><button class="btn6"><strong>Iniciar Sesión</strong></button></a>
      <a href="{{ route('Usuarios.create') }}"><button class="btn6"><strong> Registrarse</strong></button></a>
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



<div class="container">
  <input type="text" placeholder="Buscar Productos">
  <button class="btn1"><i class="fa-solid fa-magnifying-glass"></i></button>
</div>


<div id="hero2">
  <h2> Aquí Encontrarás</h2>
  <h1> Grandes Ofertas</h1>
  <p> Además de múltiples productos</p>
  <a class="btn-2"> Descubrelo ahora</a>
</div>
<!--contenido-->

<div class="select-menu">
  <div class="select-btn">
    <span class=" sBtn-text">Categorías</span>
    <i class="fa-solid fa-chevron-down"></i>
  </div>
  <ul class="options">
    <li class="option">
      <i class="fa-solid fa-laptop"></i>
      <span class="option-text"> Tecnología</span>
    </li>
    <li class="option">
      <i class="fa-solid fa-shirt"></i>
      <span class="option-text"> Moda</span>
    </li>
    <li class="option">
      <i class="fa-solid fa-couch"></i>
      <span class="option-text"> Muebles</span>
    </li>
    <li class="option">
      <i class="fa-solid fa-hat-wizard"></i>
      <span class="option-text"> Accesorios</span>
    </li>
  </ul>
</div>
<script src="{{ asset('JS/script.js') }}"></script>
<div class="linea-vertical"></div>
<div class="service-user">
  <a href="#" class="btn-3"> Servicio Al Cliente</a>
  <a href="#" class="btn-4"> Tarjetas De Regalo</a>
</div>
<div class="linea-vertical2"></div>

<!-- barra lateral -->
<div class="sidebar">
  <div class="ofertas">
    <img src="fondos/fondoofertas.png" alt="">
    <p><strong> ¡Ofertas Del Día De Hoy!</strong></p>
  </div>
  <div class="ofertas2">
    <p> Por Tu Primera Compra</p>
    <img src="fondos/fondotecno.jpg" alt="">
  </div>
  <div class="down">
    <img src="images/flechabajo.png" alt="">
    <h2>Obtendrás 10.000 cop de</h2>
    <p><strong> DTO</strong></p>
    <p><strong> + </strong></p>
    <p><strong> Envió Gratis</strong></p>
    <img src="images/fuego.png" class="img2" alt="">
  </div>
  <div class="mas-vendidos">
    <img src="fondos/fondoe-co.jpg" alt="">
    <p> Los Más Vendidos</p>
  </div>
</div>
<!-- end sidebar-->
<!-- star container products-->
<section id="product-1">
  <div class="container2">
    <div class="pro">
      <img src="images/audif.png" alt="">
      <div class="content-container">
        <span> JOYup</span>
        <h5> Audífonos Estéreo Inalámbricos</h5>
        <div class="star">
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
        </div>
        <h4> COP 7.890</h4>
      </div>
      <div class="shop">
      <a href="#"><i class="fa-solid fa-cart-shopping"></i></a>
      </div>
    </div>
    <div class="pro">
      <img src="images/llaverothor.png" alt="">
      <div class="content-container">
        <span> SZ HCHAO</span>
        <h5> LLavéro De Thor</h5>
        <div class="star">
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
        </div>
        <h4> COP 11.000</h4>
      </div>
      <div class="shop">
        <a href="#"><i class="fa-solid fa-cart-shopping"></i></a>
      </div>
    </div>
    <div class="pro">
      <img src="images/relojpulsera.png" alt="">
      <div class="content-container">
        <span> HSTKJ</span>
        <h5> Reloj Inteligente De Pulsera</h5>
        <div class="star">
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
        </div>
        <h4> COP 32.690</h4>
      </div>
      <div class="shop">
        <a href="#"><i class="fa-solid fa-cart-shopping"></i></a>
      </div>
    </div>
    <div class="pro">
      <img src="images/gafasniño.png" alt="">
      <div class="content-container">
        <span> Siyang Children Optical Shop</span>
        <h5> Gafas De Sol Para Niño</h5>
        <div class="star">
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
        </div>
        <h4> COP 7.890</h4>
      </div>
      <div class="shop">
        <a href="#"><i class="fa-solid fa-cart-shopping"></i></a>
      </div>
    </div>
    <div class="pro">
      <img src="images/pantaloncargo.png" alt="">
      <div class="content-container">
        <span> Jack Fairwhale</span>
        <h5> Pantalón Cargo Para Hombre</h5>
        <div class="star">
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
        </div>
        <h4> COP 92.000</h4>
      </div>
      <div class="shop">
        <a href="#"><i class="fa-solid fa-cart-shopping"></i></a>
      </div>
    </div>
    <div class="pro">
      <img src="images/lucesled.png" alt="">
      <div class="content-container">
        <span> LBWYYDS</span>
        <h5> Luces Led Para Habitación</h5>
        <div class="star">
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
        </div>
        <h4> COP 19.900</h4>
      </div>
      <div class="shop">
        <a href="#"><i class="fa-solid fa-cart-shopping"></i></a>
      </div>
    </div>
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


