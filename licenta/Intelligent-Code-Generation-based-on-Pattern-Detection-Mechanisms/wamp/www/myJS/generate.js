function populateProgram1(){
    getText("file/file1", file1Success, error);
}
function file1Success(data){
    data = data.replace(/ /g,"&nbsp;");
    var lines = data.split("\n");
    var table = $(".file1 tbody");
    $(".file1 .title").html(lines[0]);
    for(var i = 1; i < lines.length; i++){
        table.append(tableRow(i, lines[i]));
    }

    $(".file1 tbody").sortable(
        {
            group: 'draggable',
            drop: false,
            onDrop: function  (item, targetContainer, _super) {
                updateTemplateLines();
                _super(item, targetContainer);
            }
        });
}


function populateProgram2(){
    getText("file/file2", file2Success, error);
}
function file2Success(data){
    data = data.replace(/ /g,"&nbsp;");
    var lines = data.split("\n");
    var table = $(".file2 tbody");
    $(".file2 .title").html(lines[0]);
    for(var i = 1; i < lines.length; i++){
        table.append(tableRow(i, lines[i]));
    }

    $(".file2 tbody").sortable(
        {
            group: 'draggable',
            drop: false,
            onDrop: function  (item, targetContainer, _super) {
                updateTemplateLines();
                _super(item, targetContainer);
            }
        });
}

function generateProgram(){
    getText("file/generate", generateSuccess, error);
}

function generateSuccess(data){
    data = data.replace(/ /g,"&nbsp;");
    var lines = data.split("\n");
    var table = $(".generate tbody");
    if ( lines[0] != '' )
        $(".generate .templateName").val(lines[0]);
    for(var i = 1; i < lines.length; i++){
        table.append(tableRow(i, lines[i]));
    }

    addButtonShowHide(".generate");
    $(".generate tbody").sortable(
        {
            group: 'draggable',
            onDrop: function  (item, targetContainer, _super) {
                updateTemplateLines();
                _super(item, targetContainer);
            }
        });
    makeTemplateLinesEditable();

}

function updateTemplateName(){
    var name = $(".generate .templateName").val();
    getText("save/templateName/" + name, updateSavedName, errorSave);
}

function errorSave(){
    $(".templateName").addClass("notSaved");
}

function updateSavedName(){
    updateTemplateLines();

    $(".templateName").removeClass("notSaved");
    $(".templateName").addClass("saved100");
    setTimeout(function() {
        $(".templateName")
            .removeClass("saved100")
            .addClass("saved75");
        setTimeout(function() {
            $(".templateName")
                .removeClass("saved75")
                .addClass("saved50");
            setTimeout(function() {
                $(".templateName")
                    .removeClass("saved50")
                    .addClass("saved25");
                setTimeout(function() {
                    $(".templateName")
                        .removeClass("saved25");
                }, 300);
            }, 300);
        }, 300);
    }, 300);
}

function getText(path, callbackSuccess, callbackError){
    $.ajax({
        type: "GET",
        url: path,
        success: function(data){
            callbackSuccess(data);
        },
        error: function(data) {
            debugger;
            callbackError(data);
        }
    });

}

function error(data){
    console.log("Error: "+ data.statusText.toUpperCase());
}

var lastId;
function addButtonShowHide(file){
    $(file + " tr").each(function (e, el){
        $(el).unbind('mouseover mouseout');

        $(el).mouseover(function(){
            $(el).find(".remove").show();
            var td = $(el).find("td").eq(0);
            if ( td.html().indexOf(".") != -1 ){
                lastId  = td.html();
                td.html('&#187;');
                td.addClass("fontSize25");
//                console.log("in");
            }
        }).mouseout(function(){
//                console.log("out");
                $(el).find(".remove").hide();
                var td = $(el).find("td").eq(0);
                td.html(lastId);

                td.removeClass("fontSize25");
            });
    });


}


function tableRow(index, text){

    return '<tr>' +
        '    <td style="width: 30px; height: 20px;">' +
        index +
        '.</td>' +
        '   <td style="display: block; width: 345px; height: 20px; overflow: hidden; position: relative; top: 7px;">' +
        text +
        '   </td>' +
        '   <td style="width: 50px;">' +
        '       <input type="submit" class="button remove" value="Remove" style="width: 65px; font-size: 12px; display: none; " onclick="removeLine(this)">' +
        '       </td>' +
        '   </tr>';

}


function removeLine(data){
    $(data).closest("tr").remove();
    updateTemplateLines();
}