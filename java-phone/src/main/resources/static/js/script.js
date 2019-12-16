function poll() {
    $.get("/messages", {}, function (data) {
        $("#messages").html(data.join("<br><br>"));
        console.log(data.join("<br>"));
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