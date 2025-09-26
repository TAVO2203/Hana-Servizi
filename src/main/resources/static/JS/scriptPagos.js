document.addEventListener('DOMContentLoaded', function () {
    const stripe = Stripe(stripePublicKey);
    const elements = stripe.elements();
    const card = elements.create('card');
    card.mount('#card-element');

    const metodoPago = document.getElementById('metodo-pago');
    const tarjetaForm = document.getElementById('tarjeta-form');
    const nequiForm = document.getElementById('nequi-form');
    const daviplataForm = document.getElementById('daviplata-form');
    const payBtn = document.getElementById('pay-btn');
    const messageEl = document.getElementById('payment-message');

    // Cambiar formularios según método de pago
    metodoPago.addEventListener('change', () => {
        tarjetaForm.style.display = metodoPago.value === 'tarjeta' ? 'block' : 'none';
        nequiForm.style.display = metodoPago.value === 'nequi' ? 'block' : 'none';
        daviplataForm.style.display = metodoPago.value === 'daviplata' ? 'block' : 'none';
    });

    // Manejo del formulario de pago
    document.getElementById('payment-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        payBtn.disabled = true;
        payBtn.textContent = "Procesando...";
        messageEl.textContent = "";

        const metodo = metodoPago.value;

        try {
            if (metodo === 'tarjeta') {
                // Crear PaymentIntent
                const resp = await fetch('/stripe/paymentintent', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        description: "Pago HanaServizi",
                        amount: Math.round(totalFinal * 100),
                        currency: "cop"
                    })
                });
                const data = await resp.json();

                // Confirmar pago con Stripe
                const result = await stripe.confirmCardPayment(data.client_secret, {
                    payment_method: {
                        card: card,
                        billing_details: { name: document.getElementById('cardholder-name').value }
                    }
                });

                if (result.error) {
                    messageEl.textContent = "❌ " + result.error.message;
                    messageEl.style.color = "red";
                } else if (result.paymentIntent && result.paymentIntent.status === 'succeeded') {

                    // 🔹 CAMBIO CLAVE: Enviar telefono y direccion al backend
                    const pagoData = {
                        telefono: document.getElementById('telefono').value,
                        direccion: document.getElementById('direccion').value
                    };

                    const confirmResp = await fetch(`/stripe/confirm/${result.paymentIntent.id}`, {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(pagoData)
                    });
                    const confirmData = await confirmResp.json();

                    if (confirmData.id) {
                        messageEl.textContent = "✅ Pago con tarjeta exitoso";
                        messageEl.style.color = "green";
                        window.location.href = "/stripe/confirmacion?pagoId=" + confirmData.id;
                    } else {
                        messageEl.textContent = "⚠️ Error: No se pudo obtener el ID del pago";
                        messageEl.style.color = "red";
                    }
                }

            } else if (metodo === 'nequi') {
                const telefono = document.getElementById('telefono-nequi').value;
                const resp = await fetch(`/stripe/nequi?telefono=${telefono}&total=${totalFinal}&userId=${usuarioId}`, { method: 'POST' });
                const result = await resp.json();

                if (result.status === "success" && result.id) {
                    messageEl.textContent = "✅ Pago con Nequi exitoso";
                    messageEl.style.color = "green";
                    setTimeout(() => window.location.href = "/stripe/confirmacion?pagoId=" + result.id, 1000);
                } else {
                    messageEl.textContent = "⚠️ Error en Nequi";
                    messageEl.style.color = "red";
                }

            } else if (metodo === 'daviplata') {
                const telefono = document.getElementById('telefono-daviplata').value;
                const resp = await fetch(`/stripe/daviplata?telefono=${telefono}&monto=${totalFinal}&userId=${usuarioId}`, { method: 'POST' });
                const result = await resp.json();

                if (result.status === "success" && result.id) {
                    messageEl.textContent = "✅ Pago con Daviplata exitoso";
                    messageEl.style.color = "green";
                    setTimeout(() => window.location.href = "/stripe/confirmacion?pagoId=" + result.id, 1000);
                } else {
                    messageEl.textContent = "⚠️ Error en Daviplata";
                    messageEl.style.color = "red";
                }
            }

        } catch (err) {
            console.error(err);
            messageEl.textContent = "⚠️ Error: " + err.message;
            messageEl.style.color = "red";
        }

        payBtn.disabled = false;
        payBtn.textContent = "Pagar Ahora";
    });
});
