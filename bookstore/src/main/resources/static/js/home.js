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
                //console.log("Products Resp:", data)
                this.products = data.data
            });
        },
        addToCart(product) {
            addProductToCart(product)
            updateCartItemCount();
        },
    }))
});
