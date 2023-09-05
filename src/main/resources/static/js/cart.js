document.addEventListener('alpine:init', () => {
    Alpine.data('initData', () => ({
        cart: { items: [] },
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
        },
        loadCart() {
            const cartId = localStorage.getItem("cartId");
            if(cartId) {
                $.getJSON("/api/carts?cartId=" + cartId, (data) => {
                    //console.log("Cart Resp:", data)
                    this.cart = data;
                })
                .fail( () => {
                    alert("Error loading cart");
                });
            }
        },
        updateItemQuantity(code, quantity) {
            const cartId = localStorage.getItem("cartId");
            let cartParam = "";
            if(cartId) {
                cartParam = "?cartId="+cartId;
            }
            $.ajax ({
                url: '/api/carts'+cartParam,
                type: "PUT",
                dataType: "json",
                contentType: "application/json",
                data : '{"code":"'+ code +'", "quantity":"'+quantity+'"}"',
                success: () => {
                    updateCartItemCount();
                    this.loadCart();
                }
            });
        },
        removeCart() {
            const cartId = localStorage.getItem("cartId");
            localStorage.removeItem("cartId");
            if(cartId) {
                $.ajax ({
                    url: "/api/carts?cartId="+cartId,
                    type: "DELETE",
                    dataType: "json",
                    contentType: "application/json",
                    success: function() {
                        updateCartItemCount();
                    }
                });
            }
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
                    console.log("Order Resp:", resp)
                    this.removeCart();
                    //alert("Order placed successfully")
                    window.location = "/order/"+resp.orderId;
                }
            });
        },
    }))
});
