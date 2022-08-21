$(document).ready(function () {
    comma();
})
function comma(){
    if(document.querySelector('.comma')!=null) {
        let $comma = $(".comma");

        for(let i=0; i<$comma.length; i++) {
            let hasComma = $comma[i].innerText.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            $(".comma")[i].innerText=hasComma;
        }
    }
}
let beforeValue;
    $(document).on('click',".controlBtn", function (e) {
        let selectBox="";
        selectBox +="<td class='orderStatus'>";
        selectBox += "<select class='statusBox'>";
        selectBox += "<option value='상태선택'>상태선택</option>";
        selectBox += "<option value='주문완료'>주문완료</option>"
        selectBox += "<option value='배송중'>배송중</option>";
        selectBox += "<option value='배송완료'>배송완료</option>";
        selectBox += "</select>";
        selectBox += "</td>";

        let btn="";
        btn="<button class='submitBtn btn btn-outline-dark' type='button'>저장</button>";

        beforeValue=$(this).parents()[1].innerHTML;

        let status = $(this).parent().siblings(".orderStatus");
        status.replaceWith(selectBox);

         $(this).replaceWith(btn);



        $(".submitBtn").on('click',function (e) {

            let orderStatus = $(this).parent().siblings(".orderStatus");
            let selectValue = orderStatus.children().val();
            let orderId = $(this).siblings(".orderId").val();
            let btn = $(this);

            if(selectValue=='상태선택'){
                /*원상복구*/
                $(this).parents()[1].innerHTML=beforeValue;
            }else {
                let param = {
                    "orderId": orderId,
                    "orderStatus": selectValue
                }
                $.ajax({
                    url: "/admin/customer-order",
                    type: 'POST',
                    contentType: 'application/json',
                    dataType: 'json',
                    data: JSON.stringify(param),
                    success: function () {
                        orderStatus.replaceWith("<td class='orderStatus'>" + String(selectValue) + "</td>");
                        btn.replaceWith("<button class='controlBtn btn btn-outline-dark' type=\"button\" >상태 변경</button>")
                    }
                })
            }
            comma();
        });
    });

function  reload() {
    location.href="/admin/customer-order";
}



