function updateCartItemCount() {
    const cartId = localStorage.getItem("cartId");
    let cartParam = "";
    if(cartId) {
        cartParam = "?cartId=" + cartId;
    }
    $.getJSON("/api/carts" + cartParam, function (data) {
        console.log("Cart Count Resp:", data)
        localStorage.setItem("cartId", data.id);
        let count = 0;
        data.items.forEach(item => {
            count = count + item.quantity;
        });
        $('#cart-item-count').text('(' + count + ')');
    })
    .fail(function () {
        localStorage.removeItem("cartId");
        $('#cart-item-count').text('(' + 0 + ')');
    });
}