new Vue({
    el: '#app',
    data: {
        cart: { items: [] },
        orderDetails: {
            items: [],
            customer: {},
            deliveryAddress: {}
        }
    },
    created: function () {
        updateCartItemCount();
        if(window.orderId) {
            this.getOrderDetails(window.orderId)
        }
    },
    methods: {
        getOrderDetails(orderId) {
            let self = this;
            $.getJSON("/api/orders/"+orderId, function (data) {
                console.log("Get Order Resp:", data)
                self.orderDetails = data
            });
        }
    }
});
