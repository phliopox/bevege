
$(document).ready(function (){
    checkedReSum();

    $(".checkBox").change(function(){
        checkedReSum();
        if(document.querySelector('#orderPage table')!=null){
            document.querySelector('#orderPrice').value = document.querySelector('#selectedItemSum').value;
            console.log(document.querySelector('#selectedItemSum').value);
/*
            $('#orderPrice').attr('value',$('#selectedItemSum').val());
*/
        }
    });

});
function plusComma(){
    if(document.querySelector('.comma')!=null) {
        let $comma = $(".comma");
        for(let i=0; i<$comma.length; i++) {
            let hasComma = $comma[i].value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            $(".comma")[i].value=hasComma;
        }
    }
}
function minusComma(value){
    value = value.replace(/[^\d]+/g, "");
    return value;
}
function orderAmountUp(e){
    let itemId = e.parentElement.querySelector(".itemId").value;
    let amountTag=e.parentElement.querySelector(".orderAmount");
    let val=amountTag.value;

    amountTag.value=++val;
    let param={
        "editAmount":val,
        "itemId":itemId
    }

    $.ajax({
        url:'/my_page/cart/amount_edit',
        type:'POST',
        contentType:'application/json',
        dataType:'json',
        data:JSON.stringify(param),
        success:function () {
            alert("수량이 변경되었습니다.");
            let priceValue = e.parentElement.parentElement.querySelector(".price").value;
            let minusCommaVal = parseInt(minusComma(priceValue));
            let resultPriceTag = e.parentElement.parentElement.querySelector(".resultPrice");
            resultPriceTag.value=val*minusCommaVal;
            checkedReSum();
            if(document.querySelector('#orderPage table')!=null){
                document.querySelector('#orderPrice').value = document.querySelector('#selectedItemSum').value;
                console.log(document.querySelector('#selectedItemSum').value);}

    } });
}

function orderAmountDown(e){
    let itemId=e.parentElement.querySelector(".itemId").value;
    let amountTag = e.parentElement.querySelector(".orderAmount");
    let val=amountTag.value;

    if(val>1){
        amountTag.value=--val;
    }
    let param={
        "editAmount":val,
        "itemId":itemId
    }

    $.ajax({
        url:'/my_page/cart/amount_edit',
        type:'POST',
        contentType:'application/json',
        dataType:'json',
        data:JSON.stringify(param),
        success:function () {
            alert("수량이 변경되었습니다.");
            let priceValue = e.parentElement.parentElement.querySelector(".price").value;
            let minusCommaVal = parseInt(minusComma(priceValue));
            let resultPriceTag = e.parentElement.parentElement.querySelector(".resultPrice");
            resultPriceTag.value=val*minusCommaVal;

            checkedReSum();
            if(document.querySelector('#orderPage table')!=null){
                document.querySelector('#orderPrice').value = document.querySelector('#selectedItemSum').value;
                console.log(document.querySelector('#selectedItemSum').value);}
        }
    });
}

function deleteOneItem(e){
    let itemId= e.parentElement.querySelector(".itemId").value;
    let param={
        "itemId":itemId
    }
    $.ajax({
        url:'/my_page/cart/delete_item',
        type:'POST',
        contentType:'application/json',
        dataType:'json',
        data:JSON.stringify(param),
        success:function(){
            console.log("통신성공");
            e.parentElement.parentElement.parentElement.remove();
            checkedReSum();
        }
    });
}

function checkedReSum(){
    let allCheckBox = document.querySelectorAll(".checkBox");

    let sum=0;
    for(let i=0; i<allCheckBox.length; i++){
        let element = allCheckBox.item(i);
        if(element.checked){
            let itemPriceTag = element.parentElement.parentElement.querySelector(".resultPrice");
            let itemPrice = parseInt(minusComma(itemPriceTag.value));
            sum+=itemPrice;
        }
    }
    document.querySelector("#selectedItemSum").value=sum;
    plusComma();
}

function allSelected(){
    let total = $(".checkBox").length;
    let checked = $(".checkBox:checked").length;
    let checkBoolean = $(".checkBox").is(":checked");

    if(total!=checked&&checkBoolean||!checkBoolean){ //일부만 체크 || 전부 해제 일때
        $(".checkBox").prop("checked", true);}
    else if(total==checked&&checkBoolean) { //전부 체크 일때
        $(".checkBox").prop("checked", false);
     }
    }

function selectedDelete(){
    let allCheckBox = document.querySelectorAll(".checkBox");
    for(let i=0; i<allCheckBox.length; i++){
        let element = allCheckBox.item(i);
        if(element.checked){
           deleteOneItem(element.parentElement);
        }
    }
}