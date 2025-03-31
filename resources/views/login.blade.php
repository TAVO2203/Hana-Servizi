<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HanaServizi</title>
    <link rel="icon" type="image" href="fondos/icon.png">
    <link rel="stylesheet" href="{{ asset('CSS/Login.css') }}">
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

<div class="container3">
    <div class="icon">
        <img src="fondos/icon.png">
        <h1> HanaServizi</h1>
    </div>
    <div class="form-container">
        <h2> Inicia Sesión</h2>
        <form>
            <div class="input-group">
               <div class="input-field" id="nameInput">
                   <i class="fa-solid fa-user"></i>
                 <input type="text" placeholder="Nombre" required>
               </div>
              <div class="input-field">
                  <i class="fa-solid fa-envelope"></i>
                 <input type="email"placeholder="Correo Electrónico" required>
              </div>
              <div class="input-field">
                  <i class="fa-solid fa-lock"></i>
                 <input type="password" placeholder="Contraseña" required >
              </div>
              <p>¿Olvidaste Tu Contraseña?<a class="olviddaste" href="#">Click Aquí </a></p>
              <div class="btn-field">
                  <button id="SignIn" type="button">Iniciar Sesión </button>
              </div>
                <div class="new-account">
                    <p> Eres nuevo en HanaServizi?<a class="link" href="{{route('Usuarios.create')}}">Registrarme</a></p>
                </div>
            </div>
            <div class="line-width-text">
                <span> O Iniciar sesión con</span>
            </div>
            <div class="other-login">
                <a href="#" class="google">
                    <img src="images/google.png">
                    <span> Google </span>
                </a>
                <a href="#" class="facebook">
                  <img src="images/facebook.png">
                  <span> Facebook </span>
                </a>
            </div>
            <a class="nav-link" href="{{ route('dashboard') }}">OPCIONAL</a>
        </form>
    </div>
</div>

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
<script> src="{{ asset('JS/validation.js') }}"</script>

</body>
</html>