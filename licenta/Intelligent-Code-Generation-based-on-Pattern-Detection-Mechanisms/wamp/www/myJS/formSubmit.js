function generate(){
    var file1 = $('#fakeUpload1').val();
    var file2 = $('#fakeUpload2').val();
    var wordCount = $("#wordCount").is(":checked");
    var nameSimilarity = $('#nameSimilarity').val();
    var stackSimilarity = $('#stackSimilarity').val();
    var before = $("#before").is(":checked");
    var after = $("#after").is(":checked");


    $.ajax({
        type: "post",
        url: "setSettings",
        contentType: 'application/json',
        dataType: 'JSON',
        data: JSON.stringify(
            {
                File1: file1,
                File2: file2,
                WordCount: wordCount,
                NameSimilarity: nameSimilarity,
                StackSimilarity: stackSimilarity,
                Before: before,
                After: after
            }),
        success: function(data){
            self.location = "generate";
        },
        error: function(data) {
            errorForm("generate", data);
        }
    });
}


function instantiate(){
    get("final/setNameWithOutSave/" + $("#fakeUploadInstantiate").val(), function(data){
        self.location = "instantiate";
    }, function (data) {
        errorForm("instantiate", data)
    });
    return false;
}


function errorForm(formId, error){
    $("#"+formId+" .error").html(error.statusText.toUpperCase())
    $("#"+formId+" .error").fadeOut(3000,
        function() {
            $("#"+formId+" .error").html("");
            $("#"+formId+" .error").show();
        }
    )
}

function get(path, callbackSuccess, callbackError){
    $.ajax({
        type: "GET",
        url: path,
        success: function(data){
            callbackSuccess(data);
        },
        error: function(data) {
            callbackError(data);
        }
    });

}