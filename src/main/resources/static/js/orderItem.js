

function payment(){
    let price = minusComma(document.querySelector("#selectedItemSum").value);
    $.ajax({
        url:'/my_page/order/payment',
        type:'GET',
        success:function (data) {
            let orderPage ="";
            orderPage += "<section id='orderPage' class=\"page-add cart-page-add\">";
            orderPage +="<div class=\"container px-4 px-lg-5 my-5\">"
            orderPage +="<table>"
            orderPage +="<tr><td colspan='2'><p>주문서 작성</p></td></tr>"
            orderPage +="<tr><td class='name'>받는사람</td>";
            orderPage +="<td><input id='receiverName' type='text'></td></tr>"
            orderPage +="<tr><td class='name' >주소</td>";
            orderPage +="<td><input class='address_kakao' id='address' type='text' style=\"width:350px;\"></td></tr>"
            orderPage +="<tr><td class='name'>소유포인트</td>";
            orderPage +="<td><input class='accountPoint comma' type='text' value='"+data.point+"' readonly></td></tr>"
            orderPage +="<tr><td class='name'>포인트 사용</td>";
            orderPage +="<td><input id='usedPoint' type='number' value='0'></td></tr>"
            orderPage +="<tr><td class='name'>계좌 현금</td>";
            orderPage +="<td><input class='accountMoney comma' type='text' value='"+data.money+"' readonly></td></tr>"
            orderPage +="<tr><td class='name'>현금 사용</td>";
            orderPage +="<td><input id='usedMoney' type='number' value='0'></td></tr>"
            orderPage +="<tr id='lastTr'><td id='orderPriceTh'>총 결제 금액</td><td><input class='comma' id='orderPrice' type='text' value='"+price+"' readonly></td></tr>"
            orderPage +="<tr><td colspan='2'><button class=\"col-md-3 btn btn-dark\" onclick='orderItem(this)'>결제</button></td></tr>"
            orderPage +="</table>"
            orderPage +="</div>"
            orderPage +="</section>";

            $("#orderPage").replaceWith(orderPage);
            let val = $('#orderPrice').val();
            plusComma();
            $('html, body').scrollTop(document.body.scrollHeight)

            document.querySelector(".address_kakao").addEventListener("click", function () { //주소입력칸을 클릭하면
                //카카오 지도 발생
                new daum.Postcode({
                    oncomplete: function (data) { //선택시 입력값 세팅
                        document.querySelector(".address_kakao").value = data.address; // 주소 넣기
                    }
                }).open();
            });
        }
    })
}



function orderItem(e) {
    let table = e.parentElement.parentElement.parentElement;
    let receiverName = table.querySelector("#receiverName").value;
    let address = table.querySelector("#address").value;
    let point =table.querySelector("#usedPoint").value
    let money = table.querySelector("#usedMoney").value;

    //입력된 point money 검증 준비
    let usedPoint = point==''?0:point;
    let usedMoney = money==''?0:money;
    let paymentMoney = parseInt(usedPoint) + parseInt(usedMoney);
    let resultMoney = parseInt(minusComma(table.querySelector("#orderPrice").value));

    //이름과 주소 검증 준비
    let elementNodeListOf = document.querySelectorAll("#orderPage input[type=text]");
    let emptyString=false;
    for (const e of elementNodeListOf) {
        if(e.value.trim()==''){
            emptyString=true;
        }
    }

    //소유금액이 결제 금액 지불 가능한가
    let hasMoney=false;
    let accountMoney=parseInt(minusComma(document.querySelector(".accountMoney").value));
    let accountPoint=parseInt(minusComma(document.querySelector(".accountPoint").value));
    console.log(accountPoint + accountMoney);
    if(accountMoney+accountPoint>=resultMoney){
        hasMoney=true;
    }

    //검증 로직
    if(paymentMoney==resultMoney&&!emptyString&&hasMoney) {

        let allChecked = $(".checkBox:checked");

        for (let i = 0; i < allChecked.length; i++) {
            let parentElement = allChecked[i].parentElement.parentElement;
            let itemId = parentElement.querySelector(".itemId").value;
            let orderAmount = parentElement.querySelector(".orderAmount").value;
            let orderPrice = minusComma(parentElement.querySelector(".resultPrice").value);

            param = {
                "receiverName": receiverName,
                "address": address,
                "usedPoint": usedPoint,
                "usedMoney": usedMoney,

                "itemId": itemId,
                "orderPrice": orderPrice,
                "orderAmount": orderAmount
            }
            $.ajax({
                url: '/my_page/order',
                type: 'POST',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(param),
                success: function () {
                    window.location.href = "/my_page/my_order";
                }
            })
        }
    }else{
        if(emptyString){
            alert("이름과 주소를 입력하세요.");
        }
        if(paymentMoney!=resultMoney){
            alert("결제금액이 충분하지 않습니다.");
        }
        if(paymentMoney>resultMoney){
            alert("정확한 결제금액을 입력해주세요!")
        }
        if(!hasMoney){
            let noMoney = document.querySelector(".noMoney");
            if(noMoney==null){
                $("#lastTr").after("<tr class='noMoney'><td colspan='2'><p style='color:red;'>소유 금액이 부족합니다</p></td></tr>");
            }
        }
    }
}
