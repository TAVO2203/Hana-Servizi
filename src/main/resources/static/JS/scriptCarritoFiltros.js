document.addEventListener("DOMContentLoaded", () => {
    let productoActual = {};

    const modal = document.getElementById("modalCarrito");
    const closeModal = document.getElementById("closeModal");

    // Abrir modal solo si es moda
    document.querySelectorAll(".btn-carrito").forEach(btn => {
        btn.addEventListener("click", () => {
            productoActual = {
                id: btn.dataset.id,
                nombre: btn.dataset.nombre,
                precio: btn.dataset.precio,
                imagen: btn.dataset.imagen,
                categoria: btn.dataset.categoria,
                tallas: btn.dataset.tallas
            };

            if (productoActual.categoria && productoActual.categoria.toLowerCase() === "moda") {
                document.getElementById("modal-nombre").innerText = productoActual.nombre;
                document.getElementById("modal-precio").innerText = "$ " + productoActual.precio;
                document.getElementById("modal-imagen").src = "/uploads/" + productoActual.imagen;

                let selectTalla = document.getElementById("modal-talla");
                selectTalla.innerHTML = "";

                if (productoActual.tallas && productoActual.tallas.includes(":")) {
                    productoActual.tallas.split(";").forEach(t => {
                        let [talla, stock] = t.split(":");
                        if (talla && parseInt(stock) > 0) {
                            let option = document.createElement("option");
                            option.value = talla;
                            option.text = `${talla} (stock: ${stock})`;
                            selectTalla.add(option);
                        }
                    });
                } else {
                    let option = document.createElement("option");
                    option.value = "Única";
                    option.text = "Única";
                    selectTalla.add(option);
                }

                modal.style.display = "flex"; // mostrar modal
            } else {
                // Enviar directo al carrito
                window.location.href = `/carrito/agregar?id=${productoActual.id}&nombre=${encodeURIComponent(productoActual.nombre)}&precio=${productoActual.precio}&cantidad=1&imagen=${productoActual.imagen}`;
            }
        });
    });

    // Confirmar agregar
    document.getElementById("btnConfirmarAgregar").addEventListener("click", () => {
        let talla = document.getElementById("modal-talla").value;
        window.location.href = `/carrito/agregar?id=${productoActual.id}&nombre=${encodeURIComponent(productoActual.nombre)}&precio=${productoActual.precio}&cantidad=1&talla=${talla}&imagen=${productoActual.imagen}`;
    });

    // Cerrar modal
    closeModal.addEventListener("click", () => modal.style.display = "none");
    window.addEventListener("click", e => {
        if (e.target === modal) modal.style.display = "none";
    });
});
