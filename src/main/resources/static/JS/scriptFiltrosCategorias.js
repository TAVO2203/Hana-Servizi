document.addEventListener("DOMContentLoaded", () => {
    let productoActual = {};
    const modal = document.getElementById("modalCarrito");
    const closeModal = document.getElementById("closeModal");
    const selectTalla = document.getElementById("modal-talla");
    const btnConfirmar = document.getElementById("btnConfirmarAgregar");

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
                // Cargar imagen en modal
                document.getElementById("modal-imagen").src = "/uploads/" + productoActual.imagen;

                // Llenar select de tallas
                selectTalla.innerHTML = '<option disabled selected>Selecciona una talla</option>';

                if (productoActual.tallas && productoActual.tallas.includes(":")) {
                    productoActual.tallas.split(";").forEach(t => {
                        let [talla, stock] = t.split(":");
                        if (talla && parseInt(stock) > 0) {
                            let option = document.createElement("option");
                            option.value = talla;
                            option.text = `${talla} (Stock: ${stock})`;
                            selectTalla.add(option);
                        }
                    });
                } else {
                    let option = document.createElement("option");
                    option.value = "Única";
                    option.text = "Única";
                    selectTalla.add(option);
                }

                // Mostrar modal
                modal.style.display = "flex";
                setTimeout(() => modal.classList.add("active"), 10);
            } else {
                // Si no es de moda, agregar directo
                window.location.href = `/carrito/agregar?id=${productoActual.id}&nombre=${encodeURIComponent(productoActual.nombre)}&precio=${productoActual.precio}&cantidad=1&imagen=${encodeURIComponent(productoActual.imagen)}`;
            }
        });
    });

    // Confirmar agregar
    btnConfirmar.addEventListener("click", () => {
        let talla = selectTalla.value;
        if (!talla || talla === "Selecciona una talla") {
            alert("Por favor selecciona una talla.");
            return;
        }
        window.location.href = `/carrito/agregar?id=${productoActual.id}&nombre=${encodeURIComponent(productoActual.nombre)}&precio=${productoActual.precio}&cantidad=1&talla=${encodeURIComponent(talla)}&imagen=${encodeURIComponent(productoActual.imagen)}`;
    });

    // Cerrar modal con transición
    const cerrarModal = () => {
        modal.classList.remove("active");
        setTimeout(() => {
            modal.style.display = "none";
        }, 300);
    };

    closeModal.addEventListener("click", cerrarModal);
    window.addEventListener("click", e => {
        if (e.target === modal) cerrarModal();
    });
});
