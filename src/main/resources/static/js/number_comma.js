
$(document).ready(function () {

    if(document.querySelector('.comma')!=null) {
        let $comma = $(".comma");

        for(let i=0; i<$comma.length; i++) {
            let hasComma = $comma[i].value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            $(".comma")[i].value=hasComma;
        }
    }

});
