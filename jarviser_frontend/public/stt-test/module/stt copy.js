document.getElementById("vad_start").addEventListener("click", () => {
  navigator.mediaDevices
    .getUserMedia({ audio: true })
    .then((stream) => startVAD(stream)) // Pass the 'stream' to the startVAD function
    .catch((err) => console.log("getUserMedia() failed: ", err));
});

document.getElementById("vad_stop").addEventListener("click", stopVAD);

let mediaRecorder;
let recordedChunks = [];
let vad;
let audioContext;
let time = 0;
let sampleRate;

function startVAD(stream) {
  if (vad) return;
  window.AudioContext = window.AudioContext || window.webkitAudioContext;
  audioContext = new AudioContext();
  let source = audioContext.createMediaStreamSource(stream);
  let startTime = 0;

  let options = {
    source: source,
    voice_start: function () {
      mediaRecorder.start(); // Pass the 'stream' to the startAudio function
      console.log("voice_start");
      startTime = new Date().getMilliseconds();
    },
    voice_stop: function () {
      time = new Date().getMilliseconds() - startTime;
      mediaRecorder.stop();
      mediaRecorder.start();
      time = 0;
      console.log("voice_stop");
    },
  };
  startAudio(stream);
  vad = new window.VAD(options); // Initialize VAD with the correct options
  sampleRate = vad.options.context.sampleRate;
  console.log("startVAD");
}

function stopVAD() {
  vad.stop();
  audioContext.close();
  vad = null;
  console.log("stopVAD");
}

function startRecording(stream) {
  if (!mediaRecorder) {
    mediaRecorder = new MediaRecorder(stream); // Use the 'stream' parameter here

    mediaRecorder.addEventListener("start", function () {});
    mediaRecorder.addEventListener("dataavailable", function (e) {
      if (e.data.size > 0) {
        recordedChunks.push(e.data.slice);
        console.log("pushing..");
      }
    });

    mediaRecorder.addEventListener("stop", function (time) {
      let blob = new Blob(recordedChunks, { type: "audio/wav" });
      recordedChunks = [];
      sendAudio(blob);
    });
    mediaRecorder.start();
  } else {
    mediaRecorder.start();
  }
}

function stopAudio() {
  if (mediaRecorder) {
    mediaRecorder.stop();
    mediaRecorder.start();
  }
}

async function sendAudio(blob) {
  try {
    const url = "http://localhost:8081/audio/transcript";
    const formData = new FormData();
    const testID = "fRsFnxwhA7frdnfFMjNPKA=="; //임시로 넣은 testID
    formData.append("file", blob);
    formData.append("meetingId", testID);
    const response = await fetch(url, {
      method: "POST",
      body: formData,
      headers: { Authorization: "Bearer " + token },
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    console.log(data.text);

    // STT 결과를 <div id="stt-chatting"></div>에 추가하는 부분
    const chatDiv = document.getElementById("stt-chatting");
    const messageDiv = document.createElement("div");
    messageDiv.className = "stt-message"; // CSS 클래스 추가 (필요시 스타일링을 위해)
    messageDiv.textContent = data.text;
    chatDiv.appendChild(messageDiv);
  } catch (error) {
    console.error("Error sending audio", error);
  }
}
