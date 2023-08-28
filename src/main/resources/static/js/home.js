document.addEventListener('alpine:init', () => {
    Alpine.data('initData', () => ({
        products: [],
        cart: { items: [] },

        init() {
            this.loadProducts();
            updateCartItemCount();
        },
        loadProducts() {
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
                success: (data) => {
                    localStorage.setItem("cartId", data.id);
                    updateCartItemCount();
                },
                error: (jqXHR, textStatus, errorThrown) => {
                    if(jqXHR.status === 404) {
                        localStorage.removeItem("cartId");
                        this.addToCart(code);
                    }
                }
            });
        },
    }))
});
