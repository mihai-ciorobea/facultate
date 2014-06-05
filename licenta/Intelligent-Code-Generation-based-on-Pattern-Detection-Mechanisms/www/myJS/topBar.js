$(document).ready(function(){
    $("input[id='defaultProgram']").change(function(){
        if ( $("input[id='defaultProgram']").is(":checked") ){
            get("save/defaultProgram/1",function(data){$(".generate tr").remove();generateSuccess(data);}, error);
        } else {
            get("save/defaultProgram/2",function(data){$(".generate tr").remove();generateSuccess(data);}, error);
        }
    });
});


function get(url, success, error){
    getText(url, success, error);
}


function save(){
	if ( $(".templateName").val() == "Template name.." ){
		showError("templateName", "Please set a name to the final template.");
		return false;
	}

	get("final/save", function(data){
		goToNextPage("instantiate");
	}, function(data){
		showError("save", "An error has accourd.");
	});
   


	
	return true;
}

function showError (type, message) {
	alert('Error: ' + type + " = " + message);

}

function goToNextPage(url){
	window.location.href=url;
	
}