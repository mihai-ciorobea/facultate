function updateTemplateLines(){
    var text = "";
    $(".generate tr").each(function(id, el){
        //$(".generate tr").eq(8).find('td').eq(1).find('input').val()
        var td = $(el).find('td').eq(1);
        var line;
        if ( td.find('input').length == 1 )
            line = td.find('input').val();
        else
            line = td.html();
        line = line.replace(/&nbsp;/g, " ");

        text += line + "\n";
    });

    addButtonShowHide(".generate");
    makeTemplateLinesEditable();
    POST(updateLinesUrl, text, function(){}, function(data){console.log("Error when saving the template.");});
}

var updateLinesUrl = "save/makeFinal";

function POST(url, data, successCallback, errorCallback){
    $.ajax({
        type: "post",
        url: url,
        contentType: 'application/json',
        dataType: 'JSON',
        data: JSON.stringify(data),
        success: function(){successCallback();},
        error: function(data) {errorCallback(data);}
    });
}

var oldText;
function makeTemplateLinesEditable(){
    $(".generate tr").each(function(id, tr){
        $(tr).unbind('dblclick');

        $(tr).dblclick(function(a,el){
            var td = $(tr).find("td").eq(1);
            if ( td.find('input').length == 1 )
                oldText = td.find("input").val();
            else
                oldText = td.html();

            td.html('<input class="edit" type="text" onfocus="editLine(this);">');
            td.find("input").val(oldText.replace(/&nbsp;/g, " "));
            td.find("input").focus();
        });

        $(tr).click(function(){
            var el = document.activeElement;
            if (  $(el).is("input") ){
                var newText = $(el).val().replace(/ /g,"&nbsp;");
                $(el).closest("td").html(newText);
                $(el).blur();
                updateTemplateLines();
            }

        })
    });
}

function editLine(el){
    $(el).keyup(function(e){
        if(e.keyCode == 13)//enter
        {
            var newText = $(el).val().replace(/ /g,"&nbsp;");
            var td = $(el).closest("td");
            td.html(newText);
            updateTemplateLines();
            oldText = undefined;
            td.closest("tr").after(
                '<tr>' +
                    '<td style="width: 30px; height: 20px;" class="">' +
                    '</td>' +
                    '<td>' +
                    '<input class="edit" type="text" onfocus="editLine(this);">' +
                    '</td>' +
                    '<td style="width: 50px;">' +
                    '   <input type="submit" class="button remove" value="Remove" style="width: 65px; font-size: 12px; display: none;" onclick="removeLine(this)">' +
                    '</td>' +
                '</tr>');


            td.closest('tr').next().find('input').eq(0).focus();
            addButtonShowHide(".generate");

        }
        if(e.keyCode == 27)//esc
        {
            if ( oldText == undefined ){
                $(el).closest("tr").remove();
            } else {
                $(el).closest("td").html(oldText);
            }
        }
    });
}





