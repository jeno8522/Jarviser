const { SpeechRecorder } = require("speech-recorder");
const { WaveFile } = require("wavefile");

let buffer = [];
const sampleRate = 16000;
const recorder = new SpeechRecorder({
  onChunkStart: ({ audio }) => {
    console.log(Date.now(), "Chunk start");
    buffer = [];
  },
  onAudio: ({ audio, speech }) => {
    if (speech) {
      console.log(Date.now(), "Speech detected");
      for (let i = 0; i < audio.length; i++) {
        buffer.push(audio[i]);
      }
    }
  },
  onChunkEnd: () => {
    console.log(Date.now(), "Chunk end");
    let wav = new WaveFile();
    let buffer2 = buffer;
    wav.fromScratch(1, sampleRate, "16", buffer);
    console.log("sending audio");
    sendAudio(wav.toBase64(), "http://localhost:8080/voice");
  },
});

console.log("Recording for 60 seconds...");
recorder.start();
setTimeout(() => {
  console.log("Done!");
  recorder.stop();
}, 60000);

function sendAudio(data, url) {
  const form = document.getElementById("form");
  form.setAttribute("method", "post");
  form.setAttribute("action", url);

  const audio = document.createElement("input");
  audio.setAttribute("type", "hidden");
  audio.setAttribute("name", "audio");
  audio.setAttribute("value", data);

  form.appendChild(audio);
  document.body.appendChild(form);

  form.submit();
}
