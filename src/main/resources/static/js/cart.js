document.addEventListener('alpine:init', () => {
    Alpine.data('initData', () => ({
        cart: { items: [], totalAmount: 0 },
        orderForm: {
            customer: {
                name: "Siva",
                email: "siva@gmail.com",
                phone: "999999999999"
            },
            payment: {
                cardNumber: "1111222233334444",
                cvv: "123",
                expiryMonth: 2,
                expiryYear: 2030
            },
            deliveryAddress: {
                addressLine1: "KPHB",
                addressLine2: "Kukatpally",
                city:"Hyderabad",
                state: "TS",
                zipCode: "500072",
                country: "India"
            }
        },

        init() {
            updateCartItemCount();
            this.loadCart();
            this.cart.totalAmount = getCartTotal();
        },
        loadCart() {
            this.cart = getCart()
        },
        updateItemQuantity(code, quantity) {
            updateProductQuantity(code, quantity);
            updateCartItemCount();
            this.cart.totalAmount = getCartTotal();
        },
        removeCart() {
            deleteCart()
            updateCartItemCount();
        },
        createOrder() {
            //console.log("Current Cart:",this.cart)
            //console.log("Order Form", this.orderForm);
            let order = Object.assign({}, this.orderForm, {items: this.cart.items});
            //console.log("Order ", order);

            $.ajax ({
                url: '/api/orders',
                type: "POST",
                dataType: "json",
                contentType: "application/json",
                data : JSON.stringify(order),
                success: (resp) => {
                    //console.log("Order Resp:", resp)
                    this.removeCart();
                    //alert("Order placed successfully")
                    window.location = "/order/"+resp.orderId;
                }
            });
        },
    }))
});
