
//this.id를 통해 replyId 값 받고, response 로 board_id를 받아서 부분 load 해줌
let board_id = document.querySelector("#postId").value;
let pageNum = document.querySelector("#pageNum").value;
function deleteCmt(e){
    let replyId=e.id;
    let param = {
        "replyId": replyId
    };
    $.ajax({
        url:'/board/replyDelete',
        type:'POST',
        contentType:'application/json;',
        dataType:'json',
        data:JSON.stringify(param),
        success:function (){
            console.log("통신성공");
            $('#reply_section').load(" #reply_section");
        }
    });
}

function updateReply(e){

    let replyId = e.id;
    let parentUlTag = $(e).parents().get(1);
    let content =parentUlTag.querySelector(".reply_content");
    let writer = parentUlTag.querySelector(".reply_writer");

    let commentsView="";

    commentsView += '<ul id="replyUpdate_section">';
    commentsView += '<li class="reply_writer" style="font-weight:700; font-size:13px;">';
    commentsView += writer.innerText;
    commentsView += '</li>';
    commentsView += '<textarea type="text" id="update_content" required>';
    commentsView += content.innerText;
    commentsView += '</textarea>';
    commentsView += '<button type="button" onclick="updateBtn('+replyId+')">수정</button>';
    commentsView += '<button type="button" onclick="getReplyList()">취소</button>';
    commentsView += '</ul>';

    $(parentUlTag).replaceWith(commentsView);

}

function updateBtn(replyId){

    let updateContent = $("#update_content").val();
    let param={
        "replyId" :replyId,
        "updateContent" : updateContent
    };

    $.ajax({
        url: '/board/replyUpdate',
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify(param),
        success: function(result){
            console.log(result);
            console.log("통신성공");
            $('#reply_section').load(" #reply_section");

        }
    });
}

function getReplyList(){
    console.log(pageNum);
    $.ajax({
        url:'/board/'+pageNum+'/'+board_id,
        type:'GET',
        success:function(){
            $('#reply_section').load(" #reply_section");
        }
    });
}

function deletePost(){
    return confirm("게시글과 함께 댓글도 모두 삭제됩니다. \n정말로 삭제하시겠습니까?") == true;
}







/*board js 코드 리팩토링 과정용 이전코드!*/


/*
window.onload = function () {
    let element = document.querySelector("#reply_delete");
    initEvent(element);
}
function initEvent(element){
    if(element!=null) {
        element.addEventListener('click', deleteCmt);
    }
}
function deleteCmt(){
    let replyIdStr = document.querySelector("#replyId").innerText;

    let param= {
        "replyId" : replyIdStr
    };
    let xhr = new XMLHttpRequest();

    xhr.open('POST','/board/replyDelete',true);
    xhr.setRequestHeader('Content-Type','application/json');
    xhr.onload= () => {
        if(xhr.status==200) {

            console.log(xhr.response)
            console.log(console.log("통신성공"));
        }else{
            console.log(xhr.responseText);
            console.log("통신실패");
        }
    }
    xhr.send(JSON.stringify(param));
}

function deletePost(){
    if(confirm("게시글과 함께 댓글도 모두 삭제됩니다. \n정말로 삭제하시겠습니까?")==true){
        return true;
    }
    return false;
}*/

//querySelector 로 replyId 선택후 ajax 로 보냄
/*
function findReplyId(e){
    let ulTag = document.querySelectorAll(".reply_list ul");

    for (const ul of ulTag) {
        let selectCorrect = ul.querySelector("button").isEqualNode(e);
        if(selectCorrect){
            let replyId = ul.querySelector("#replyId").innerText;
            console.log(replyId);
            deleteCmt(replyId);
            return;
        }
    }
}
function deleteCmt(replyId){

    let param = {
        "replyId": replyId
    };

    let xhr = new XMLHttpRequest();

    xhr.open('POST','/board/replyDelete',true);
    xhr.setRequestHeader('Content-Type','application/json');
    xhr.onload= () => {
        if(xhr.status==200) {

            console.log(xhr.response);
            console.log(replyId);
            console.log("통신성공");
        }else{
            console.log(xhr.responseText);
            console.log("통신실패");
        }
    }
    xhr.send(JSON.stringify(param));
}

function deletePost(){
    if(confirm("게시글과 함께 댓글도 모두 삭제됩니다. \n정말로 삭제하시겠습니까?")==true){
        return true;
    }
    return false;
}*/

//a 태그 id 에 replyId 담고 onclick 메소드에 this 를 태워 this.id로 꺼냄
/*function deleteCmt(e){
    let replyId=e.id;
    let param = {
        "replyId": replyId
    };

    let xhr = new XMLHttpRequest();

    xhr.open('POST','/board/replyDelete',true);
    xhr.setRequestHeader('Content-Type','application/json');
    xhr.send(JSON.stringify(param));
    xhr.onload= () => {
        if(xhr.status==200) {

            console.log(xhr.response);
            console.log(replyId);
            console.log("통신성공");

        }else{
            console.log(xhr.responseText);
            console.log("통신실패");
        }
    }
}

function deletePost(){
    return confirm("게시글과 함께 댓글도 모두 삭제됩니다. \n정말로 삭제하시겠습니까?") == true;

}*/
