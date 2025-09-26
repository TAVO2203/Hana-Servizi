document.addEventListener("DOMContentLoaded", () => {
    let productoActual = {};
    const modal = document.getElementById("modalCarrito");
    const closeModal = document.getElementById("closeModal");

    // Abrir modal
    document.querySelectorAll(".btn-add-cart").forEach(btn => {
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

                modal.classList.add("active");
            } else {
                window.location.href = `/carrito/agregar?id=${productoActual.id}&nombre=${encodeURIComponent(productoActual.nombre)}&precio=${productoActual.precio}&cantidad=1&imagen=${encodeURIComponent(productoActual.imagen)}`;
            }
        });
    });

    // Confirmar agregar
    document.getElementById("btnConfirmarAgregar").addEventListener("click", () => {
        let talla = document.getElementById("modal-talla").value;
        window.location.href = `/carrito/agregar?id=${productoActual.id}&nombre=${encodeURIComponent(productoActual.nombre)}&precio=${productoActual.precio}&cantidad=1&talla=${encodeURIComponent(talla)}&imagen=${encodeURIComponent(productoActual.imagen)}`;
    });

    // Cerrar modal con transición suave
    const cerrarModal = () => {
        modal.classList.remove("active");
        setTimeout(() => {
            modal.style.display = "none";
        }, 400); // mismo tiempo que el transition
    };

    closeModal.addEventListener("click", cerrarModal);
    window.addEventListener("click", e => {
        if (e.target === modal) cerrarModal();
    });
});
