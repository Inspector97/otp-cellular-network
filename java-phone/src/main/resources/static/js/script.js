function poll() {
    $.get("/messages", {}, function (data) {
        $("#messages").html(data.join("<br><br>"));
        console.log(data.join("<br>"));
    });
}

function ready() {
    setInterval(poll, 1000);
    $.get("/getPhoneNumber", {}, function (data) {
        $("#phoneNumber").html("Phone number: " + data);
    });
}

function sendMessage() {
    $.post("/sendMessage", {
        phone: $("#input-phone").val(),
        message: $("#input-message").val()
    });
}

document.addEventListener("DOMContentLoaded", ready);
