document.addEventListener('alpine:init', () => {
    Alpine.data('initData', () => ({
        products: [],
        cart: { items: [] },

        init() {
            this.loadProducts();
            updateCartItemCount();
        },
        loadProducts() {
            let self = this;
            $.getJSON("/api/products", (data)=> {
                console.log("Products Resp:", data)
                this.products = data.data
            });
        },
        addToCart(code) {
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
    }))
});
