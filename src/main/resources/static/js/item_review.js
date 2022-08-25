let pageNum = document.querySelector("#pageNum").value;
let Myeditor;

function review_board() {

    $("#toggle").toggle();

    reviewListAjax();
}
function reviewListAjax(){
    let reviewList = [];
    $.ajax({
        url: '/items/' + pageNum + "/" + itemId + "/reviews",
        type: 'GET',
        success: function (data) {
            if (data !=null) {
                reviewList = data;

                let reviewView = "";
                reviewView += "<div id='toggle'>"
                reviewView += "<div id='review_table' style='width:50%; margin-left:50%;'>";
                if (reviewList.length > 0) {
                    reviewView += "<table class='reviewList'>";
                    reviewView += "<thead><tr><th style='width: 80%'>Title</th><th style='width: 20%'>Writer</th></tr></thead>";
                    reviewView += "<tbody>";
                    for (let i = 0; i < reviewList.length; i++) {
                        reviewView += "<tr onclick='reviewDetail(this)'>";
                        reviewView += "<td style='display: none'><input id='reviewValue' value='" + reviewList[i].review + "' hidden></td>";
                        reviewView += "<td>" + reviewList[i].title + "</td>";
                        reviewView += "<td>" + reviewList[i].memberId + "</td></tr>";
                    }
                    reviewView += "</tbody>";
                    reviewView += "</table>";
                } else {
                    reviewView += "<p style='margin-top: 20px'>리뷰가 존재하지 않습니다.</p>";
                }

                if(document.querySelector("#orderedMember")!=null) {
                    reviewView += "<button class='col-md-4 btn btn-outline-dark' onclick='reviewEnroll()'>신규 리뷰 등록하기</button>";
                }

                reviewView +="</div>";

                $("#review_table").replaceWith(reviewView);
            }
        }
    });}


function reviewEnroll(){
    let reviewContent="";
    reviewContent +="<div id='review_table' style='width:60%; margin-left:40%;'>";
    reviewContent +="<input type='text' name='reviewTitle' id='reviewTitle' style=\"margin-top:15px; margin-bottom:15px; width:100%;\" required>";
    reviewContent += "<textarea id='classic' style=\"padding:20px; margin-top:5px; width:100%; border:1px solid black;\"></textarea>";
    reviewContent +="<button id='submit' style='margin-top:10px;' class=\"col-md-3 btn btn-outline-dark\" onclick='reviewSubmit()'>리뷰 등록</button>"
    reviewContent +="<button style='margin-top:10px;' class=\"col-md-3 btn btn-outline-dark\" onclick='reviewListAjax()'>목록으로</button>";
    reviewContent +="</div>";

    $("#review_table").replaceWith(reviewContent);

    ClassicEditor
        .create( document.querySelector( '#classic' ),{
            language: "ko",
            ckfinder:{
                uploadUrl:'http://localhost:8080/items/review_enroll/img_enroll'
            }
        })
        .then(editor=>{
            console.log(editor)
            Myeditor=editor;
        })
        .catch( error => {
            console.error( error );
        });
}

function reviewSubmit() {
    let title = $("#reviewTitle").val();
    let editorData = Myeditor.getData();
    let TagEncodingData=editorData.replace(/</g,"&lt;");
    let EncodingResult = TagEncodingData.replace(/>/g,"&gt;");

    console.log(TagEncodingData);
    console.log(EncodingResult);

    param = {
        "title": title,
        "ckeditor": EncodingResult
    }

    $.ajax({
        url: "/items/" + pageNum + "/" + itemId + "/" + "review_enroll",
        type: 'POST',
        contentType:'application/json',
        dataType: 'json',
        data: JSON.stringify(param),
        success: function (data) {
            review_board();
        }
    });
}

function reviewDetail(e){
    console.log(e);
    let value = e.querySelector("#reviewValue").value;

    let reviewContent = "";
    reviewContent += "<div id='review_table' style='width:50%; margin-left:50%;'>";
    reviewContent += "<div id='valueJs'>"+value+"</div>"
    reviewContent += "<button style='margin-top:10px;' class=\"col-md-3 btn btn-outline-dark\" onclick='reviewListAjax()'>목록으로</button>";
    reviewContent += "</div>";


    $("#review_table").replaceWith(reviewContent);

}
