let startPage = $("#startPage").val();
let endPage = $("#endPage").val();
let prev =$("#prev").val();
let next =$("#next").val();
let pageNum=parseInt($("#pageNum").val());

window.onload = function () {

    let liStr = "";
    liStr += '<div id="pageMarker">';
    liStr += '<ul id="page_list">';

    if (prev==='true'){
        liStr += '<button style="border:none; width:100px; height:30px; margin-right:20px;" onclick="prevBtn()">Previous</button>';
    }

    for (let i = startPage; i <= endPage; i++) {
        if(pageNum==i){
            liStr += '<li class="active" onclick="paging(this.innerText,this)">' + i + '</li>';
        }else {
            liStr += '<li onclick="paging(this.innerText,this)">' + i + '</li>';
        }
    }

    if (next==='true') {
        liStr += '<button style="border:none; width:80px; height:30px; margin-left:20px;" onclick="nextBtn()">Next</button>';
    }

    liStr += '</ul>';
    liStr += '</div>';
    liStr += '<form id="pageNumSubmit" action="" method="get">';
    liStr += '<input id="pageIndex" name="pageIndex" value=""  hidden>'
    liStr += '</form>';

    $("#pageMarker").replaceWith(liStr);

};

function paging(liPageNum, e){
    $(e).addClass("active");
    $("#pageIndex").attr('value',liPageNum);
    $("#pageNumSubmit").submit();
}

function prevBtn(){
    let prevBlock=parseInt(startPage)-1;
    paging(prevBlock);
}

function nextBtn(){
    let nextBlock=parseInt(endPage)+1;
    paging(nextBlock);
}
