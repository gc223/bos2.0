$(function () {

    'use strict';


    var $citypicker1 = $('#sendAreaInfo');

    $citypicker1.citypicker();

    var $citypicker2 = $('#recAreaInfo');

    $citypicker2.citypicker({
        province: '江苏省',
        city: '常州市',
        district: '溧阳市'
    });

    // var $citypicker3 = $('#city-picker3');

    $('#reset').click(function () {
        $citypicker1.citypicker('reset');


    });
    $('#reset2').click(function () {
        $citypicker2.citypicker('reset');


    });

    //
    // $('#destroy').click(function () {
    //     $citypicker3.citypicker('destroy');
    // });


});
