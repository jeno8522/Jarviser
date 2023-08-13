//FIXME : 추후 import로 대체 현재 html에서 불러오고 있음.
// import SockJS from "sockjs-client";
// import Stomp from "stompjs";
document.getElementById("text-form").addEventListener("submit", function (e) {
  e.preventDefault();
  const userText = document.getElementById("text-input").value;
  uploadText(userText);
});

var token = localStorage.getItem("access-token");
var meetingId = 3; //FIXME: 회의 ID 설정하기 - 암호화된 회의의 id를 지정한다.
const Received = {
  chat: receivedChat,
  stt: receivedStt,
  comeinsession: receivedComeIn,
  leavesession: receivedLeave,
};
var socket = new SockJS("http://localhost:8081/ws");
var stompClient = Stomp.over(socket);
stompClient.connect({"Authorization":"Bearer "+token}, function (frame) {
  stompClient.subscribe("/topic/" + meetingId, function (messageOutput) {
    let message = JSON.parse(messageOutput.body);
    let type = message.type;
    Received[type](message);
    console.log(stt);
    console.log(chat);
    console.log(participants);
    document.getElementById("response").innerText = "Received: " + messageOutput.body;
  });
});

const stt = [];
const chat = [];
const participants = [];
function receivedChat(data) {
  chat.push([data.userId, data.content]);
}
function receivedStt(data) {
  stt.push([data.userId, data.content]);
}
function receivedComeIn(data) {
  participants.push(data.userId);
}
function receivedLeave(data) {
  const index = participants.indexOf(data.userId);
  if (index > -1) {
    participants.splice(index, 1);
  }
}
function getUserIdFromToken(token) {
  const payload = token.split(".")[1];
  const decodedPayload = atob(payload);
  const jsonPayload = JSON.parse(decodedPayload);
  console.log("jsonpayload == ", jsonPayload);
  return jsonPayload.userId;
}

function uploadText(userText) {
  var token = localStorage.getItem("access-token");
  var serverUrl = "http://localhost:8081/app/message"; // 서버의 URL

  var formData = new FormData();
  formData.append("meetingId", meetingId);
  formData.append("userId", userId);
  formData.append("content", userText);

  fetch(serverUrl, {
    method: "POST",
    body: formData,
    headers: {
      Authorization: "Bearer " + token,
    },
  })
    .then((response) => response.json())
    .then((data) => {
      console.log(data);
    });
}
