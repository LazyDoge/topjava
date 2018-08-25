var filterData;

function makeEditable() {
    $(".deleteMeal").click(function () {
        deleteRow($(this).parents("tr").attr("id"));
        // console.log($(this).parents("tr").attr("id"));
    });

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({cache: false});
}

function deleteItem(id) {
    deleteRow(id);
    // console.log(id);
}

function filter() {
    var form = $("#filterForm");
    $.ajax({
        type: "GET",
        url: ajaxUrl+"filter",
        data: form.serialize(),
        success: function (data) {
            filterData = data;
            datatableApi.clear().rows.add(data).draw();
            successNoty("Filtered by ajax")

        }
    })

}

function add() {
    $("#detailsForm").find(":input").val("");
    $("#editRow").modal();
}

function deleteRow(id) {
    $.ajax({
        url: ajaxUrl + id,
        type: "DELETE",
        success: function () {
            updateTable();
            successNoty("Deleted");
        }
    });
}

function resetFilter() {
    filterData = null;
    updateTable();
}

function updateTable() {
    $.get(ajaxUrl, function (data) {
        if (filterData == null) {
            filterData = data;
        }
        datatableApi.clear().rows.add(filterData).draw();
    });
}

function save() {
    var form = $("#detailsForm");
    $.ajax({
        type: "POST",
        url: ajaxUrl,
        data: form.serialize(),
        success: function () {
            $("#editRow").modal("hide");
            updateTable();
            successNoty("Saved");
        }
    });
}

var failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(text) {
    closeNoty();
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + text,
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}

function failNoty(jqXHR) {
    closeNoty();
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;Error status: " + jqXHR.status,
        type: "error",
        layout: "bottomRight"
    }).show();
}