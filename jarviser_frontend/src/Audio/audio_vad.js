/*
현재 node audio_vad.js로 실행시 서버로 wav 파일 자동 전송
이를 web 환경으로 이전 필요
*/

const { SpeechRecorder } = require("speech-recorder"); //오디오 녹음을 위한 speech-recorder 모듈
const { WaveFile } = require("wavefile"); //오디오 파일을 위한 wavefile 모듈
const FormData = require("form-data"); //오디오 파일을 서버로 전송하기 위한 form-data 모듈
const fetch = require("node-fetch"); //오디오 파일을 서버로 전송하기 위한 node-fetch 모듈
const { Readable } = require("stream"); //오디오 파일을 서버로 전송하기 위한 stream 모듈

let buffer = []; //오디오 데이터를 저장할 배열
//FIXME:서버에서 index를 처리하도록 해야 서버에 같은 이름으로 저장하지 않음.
let index = 0; //오디오 파일 이름에 사용할 인덱스
const sampleRate = 16000; //오디오 샘플링 레이트
const recorder = new SpeechRecorder({
  //발화가 시작되었을 때 호출되는 콜백함수
  onChunkStart: ({ audio }) => {
    console.log(Date.now(), "Chunk start");
    buffer = [];
  },
  //오디오가 입력 중일 때 실행되는 콜백 함수 speech가 true일 때만 처리
  onAudio: ({ audio, speech }) => {
    if (speech) {
      //speech가 true일 때만 buffer에 오디오 데이터를 저장
      for (let i = 0; i < audio.length; i++) {
        buffer.push(audio[i]);
      }
    }
  },
  //발화가 종료되었을 때 호출되는 콜백함수
  onChunkEnd: () => {
    console.log(Date.now(), "Chunk end");
    let wav = new WaveFile();
    let buffer2 = buffer;
    index++;
    wav.fromScratch(1, sampleRate, "16", buffer2);
    console.log("sending audio" + index);
    sendAudio(wav.toBuffer())
      .then((response) => response.json()) // if the response is JSON
      .then((data) => {
        console.log(data.text);
      })
      .catch((error) => console.error("Error sending audio", error));
  },
});

console.log("Recording for 60 seconds...");
recorder.start();
setTimeout(() => {
  console.log("Done!");
  recorder.stop();
}, 1000 * 60 * 60);

async function sendAudio(data) {
  try {
    const url = "http://localhost:8081/meeting/transcript";
    const formData = new FormData();
    const bufferStream = new Readable();
    bufferStream.push(data);
    bufferStream.push(null);

    formData.append("file", bufferStream, { filename: "audio" + index + ".wav" });
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
