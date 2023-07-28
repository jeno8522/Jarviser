const { SpeechRecorder } = require("speech-recorder");
const { WaveFile } = require("wavefile");
const FormData = require("form-data");
const fetch = require("node-fetch");

let buffer = [];
const sampleRate = 16000;
const recorder = new SpeechRecorder({
  onChunkStart: ({ audio }) => {
    console.log(Date.now(), "Chunk start");
    buffer = [];
  },
  onAudio: ({ audio, speech }) => {
    if (speech) {
      for (let i = 0; i < audio.length; i++) {
        buffer.push(audio[i]);
      }
    }
  },
  onChunkEnd: () => {
    console.log(Date.now(), "Chunk end");
    let wav = new WaveFile();
    let buffer2 = buffer;
    wav.fromScratch(1, sampleRate, "16", buffer2);
    console.log("sending audio");
    sendAudio(wav.toBase64())
      .then((response) => console.log("Response received", response))
      .catch((error) => console.error("Error sending audio", error));
  },
});

console.log("Recording for 60 seconds...");
recorder.start();
setTimeout(() => {
  console.log("Done!");
  recorder.stop();
}, 60000);

async function sendAudio(data) {
  try {
    const url = "http://localhost:8081/meeting/transcript";
    const formData = new FormData();
    formData.append("file", data, "audio.wav");
    const response = await fetch(url, {
      method: "POST",
      body: formData,
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return response;
  } catch (error) {
    console.error("Error sending audio", error);
  }
}
