new Vue({
    el: '#app',
    data: {
        products: [],
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
        orderDetails: {
            items: [],
            customer: {},
            deliveryAddress: {}
        }
    },
    created: function () {
        this.loadProducts();
        this.updateCartItemCount();
        if(window.orderId) {
            this.getOrderDetails(window.orderId)
        }
    },
    methods: {
        loadProducts() {
            let self = this;
            $.getJSON("/api/products", function (data) {
                console.log("Products Resp:", data)
                self.products = data.data
            });
        },
        updateCartItemCount() {
            let self = this;
            const cartId = localStorage.getItem("cartId");
            let cartParam = "";
            if(cartId) {
                cartParam = "?cartId="+cartId;
            }
            $.getJSON('/api/carts'+cartParam, function (data) {
                console.log("Cart Resp:", data)
                self.cart = data;
                localStorage.setItem("cartId", data.id);
                let count = 0;
                data.items.forEach(item => {
                    count = count + item.quantity;
                });
                $('#cart-item-count').text('('+count+')');
            })
            .fail(function() { self.removeCart(); });
        },
        addToCart(code) {
            let self = this;
            const cartId = localStorage.getItem("cartId");
            let cartParam = "";
            if(cartId) {
                cartParam = "?cartId="+cartId;
            }
            $.ajax ({
                url: '/api/carts'+cartParam,
                type: "POST",
                dataType: "json",
                contentType: "application/json",
                data : '{"code":"'+ code +'"}"',
                complete: function() {
                    self.updateCartItemCount();
                }
            });
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
                    self.updateCartItemCount();
                }
            });
        },
        removeCart() {
            let self = this;
            const cartId = localStorage.getItem("cartId");
            let cartParam = "";
            if(cartId) {
                cartParam = "?cartId="+cartId;
            }
            $.ajax ({
                url: '/api/carts'+cartParam,
                type: "DELETE",
                dataType: "json",
                contentType: "application/json",
                complete: function() {
                    localStorage.removeItem("cartId");
                    self.updateCartItemCount();
                }
            });
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
        },
        getOrderDetails(orderId) {
            let self = this;
            $.getJSON("/api/orders/"+orderId, function (data) {
                console.log("Get Order Resp:", data)
                self.orderDetails = data
            });
        }
    }
});
