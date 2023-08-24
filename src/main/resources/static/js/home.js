new Vue({
    el: '#app',
    data: {
        products: [],
        cart: { items: [] }
    },
    created: function () {
        this.loadProducts();
        updateCartItemCount();
    },
    methods: {
        loadProducts() {
            let self = this;
            $.getJSON("/api/products", function (data) {
                console.log("Products Resp:", data)
                self.products = data.data
            });
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
                    updateCartItemCount();
                }
            });
        },
    }
});
