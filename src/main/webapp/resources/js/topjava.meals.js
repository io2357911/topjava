var ctx;
var mealAjaxUrl = "profile/meals/";

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$.ajaxSetup({
    converters: {
        "text json": function (stringData) {
            let json = JSON.parse(stringData);
            $(json).each(function () {
                this.dateTime = this.dateTime.replace('T', ' ').substr(0, 16);
            });
            return json;
        }
    }
});

$(function () {
    ctx = {
        ajaxUrl: mealAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
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
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data) {
                $(row).attr("data-mealExcess", data.excess);
            }
        }),
        updateTable: function () {
            $.ajax({
                type: "GET",
                url: mealAjaxUrl + "filter",
                data: $("#filter").serialize()
            }).done(updateTableByData);
        }
    };
    makeEditable();
});

$('.datePicker').datetimepicker({
    timepicker: false,
    format: 'Y-m-d',
    formatDate: 'Y-m-d',
});

$('.timePicker').datetimepicker({
    datepicker: false,
    format: 'H:i',
});

$('#dateTime').datetimepicker({
    format: 'Y-m-d H:i'
});