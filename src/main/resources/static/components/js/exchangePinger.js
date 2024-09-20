$(document).ready(function() {

    console.log("Script loaded");
    if($.ajax){
        console.log("Ajax ready");
    }else{
        console.log("Ajaxa ti gde??????");
    }

    $('#ping_btn').on('click', function(event) {

        const fromCurrencyId = $('#fromCurrencyId').val();
        const toCurrencyId = $('#toCurrencyId').val();
        const amount = $('#amount').val();

        $.ajax({
            url: '/api/calculation',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({amount, fromCurrencyId, toCurrencyId}),
            success: function(response) {
                console.log(response.message);
            },
            error: function(error) {
                console.error('Error:', error);
            }
        });
    });
});