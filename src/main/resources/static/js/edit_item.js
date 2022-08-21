function imageEdit(){
    let tr="";
    tr+="<tr id='mainImageTr'><th><label for=\"mainImage\">메인 이미지 파일 첨부</label></th>"
    tr+="<td><input type='file' id='mainImage' name='mainImage' required></td></tr>"
    tr += "<tr id='contentImageTr'><th><label for=\"contentImageFiles\">내용 이미지 파일 첨부</label></th>";
    tr +="<td><input type='file' id='contentImageFiles' name='contentImageFiles' required></td></tr>"
    tr +="<tr id='cancelBtn'><td colspan='2' style='text-align: center'><button type='button' class='btn btn-secondary' onclick='cancel()' >이미지 수정 취소</button></td></tr>"
    $("#imageEditBtn").replaceWith(tr);

}
function cancel() {
    $("#mainImageTr").remove();
    $("#contentImageTr").remove();
    $("#cancelBtn").replaceWith("<tr id='imageEditBtn'><td colspan='2' style='text-align: center'>" +
        "<button type='button' class='btn btn-secondary' onclick='imageEdit()' >상품 이미지 수정</button></td></tr>");
}

function deleteItem(){
    if(confirm("상품을 정말로 삭제하시겠습니까?")){

        let itemId = $("#itemId").val();
        let param={
            "itemId":itemId
        }

        $.ajax({
            url:'/admin/delete-product',
            type:'POST',
            contentType:'application/json',
            dataType:'json',
            data:JSON.stringify(param),
            success:function(){
                location.href='/store';
            }
        });
    }
}