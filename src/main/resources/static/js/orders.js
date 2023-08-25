document.addEventListener('alpine:init', () => {
    Alpine.data('initData', () => ({
        orders: [],
        cart: { items: [] },

        init() {
            this.loadOrders();
            updateCartItemCount();
        },
        loadOrders() {
            let self = this;
            $.getJSON("/api/orders", (data) => {
                //console.log("orders :", data)
                this.orders = data
            });
        },
    }))
});
