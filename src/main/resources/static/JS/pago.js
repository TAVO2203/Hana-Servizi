const stripe = Stripe("pk_test_XXXXXXXXXXXXXXXXXXXXXXXX"); // tu clave pública de Stripe

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("payment-form");
    const total = document.getElementById("total").value;
    const messageContainer = document.getElementById("payment-message");

    const elements = stripe.elements();
    const cardElement = elements.create("card", { style: { base: { fontSize: "16px" } } });
    cardElement.mount("#card-element");

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        try {
            // 1. Crear PaymentIntent en tu backend
            const response = await fetch("/stripe/paymentintent", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    description: "Pago en HanaServizi",
                    amount: Math.round(parseFloat(total) * 100), // centavos
                    currency: "cop"
                })
            });

            const paymentIntent = await response.json();

            // 2. Confirmar el pago con Stripe
            const { error } = await stripe.confirmCardPayment(paymentIntent.client_secret, {
                payment_method: { card: cardElement }
            });

            if (error) {
                messageContainer.textContent = "❌ " + error.message;
                messageContainer.style.color = "red";
            } else {
                messageContainer.textContent = "✅ Pago realizado con éxito";
                messageContainer.style.color = "green";
                setTimeout(() => {
                    window.location.href = "/confirmacion";
                }, 2000);
            }

        } catch (err) {
            console.error("Error en el pago:", err);
            messageContainer.textContent = "⚠️ Error en el servidor";
            messageContainer.style.color = "red";
        }
    });
});
