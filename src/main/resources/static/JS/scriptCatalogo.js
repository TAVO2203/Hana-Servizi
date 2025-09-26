let cart = [];
let selectedProduct = null; // Producto que abre el modal

// Función para abrir modal con tallas
function openTallaModal(product) {
    selectedProduct = product;
    const modal = document.getElementById("tallaModal");
    const select = document.getElementById("modalTallaSelect");
    select.innerHTML = '<option value="">Selecciona una talla</option>';

    if (product.tallas) {
        product.tallas.split(",").forEach(t => {
            const [talla, stock] = t.split(":");
            if (parseInt(stock) > 0) {
                const option = document.createElement("option");
                option.value = talla.trim();
                option.textContent = `${talla.trim()} (Stock: ${stock})`;
                select.appendChild(option);
            }
        });
    }


    modal.style.display = "flex";
}

// Función para agregar productos al carrito
function addToCart(id, name, price, talla, imagen) {
    const existingItem = cart.find(item => item.id === id && item.talla === talla);

    if (existingItem) {
        existingItem.cantidad += 1;
    } else {
        cart.push({ id, name, price, talla, imagen, cantidad: 1 });
    }

    renderCart();
    sessionStorage.setItem('carrito', JSON.stringify(cart));

    // 👇 ahora sí mandamos todos los params
    fetch(`/carrito/agregar?id=${id}&nombre=${encodeURIComponent(name)}&precio=${price}&imagen=${encodeURIComponent(imagen)}&cantidad=1${talla ? `&talla=${talla}` : ""}`, {
        method: 'GET'
    }).catch(error => console.error('Error:', error));
}


// Renderizar carrito
function renderCart() {
    const items = document.getElementById("cart-items");
    items.innerHTML = "";
    let total = 0;

    cart.forEach((item, index) => {
        let li = document.createElement("li");
        li.className = "list-group-item";
        li.innerHTML = `
                ${item.name} ${item.talla ? `(Talla: ${item.talla})` : ""}
                <span>$${item.price} x ${item.cantidad}</span>
                <button class="remove-btn" onclick="removeFromCart(${index})">❌</button>
            `;


        items.appendChild(li);
        total += item.price * item.cantidad;
    });

    document.getElementById("cart-total").textContent = total.toFixed(2);
    document.getElementById("cart").style.display = cart.length > 0 ? "block" : "none";
}

function removeFromCart(index) {
    cart.splice(index, 1);
    renderCart();
    sessionStorage.setItem('carrito', JSON.stringify(cart));
}

function finalizarCompra() {
    if (cart.length === 0) {
        alert('El carrito está vacío');
        return;
    }
    window.location.href = '/carrito';
    cart = [];
    sessionStorage.removeItem('carrito');
    renderCart();
}

// Listeners
// Listener para botones "Agregar al carrito"
document.querySelectorAll(".btn-add-cart").forEach(btn => {
    btn.addEventListener("click", (e) => {
        e.preventDefault(); // evita refresco

        const product = {
            id: btn.getAttribute("data-id"),
            nombre: btn.getAttribute("data-nombre"),
            precio: parseFloat(btn.getAttribute("data-precio")),
            tallas: btn.getAttribute("data-tallas"),
            categoria: btn.getAttribute("data-categoria"),
            imagen: btn.getAttribute("data-imagen")
        };

        // Si la categoría es "Moda", abrir modal
        if (product.categoria === "Moda") {
            openTallaModal(product);
        } else {
            // Si no es Moda, agregar directo
            addToCart(product.id, product.nombre, product.precio, null, product.imagen);

        }
    });
});


document.getElementById("confirmarTalla").addEventListener("click", () => {
    const select = document.getElementById("modalTallaSelect");
    const talla = select.value;
    if (!talla) {
        alert("Por favor selecciona una talla");
        return;
    }
    addToCart(selectedProduct.id, selectedProduct.nombre, selectedProduct.precio, talla, selectedProduct.imagen);

    document.getElementById("tallaModal").style.display = "none";
});

document.querySelector(".close").onclick = () => {
    document.getElementById("tallaModal").style.display = "none";
};

window.onclick = (e) => {
    if (e.target === document.getElementById("tallaModal")) {
        document.getElementById("tallaModal").style.display = "none";
    }
};

// Restaurar carrito desde sessionStorage
document.addEventListener('DOMContentLoaded', () => {
    const carritoGuardado = sessionStorage.getItem('carrito');
    if (carritoGuardado) {
        cart = JSON.parse(carritoGuardado);
        renderCart();
    }
});