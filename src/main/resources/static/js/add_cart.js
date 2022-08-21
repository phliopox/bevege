
let itemId=$("#itemId").val();
let orderAmount=parseInt($("#orderAmount").val());
function minusComma(value){
    value = value.replace(/[^\d]+/g, "");
    return value;
}
window.onload=function () {
 let val = parseInt(minusComma($("#price").val()));
 let beforeSale=val+val*0.1;
  $("#beforeSale").text(beforeSale);
};

function amountUp(){
    $("#orderAmount").attr('value',++orderAmount);
}
function amountDown(){
    if(orderAmount>1){
        $("#orderAmount").attr('value',--orderAmount);}
}

function addCart(){

    let pageNum = $("#pageNum").val();
  if($("#login").val()=='true'){

    let param={
        "itemId":itemId,
        "orderAmount":orderAmount
    }
    $.ajax({
        url:'/my_page/add_cart',
        type:'POST',
        contentType:'application/json',
        dataType:'json',
        data:JSON.stringify(param),
        success:function(){
            let checkConfirm = confirm("장바구니로 이동하시겠습니까?");
            if(checkConfirm==true){
                location.href='/my_page/cart';
            }
        }
    });
  }else{
      if(confirm("로그인이 필요합니다.로그인 페이지로 이동하시겠습니까?")){
          location.href = '/login?redirectURL=/items/' + pageNum + '/' + itemId;
        }
  }
}
