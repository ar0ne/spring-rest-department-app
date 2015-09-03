$(function() {
    $('#datetimepicker1').datetimepicker({

    });

    $('#datetimepicker2').datetimepicker({

    });

    $("#search_wrapper").hide();

    $("#search_wrapper__button").on("click", function(e){
        e.preventDefault();
        $("#search_wrapper").toggle();
    });

    $("#search__button").on("click", function(e) {
        e.preventDefault();

        var val_1 = $("#datetimepicker1").find("input").val();
        var val_2 = $("#datetimepicker2").find("input").val();

        var patt = /\d{4}-\d{2}-\d{2}/;


        var cur_url = $("#domain_url").text();

        if (val_1 && val_2) {

            if(patt.test(val_1) && patt.test(val_2)) {
                window.location.href = cur_url + "/date/" + val_1 + "/" + val_2;
            }

        } else if (val_1 && val_2.length == 0) {
            if(patt.test(val_1)) {
                window.location.href = cur_url + "/date/" + val_1;
            }
        }
    })

});