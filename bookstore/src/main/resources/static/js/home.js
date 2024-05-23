document.addEventListener('alpine:init', () => {
    Alpine.data('initData', (pageNo) => ({
        pageNo: pageNo,
        products: {
            data: []
        },
        cart: { items: [] },

        init() {
            this.loadProducts(this.pageNo);
            updateCartItemCount();
        },
        loadProducts() {
            $.getJSON("/api/products?page="+pageNo, (data)=> {
                //console.log("Products Resp:", data)
                this.products = data
            });
        },
        addToCart(product) {
            addProductToCart(product);
        },
    }))
});
