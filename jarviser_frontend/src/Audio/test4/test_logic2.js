//VAD를 적용하기 전 단계의 통신을 재구현한 코드
let mediaRecorder;
let recordedChunks = [];
let index = 0;

document.getElementById("start").addEventListener("click", function () {
  navigator.mediaDevices
    .getUserMedia({ audio: true })
    .then((stream) => {
      mediaRecorder = new MediaRecorder(stream);
      mediaRecorder.start();

      mediaRecorder.addEventListener("dataavailable", function (e) {
        if (e.data.size > 0) {
          recordedChunks.push(e.data);
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
});

document.getElementById("stop").addEventListener("click", function () {
  if (mediaRecorder) {
    mediaRecorder.stop();
  }
});

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
