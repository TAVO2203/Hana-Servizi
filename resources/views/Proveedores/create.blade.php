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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

</head>
<body>
<header class="header">
<style>
*{
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    line-height: 1.2;
}

body{
    font-family: 'Poppins', monospace;
    background-color: #E3F2FD;
    height: 180vh;
    display: flex;
    flex-direction: column;
}

a{
    text-decoration: none;
}
.header{
    background-color: #ff0000;
    height: 105px;
    padding: 20px 0;
    box-shadow: 0 5px 15px rgba(0,0,0,0.06);
}

.header_container{
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.bars{
    display: none;
}

.nav-menu a .active{
    color: #1a1a1a;
    background-color: #FFFFFF;
}
.logo{
    display: flex;
    align-items: center;
}
.logo img{
    height: 70px;
    margin-left: 10px;
}
.name-project{
    color: #FFFFFF;
}
.nav-menu{
    display: flex;
    align-items: center;
}

.nav-menu > a{
    color: #FFFFFF;
    font-size: 1em;
    text-transform: uppercase;
    padding: 15px;
}

button{
    background-color: #FFFFFF;
    color: #ff0000;
    text-transform: uppercase;
    position: relative;
    border: none;
    height: 40px;
    width: 100px;
    transition: .3s linear;
    cursor: pointer;
}

.btn6:hover{
    background-color: #FFFFFF;
    box-shadow: 0 0 5px #FFFFFF, 0 0 25px #FFFFFF,
    0 0 50px #FFFFFF, 0 0 100px #FFFFFF, 0 0 300px #FFFFFF;
}

.language{
    position: relative;
    width: 120px;
    margin-left: 40px;
    font-weight: bold;
}

.language-selected{
    display: flex;
    align-items: center;
    color: #FFFFFF;
    cursor: pointer;
}

.language-selected:before{
    content: "";
    display: inline-block;
    width: 30px;
    height: 30px;
    background-image: url(https://flagsapi.com/ES/flat/32.png);
    background-size: contain;
    background-repeat: no-repeat;
    margin-right: 10px;
}

.language ul{
    position: relative;
    width: 140px;
    background-color: #f8f8f8;
    border: 1px solid #f8f8f8;
    padding-top: 10px;
    box-shadow: 0 1px 10px rgba(0,0,0,0.5);
    display: none;
    list-style-type: none;
}

.language ul li{
    list-style: none;
}

.language ul li a{
    display: block;
    color: #252525;
    padding-top: 5px ;
}

.language ul li:hover{
    background-color: #FF0000;
}

.language ul li a:before{
    content: "";
    display: inline-block;
    width: 25px;
    height: 25px;
    background-size: contain;
    background-repeat: no-repeat;
    vertical-align: middle;
    margin-right: 10px;

}

a.Esp:before{
    background-image: url(https://flagsapi.com/ES/flat/32.png);
}


a.Eng:before{
    background-image: url(https://flagsapi.com/US/flat/32.png);
}

.language:hover ul{
    display: block;
}
footer{
    display: flex;
    flex-wrap: wrap;
    margin-top: auto;
    background-color: #000000;
    padding: 25px 0;

}

ul{
    list-style: none;
}

.footer-col {
    width: 25%;
    padding: 0 15px;
}

.footer-col h4{
    position: relative;
    margin-bottom: 35px;
    font-weight: 500;
    font-size: 22px;
    color: #FFFFFF;
    text-transform: capitalize;
}

.footer-col h4:before{
    content: "";
    position: absolute;
    left: 0;
    bottom: -6px;
    background-color: #FFFFFF;
    height: 2px;
    width: 40px;
}

ul li:not(:last-child){
    margin-bottom: 10px;
}

ul li a{
    display: block;
    font-size: 16px;
    text-transform: capitalize;
    color: white;
    text-decoration: none;
    transition: all 0.4s ease;
}

ul li a:hover{
    color: white;
    padding-left: 8px ;
}

.links a{
    display: inline-block;
    height: 40px;
    width: 40px;
    color: #f8f8f8;
    background-color: rgba(40, 130, 214, 0.8);
    margin: 0 8pc 8px 0;
    text-align: center;
    line-height: 40px;
    border-radius: 50%;
    transition: all 0.4s ease;
}

.links a:hover{
    color: #000000;
    background-color: #f8f8f8;
}

footer .sec-contact .info{
    position: relative;
}

.footer-col .info li{
    display: grid;
    grid-template-columns: 30px 1fr;
    margin-bottom: 16px;
}

.footer-col .info li span{
    color: #FFFFFF;
    font-size: 20px;
}
.footer-col p{
    color: #FFFFFF;
}

.footer-col .info li a{
    color: #FFFFFF;
    text-decoration: none;
}

.img3{
    position: relative;
    width: 100px;
    margin-top: -10px;
    right: 25px;
}

.copyright{
    width: 100%;
    background: #000000;
    padding: 10px 100px 20px;
    text-align: center;
    color: #f8f8f8;
    border: 1px solid rgba(0,0,0,0.15);
}
</style>

    <div class="header_container">
        <div class="logo">
            <img src="{{ asset('fondos/icon.png') }}" alt="Logo De HanaServizi" class="logo-img">
            <h2 class="name-project"> HanaServizi</h2>
        </div>
        <nav class="nav-menu">
            <a href="{{route('home')}}"> Inicio </a>
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
        <h1>Agregar Proveedor</h1>
        <form action="{{ route('Proveedores.store') }}" method="POST">
            @csrf
            <div class="mb-3">
                <label for="Nombre" class="form-label">Nombre</label>
                <input type="text" class="form-control" id="Nombre" name="Nombre" required>
            </div>
            <div class="mb-3">
                <label for="Email" class="form-label">Email</label>
                <input type="email" class="form-control" id="Email" name="Email" required>
            </div>
            <div class="mb-3">
                <label for="Telefono" class="form-label">Telefono</label>
                <input type="number" class="form-control" id="Telefono" name="Telefono" required>
            </div>
            <div class="mb-3">
                <label for="Ciudad" class="form-label">Ciudad</label>
                <input class="form-select" id="Ciudad" name="Ciudad" required>
            </div>
            <div class="mb-3">
                <label for="Direccion" class="form-label">Direccion</label>
                <input class="form-control" id="Direccion" name="Direccion"></input>
            </div>
            <button type="submit" class="btn btn-primary">Agregar</button>
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