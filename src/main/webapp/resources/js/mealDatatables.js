const ajaxUrl = "ajax/profile/meals/";
let datatableApi;

function updateTable() {
    $.ajax({
        type: "GET",
        url: ajaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(ajaxUrl, updateTableByData);
}

$.ajaxSetup({
    converters:{
        "text json": function(jsonString){
            let json = JSON.parse(jsonString);
            $(json).each(function () {
                this.dateTime = this.dateTime.replace('T', ' ').substring(0, 16);
            });
            return json;
        }
    }

});

$(function () {
    datatableApi = $("#datatable").DataTable({
        "ajax": {
            "url": ajaxUrl,
            "dataSrc": ""

        },
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
                // "render": function (date, type, row) {
                //     if (type === "display") {
                //         return date.replace('T', ' ').substring(0, 16);
                //     }
                //     return date;
                // }
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "render": renderEditBtn,
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "render": renderDeleteBtn,
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ],
        "createdRow": function (row, data, dataIndex) {
            if (!data.enabled) {
                $(row).attr("data-mealExceed", data.exceed);
            }
        }

    });
    makeEditable();
});

// $.datetimepicker.setLocale('ru');


$('#endDate').datetimepicker({
    format: 'Y-m-d',
    theme: 'dark',
    timepicker: false
});

$('#startDate').datetimepicker({
    format: 'Y-m-d',
    theme: 'dark',
    timepicker: false
});

$('#startTime, #endTime').datetimepicker({
    datepicker: false,
    format: 'H:i',
    theme: 'dark'
});

$('#dateTime').datetimepicker({
    format: 'Y-m-d\\TH:i',
    theme: 'dark'
});


