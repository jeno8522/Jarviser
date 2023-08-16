async function main() {
  const myvad = await vad.MicVAD.new({
    onSpeechStart:() =>{
      console.log('speech start')
    },
    onSpeechEnd: (audio) => {
      console.log('speech end')
      // do something with `audio` (Float32Array of audio samples at sample rate 16000)...
      console.log("start")
      const audioBlob = float32ArrayToWav(audio, 16000);
      console.log("end")
      sendAudio(audioBlob);
    },
  })
  myvad.start()
}
main()

function float32ArrayToWav(audioData, sampleRate) {
const buffer = new ArrayBuffer(44 + audioData.length * 2);
const view = new DataView(buffer);

function writeString(view, offset, string) {
  for (let i = 0; i < string.length; i++) {
    view.setUint8(offset + i, string.charCodeAt(i));
  }
}

// RIFF header
writeString(view, 0, 'RIFF');
view.setUint32(4, 32 + audioData.length * 2, true);
writeString(view, 8, 'WAVE');

// fmt chunk
writeString(view, 12, 'fmt ');
view.setUint32(16, 16, true);
view.setUint16(20, 1, true); // PCM format
view.setUint16(22, 1, true); // mono
view.setUint32(24, sampleRate, true);
view.setUint32(28, sampleRate * 2, true);
view.setUint16(32, 2, true);
view.setUint16(34, 16, true);

// data chunk
writeString(view, 36, 'data');
view.setUint32(40, audioData.length * 2, true);

const volume = 1;
let index = 44;
for (let i = 0; i < audioData.length; i++) {
  view.setInt16(index, audioData[i] * (0x7FFF * volume), true);
  index += 2;
}

return new Blob([view], { type: 'audio/wav' });
}

async function sendAudio(blob) {
try {
  let token = localStorage.getItem("access-token");
  const url = "http://localhost:8081/audio/transcript";
  const formData = new FormData();
  const testID = "fRsFnxwhA7frdnfFMjNPKA=="; //임시로 넣은 testID
  formData.append("file", blob);
  formData.append("meetingId", testID);
  const response = await fetch(url, {
    method: "POST",
    body: formData,
    headers: {Authorization: "Bearer " + token},
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
