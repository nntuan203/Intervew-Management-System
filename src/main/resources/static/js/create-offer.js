$(document).ready(function () {
    getInterviewData(); // Lấy giá trị của select box khi trang web được tải lên

    $("#interviewId").change(function (event) {
        event.preventDefault();
        let interviewScheduleId = $(this).val();
        $.get("http://localhost:8080/api/offer/" + interviewScheduleId, function (data) {
            $('#status-input').val(data.statusInterview);
            $('#interview-input').val(data.interview);
            $('#note-input').val(data.note);
        });
    });
});

function getInterviewData() {
    let interviewScheduleId = $("#interviewId").val();
    $.get("http://localhost:8080/api/offer/" + interviewScheduleId, function (data) {
        $('#status-input').val(data.statusInterview);
        $('#interview-input').val(data.interview);
        $('#note-input').val(data.note);
    });
}


$(function assignMe() {
    $('#assignMe').on('click', function (event) {
        event.preventDefault();
        let recruiterOwner = document.getElementById("recruiterOwner");

        for (let i = 0; i < recruiterOwner.options.length; i++) {

            if (recruiterOwner.options[i].textContent === username) {
                recruiterOwner.selectedIndex = i;
                break;
            }
        }
        recruiterOwner.dispatchEvent(new Event('change')
        );
    });
});
