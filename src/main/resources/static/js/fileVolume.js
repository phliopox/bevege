$(document).ready(function () {


    let max=10*1024*1024; //10MB
    let $input = $("input[type=file]");

    $input.bind('input', function () {

       if(""!=$(this).val()){

            let fileSize =this.files[0].size;
            if(fileSize>max){
                alert("파일 사이즈가 10MB를 초과합니다.");
                $(this).val("");
            }
        }
    }
    )});


