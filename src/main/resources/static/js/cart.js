new Vue({
    el: '#app',
    data: {
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
        }
    },
    created: function () {
        updateCartItemCount();
        this.loadCart();
    },
    methods: {
        loadCart() {
            let self = this;
            const cartId = localStorage.getItem("cartId");
            if(cartId) {
                $.getJSON("/api/carts?cartId=" + cartId, function (data) {
                    //console.log("Cart Resp:", data)
                    self.cart = data;
                })
                .fail(function () {
                    alert("Error loading cart");
                });
            }
        },
        updateItemQuantity(code, quantity) {
            let self = this;
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
                complete: function() {
                    updateCartItemCount();
                }
            });
        },
        removeCart() {
            let self = this;
            const cartId = localStorage.getItem("cartId");
            if(cartId) {
                $.ajax ({
                    url: "/api/carts?cartId="+cartId,
                    type: "DELETE",
                    dataType: "json",
                    contentType: "application/json",
                    complete: function() {
                        localStorage.removeItem("cartId");
                        updateCartItemCount();
                    }
                });
            }
        },
        createOrder() {
            console.log("Current Cart:",this.cart)
            console.log("Order Form", this.orderForm);
            let order = Object.assign({}, this.orderForm, {items: this.cart.items});
            console.log("Order ", order);

            let self = this;
            $.ajax ({
                url: '/api/orders',
                type: "POST",
                dataType: "json",
                contentType: "application/json",
                data : JSON.stringify(order),
                complete: function(resp) {
                    console.log("Order Resp:", resp.responseJSON)
                    self.removeCart();
                    alert("Order placed successfully")
                    window.location = "/order/"+resp.responseJSON.orderId;
                }
            });
        }
    }
});
