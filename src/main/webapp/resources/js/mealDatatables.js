var ajaxUrl = "ajax/profile/meals/";



$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ]

    });
    makeEditable();
});

function filter() {
    var filterParams = $("#filterForm");
    $.ajax({
        type: "GET",
        url: ajaxUrl+"filter",
        data: filterParams.serialize(),
        success: function (data) {
            filterForMealsNeeded = true;
            datatableApi.clear().rows.add(data).draw();
            successNoty("Filtered by ajax");

        }
    })

}

function resetFilter() {
    filterForMealsNeeded = false;
    updateTable();
}