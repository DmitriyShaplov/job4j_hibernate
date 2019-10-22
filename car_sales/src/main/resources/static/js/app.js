let stompClient = null;

function connect() {
    let socket = new SockJS('../websocket');
    stompClient = Stomp.over(socket);
    let id = $("#item-id").text();
    let headers = {};
    let tokEl = $("[name='_csrf']");
    let headerName = tokEl.prop("name");
    let token = tokEl.val();
    headers[headerName] = token;
    stompClient.connect(headers, function(frame) {
        stompClient.subscribe('/chat/' + id + '/save', function (msg) {
            addMessage(JSON.parse(msg.body));
        });
        stompClient.subscribe('/chat/' + id + '/users', function (msg) {
            userControl(JSON.parse(msg.body));
        })
    });
}

function userControl(message) {
    if (message.state === "JOIN") {
        $("#chat").append(
            "<div class='col-12' style='text-align: center'>" + message.username + " has joined the chat.</div>"
        );
    } else if (message.state === "DISCONNECT") {
        $("#chat").append(
            "<div class='col-12' style='text-align: center'>" + message.username + " has left the chat.</div>"
        );
    }
    $("#users").text(
        "Users online: " + message.users.join(", ")
    );
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}

function sendMessage() {
    let text = $("#text");
    if (!!text.val().trim()) {
        stompClient.send(
            "/app/chat/" + $("#item-id").text() + "/save", {},
            JSON.stringify(
                {
                    'text': text.val()
                }
            )
        );
    }
}

function addMessage(message) {
    let chat = $("#chat");
    chat.append(
        "<div class='col-2'><b>" + message.name + ": </b></div>" +
        "<div class='col-10' style='word-wrap: break-word'>" + message.text + "</div>"
    );
    chat.stop().animate({
        scrollTop: chat[0].scrollHeight
    }, 1000);
}

$(function () {
    $("form").on('submit', function(e) {
        e.preventDefault();
    });
    connect();
    $("#send").click(
        function () {
            sendMessage();
            $("#text").val("");
        }
    )
});
