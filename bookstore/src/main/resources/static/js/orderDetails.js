document.addEventListener('alpine:init', () => {
    Alpine.data('initData', (orderId) => ({
        id: orderId,
        cart: { items: [] },
        orderDetails: {
            items: [],
            customer: {},
            deliveryAddress: {}
        },
        init() {
            updateCartItemCount();
            this.getOrderDetails(this.id)
        },
        getOrderDetails(orderId) {
            $.getJSON("/api/orders/"+ orderId, (data) => {
                //console.log("Get Order Resp:", data)
                this.orderDetails = data
            });
        }

    }))
});
