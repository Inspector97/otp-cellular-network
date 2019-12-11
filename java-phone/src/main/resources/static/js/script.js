function poll() {
    $.get("/messages", {}, function (data) {
        $("#students").html(data);
    });
}

function ready() {
    setInterval(poll, 250)

}

function sendMessage() {
    $.post("/sendMessage");
}

document.addEventListener("DOMContentLoaded", ready);