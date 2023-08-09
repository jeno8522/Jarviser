//vad 적용 코드

let mediaRecorder;
let recordedChunks = [];
let index = 0;
let vad;
let stream;

window.AudioContext = window.AudioContext || window.webkitAudioContext;
var audioContext = new AudioContext();

function startVAD(stream) {
  var source = audioContext.createMediaStreamSource(stream);
  var options = {
    source: source,
    voice_stop: function () {
      stopAudio();
      console.log("voice_stop");
    },
    voice_start: function () {
      startAudio(stream); // Pass the 'stream' to the startAudio function
      console.log("voice_start");
    },
  };
  vad = new window.VAD(options); // Initialize VAD with the correct options
  vad.start = true; // Start VAD
  console.log("startVAD");
}

function startAudio(stream) {
  if (!mediaRecorder) {
    mediaRecorder = new MediaRecorder(stream); // Use the 'stream' parameter here

    mediaRecorder.addEventListener("start", function () {});
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
    mediaRecorder.start();
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
    .then((stream) => startVAD(stream)) // Pass the 'stream' to the startVAD function
    .catch((err) => console.log("getUserMedia() failed: ", err));
});

async function sendAudio(blob) {
  try {
    const url = "http://localhost:8081/meeting/transcript"; //backend api url 넣기
    const formData = new FormData();
    const testID = 3; //임시로 넣은 testID
    formData.append("file", blob, "audio" + index + ".wav");
    formData.append("meetingId", testID);
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
