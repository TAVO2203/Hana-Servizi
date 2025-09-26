    let productoSeleccionado = null;

    function openTallaModal(producto) {
    productoSeleccionado = producto;
    const modal = document.getElementById("tallaModal");
    const select = document.getElementById("tallaSelect");
    const img = document.getElementById("modalProductoImagen");

    // resetear select
    select.innerHTML = '<option value="">Selecciona una talla</option>';

    // cargar tallas dinámicas si existen
        if (producto.tallas) {
            try {
                let tallas = {};

                // Si viene en formato JSON válido
                if (producto.tallas.trim().startsWith("{")) {
                    tallas = JSON.parse(producto.tallas);
                } else {
                    // Si viene como "S:10;M:15;L:5"
                    producto.tallas.split(";").forEach(par => {
                        if (par.trim()) {
                            const [talla, stock] = par.split(":");
                            tallas[talla.trim()] = parseInt(stock.trim());
                        }
                    });
                }

                // Cargar en el select
                for (const [talla, stock] of Object.entries(tallas)) {
                    if (stock > 0) {
                        const option = document.createElement("option");
                        option.value = talla;
                        option.textContent = `${talla} (Stock: ${stock})`;
                        select.appendChild(option);
                    }
                }
            } catch (e) {
                console.error("Error cargando tallas", e);
            }
        }


        // cargar imagen
    img.src = "/uploads/" + producto.imagen;

    // mostrar modal
    modal.style.display = "flex";
}

    function closeTallaModal() {
    document.getElementById("tallaModal").style.display = "none";
}

    function confirmarTalla() {
    const select = document.getElementById("tallaSelect");
    const talla = select.value;

    if (!talla) {
    alert("Por favor selecciona una talla");
    return;
}

    // redirigir a agregar al carrito
    window.location.href = `/carrito/agregar?id=${productoSeleccionado.id}&nombre=${productoSeleccionado.nombre}&precio=${productoSeleccionado.precio}&imagen=${productoSeleccionado.imagen}&cantidad=1&talla=${talla}`;
}

    // Evento: abrir modal cuando dan clic al carrito
    document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".btn-add-cart").forEach(btn => {
        btn.addEventListener("click", () => {
            const producto = {
                id: btn.dataset.id,
                nombre: btn.dataset.nombre,
                precio: btn.dataset.precio,
                imagen: btn.dataset.imagen,
                categoria: btn.dataset.categoria,
                tallas: btn.dataset.tallas
            };
            if (producto.categoria && producto.categoria.toLowerCase() === "moda") {
                openTallaModal(producto);
            } else {
                window.location.href = `/carrito/agregar?id=${producto.id}&nombre=${producto.nombre}&precio=${producto.precio}&imagen=${producto.imagen}&cantidad=1`;
            }

        });
    });

    // asegurarse que siempre inicie cerrado
    document.getElementById("tallaModal").style.display = "none";
});
