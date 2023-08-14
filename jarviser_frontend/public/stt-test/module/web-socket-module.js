import SockJS from "sockjs-client";
import Stomp from "stompjs";
document.getElementById("text-send").addEventListener("click", function (e) {
  e.preventDefault();
  const userText = document.getElementById("text-input").value;
  sendChat(userText);
});

var meetingId = 3; //FIXME: 회의 ID 설정하기 - 암호화된 회의의 id를 지정한다.
var token = localStorage.getItem("access-token");
var socket = new SockJS("http://localhost:8081/ws");
var stompClient = Stomp.over(socket);

const stt = [];
const chat = [];
const participants = [];
const message = [];
stompClient.connect({}, function (frame) {
  stompClient.subscribe("/meeting/" + meetingId, function (messageOutput) {
    let message = JSON.parse(messageOutput.body);
    let type = message.type;
    Received[type](message);
    console.log(stt);
    console.log(chat);
    console.log(participants);
    document.getElementById("response").innerText = "Received: " + messageOutput.body;
  });
});

const Received = {
  chat: receivedChat,
  stt: receivedStt,
  participants: receivedParticipants,
  connect: receivedConnect,
  disconnect: receivedDisConnect,
  error: receivedError,
};

function receivedChat(data) {
  let userId = data.userId;
  let userName = data.userName;
  let time = data.time;
  let content = data.content;
  chat.push([userId, userName, content, time]);
}
function receivedStt(data) {
  let userId = data.userId;
  let userName = data.userName;
  let time = data.time;
  let content = data.content;
  stt.push([userId, userName, content, time]);
}
function receivedParticipants(data) {
  let content = data.content;
  content.forEach((element) => {
    participants.push(element);
  });
}
function receivedConnect(data) {
  let userId = data.userId;
  let userName = data.userName;
  let time = data.time;
  let content = data.content;
  participants.push([userId, userName]);
  message.push([content, time]);
}

function receivedDisConnect(data) {
  let userId = data.userId;
  let userName = data.userName;
  let time = data.time;
  let content = data.content;
  participants.pop([userId, userName]);
  message.push([content, time]);
}
function receivedError(data) {
  let content = data.content;
  let time = data.time;
  message.push([content, time]);
}

function sendChat(chatText) {
  const messageObject = {
    content: chatText,
    Authorization: token,
  };
  stompClient.send("/app/chat", {}, JSON.stringify(messageObject));
}

//TODO: mute 등 명령을 보내는 함수를 구현하기
// function sendCommand(command) {
//   const messageObject = {
//     content: command,
//     Authorization: token,
//   };
//   stompClient.send("/app/chat", {}, JSON.stringify(messageObject));
// }