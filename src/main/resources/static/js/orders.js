new Vue({
    el: '#app',
    data: {
        orders: [],
        cart: { items: [] },
    },
    created: function () {
        this.loadOrders();
        updateCartItemCount();
    },
    methods: {
        loadOrders() {
            let self = this;
            $.getJSON("/api/orders", function (data) {
                //console.log("orders :", data)
                self.orders = data
            });
        },
    }
});
