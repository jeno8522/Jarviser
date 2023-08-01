//VAD를 적용하고 있는 코드

// import { VAD } from "./vad.js";

let mediaRecorder;
let recordedChunks = [];
let index = 0;
let vad;

window.AudioContext = window.AudioContext || window.webkitAudioContext;
var audioContext = new AudioContext();

function startVAD(stream) {
  navigator.mediaDevices
    .getUserMedia({ audio: true })
    .then((stream) => {
      console.log("startVAD");
      var source = audioContext.createMediaStreamSource(stream);
      var options = {
        source: source,
        voice_stop: function () {
          console.log("voice_stop");
          stopAudio();
        },
        voice_start: function () {
          console.log("voice_start");
          startAudio();
        },
      };
      vad = window.VAD(options);
    })
    .catch((err) => console.log("getUserMedia() failed: ", err));
}

function startAudio() {
  if (!mediaRecorder) {
    navigator.mediaDevices
      .getUserMedia({ audio: true })
      .then((stream) => {
        mediaRecorder = new MediaRecorder(stream);
        mediaRecorder.start();

        mediaRecorder.addEventListener("dataavailable", function (e) {
          if (e.data.size > 0) {
            recordedChunks.push(e.data);
            console.log("pushing..");
          }
        });

        mediaRecorder.addEventListener("stop", function () {
          let blob = new Blob(recordedChunks, { type: "audio/wav" });
          recordedChunks = [];
          sendAudio(blob);
          index++;
        });
      })
      .catch((err) => console.log("getUserMedia() failed: ", err));
  } else {
    mediaRecorder.start();
  }
}

function stopAudio() {
  if (mediaRecorder) {
    mediaRecorder.stop();
  }
}

document.getElementById("vad_start").addEventListener("click", () => {
  navigator.mediaDevices
    .getUserMedia({ audio: true })
    .then((stream) => startVAD(stream))
    .catch((err) => console.log("getUserMedia() failed: ", err));
});
document.getElementById("start").addEventListener("click", startAudio);
document.getElementById("stop").addEventListener("click", stopAudio);

async function sendAudio(blob) {
  try {
    const url = "http://localhost:8081/meeting/transcript";
    const formData = new FormData();
    formData.append("file", blob, "audio" + index + ".wav");
    const response = await fetch(url, {
      method: "POST",
      body: formData,
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    console.log(data.text);
  } catch (error) {
    console.error("Error sending audio", error);
  }
}
