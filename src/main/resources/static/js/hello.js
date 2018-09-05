$(document).ready(function () {
    jQuery('.search').on('input', function() {
        alert("huy");
    });


    $.ajax({
        type: 'GET',
        url: '/notes',
        data: {
            id: 'id',
            title: 'title',
            text: 'text'
        },
        dataType: 'json',
        success: function (data) {
            $.each(data, function (index, element) {
                $( ".note-id" ).append(element.id);
                $( ".note-title" ).append(element.title);
                $( ".note-text" ).append(element.text);
            });
        }
    });
});