
function goItem(e){
    let itemId = e.parentElement.querySelector(".itemId").value;
    console.log(itemId)
    $.ajax({
        url:'/my_page/my_order/'+itemId,
        type:'GET',
        success:function (data) {
            location.href='/items/'+data+'/'+itemId;
        }
    })
}

function deleteOrder(e){
    let orderId = e.parentElement.querySelector(".orderId").value;
    console.log(orderId);

    if(confirm("주문을 취소하시겠습니까?")) {
       let param = {
            "orderId":orderId
        }

        $.ajax({
            url: '/my_page/cancel_order',
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(param),
            success: function () {
                $("#myOrderTable").load(" #myOrderTable");
            }
        });
    }
}

