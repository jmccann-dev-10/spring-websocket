'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var leaveButton = document.querySelector('#leave-button');

var stompClient = null;
var username = null;
var topic = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    event.preventDefault();
    username = document.querySelector('#name').value.trim();
    topic = document.querySelector('#topic').value.trim();

    if(username) {
        
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('http://localhost:8080/ws');
        stompClient = Stomp.over(socket);
        const headers = {"Authorization": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkZXYxMC11c2Vycy1hcGkiLCJzdWIiOiJqb2huc21pdGgiLCJpZCI6Ijk4M2YxMjI0LWFmNGYtMTFlYi04MzY4LTAyNDJhYzExMDAwMiIsImZpcnN0X25hbWUiOiJKb2huIiwibGFzdF9uYW1lIjoiU21pdGgiLCJlbWFpbF9hZGRyZXNzIjoiam9obkBzbWl0aC5jb20iLCJtb2JpbGVfcGhvbmUiOiI1NTUtNTU1LTU1NTUiLCJyb2xlcyI6IkFETUlOIiwiZXhwIjoxNjMyMzE2NDU4fQ.XHPFe_mVYGN8zelkQJt0VtsZ1oyEanLF2cSExNnY9wI"}
        stompClient.connect(headers, onConnected, onError);
    }
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe(`/topic/${topic}`, onMessageReceived);

    // Tell your username to the server
    stompClient.send(`/app/${topic}/chat.addUser`,
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    connectingElement.classList.add('hidden');
}


function onError(error) {
    console.log(error);
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    event.preventDefault();
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };
        stompClient.send(`/app/${topic}/chat.sendMessage`, {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
}

function leaveChat() {
    stompClient.disconnect(onDisconect);
}

function onDisconect() {
    usernamePage.classList.remove("hidden");
    chatPage.classList.add('hidden');
    messageArea.innerHTML = "";
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = `${message.sender} joined ${topic}!` ;
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)
leaveButton.addEventListener('click', leaveChat, true)
window.addEventListener('beforeunload', leaveChat, true)