$(document).ready(function() {

    console.log("Script loaded");
    if($.ajax){
        console.log("Ajax ready");
    }else{
        console.log("Ajaxa ti gde??????");
    }

    $('#ping_btn').on('click', function(event) {

        const fromCurrency = $('#fromCurrency').val();
        const toCurrency = $('#toCurrency').val();
        const amount = $('#amount').val();

        const postBody = {"action":"calculate", "data" : {
         "fromCurrency" : fromCurrency, "toCurrency" : toCurrency, "amount" : amount }
        }

        $.ajax({
            url: '/api/rates',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(postBody),
            success: function(response) {
                console.log(response);
                $('#amount_label').text(response.result.resultAmount);
            },
            error: function(error) {
                console.error('Error:', error);
            }
        });
    });
});