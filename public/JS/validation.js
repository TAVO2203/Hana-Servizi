const form = document.querySelector('form');

form.addEventListener('submit', (event) => {
        event.preventDefault(); // Prevenir envío del formulario
        const inputs = form.querySelectorAll('input, select');
        let esValido = true;

        // Limpiar mensajes de error anteriores
        form.querySelectorAll('.error').forEach((error) => error.remove());

        // Validaciones por cada campo
        inputs.forEach((input) => {
                let mensajeError = '';
                const valor = input.value.trim();

                // Validaciones específicas
                if (input.name === 'Nombre' && (!valor || valor.split(' ').length < 2)) {
                        mensajeError = 'Nombre: Debes incluir al menos 2 carácteres.';
                } else if (input.name === 'direccion' && !valor) {
                        mensajeError = 'Dirección: La dirección no puede estar vacía.';
                } else if (input.name === 'Telefono' && !/^[0-9]{10,15}$/.test(valor)) {
                        mensajeError = 'Telefono: Debe contener entre 10 y 15 dígitos.';
                } else if (input.name === 'Email' && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(valor)) {
                        mensajeError = 'Email: El correo electrónico no es válido.';
                } else if (input.name === 'Contraseña' && !/^.{4,12}$/.test(valor)) {
                        mensajeError = 'Contraseña: Debe tener al menos de 4 a 12 caracteres.';
                } else if (input.name === 'Confirm-password' && valor !== document.getElementById('password').value) {
                        mensajeError = 'Las contraseñas no coinciden.';
                }

                // Mostrar mensaje de error si aplica
                if (mensajeError) {
                        esValido = false;
                        mostrarError(input, mensajeError);
                }
        });

        // Si es válido, enviar el formulario
        if (esValido) {
                alert('Formulario enviado correctamente.');
                form.submit();
        }
});

// Función para mostrar un mensaje de error debajo del campo
function mostrarError(input, mensaje) {
        const error = document.createElement('small');
        error.classList.add('error');
        error.style.color = 'red';
        error.textContent = mensaje;
        input.parentNode.appendChild(error);
}

// Validar mayoría de edad
//function esMayorDeEdad(fechaNacimiento) {
       //const fechaNacimientoDate = new Date(fechaNacimiento);
       // const diferencia = fechaActual.getFullYear() - fechaNacimientoDate.getFullYear();
       // const mes = fechaActual.getMonth() - fechaNacimientoDate.getMonth();
       // const dia = fechaActual.getDate() - fechaNacimientoDate.getDate();
        //return (
           // diferencia > 18 ||
           // (diferencia === 18 && mes > 0) ||
           // /);
//}