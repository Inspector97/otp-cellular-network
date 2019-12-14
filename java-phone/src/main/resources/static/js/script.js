function poll() {
    $.get("/messages", {}, function (data) {
        $("#students").html(data);
    });
}

function ready() {
    setInterval(poll, 250);
    $.get("/getPhoneNumber", {}, function (data) {
        $("#phoneNumber").html("Phone number: " + data);
    });
}

function sendMessage() {
    $.post("/sendMessage");
}

document.addEventListener("DOMContentLoaded", ready);