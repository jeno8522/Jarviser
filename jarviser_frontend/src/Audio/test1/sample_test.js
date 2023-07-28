/*
speech-recorder 샘플 코드
현재 말을 하고 있는지 아닌지를 체크하여,
말이 시작될 때 onChunkStart,
현재 오디오가 연결 중일 때 onAudio,
말이 끝났을 때 onChunkEnd를 호출한다.

node audio_test.js 를 통해 테스트해볼 수 있다.
*/

const { SpeechRecorder } = require("speech-recorder");

const recorder = new SpeechRecorder({
  onChunkStart: ({ audio }) => {
    console.log(Date.now(), "Chunk start");
  },
  onAudio: ({ speaking, probability, volume }) => {
    console.log(Date.now(), speaking, probability, volume);
  },
  onChunkEnd: () => {
    console.log(Date.now(), "Chunk end");
  },
  sileroVadSpeakingThreshold: 0.3,
  sileroVadSilenceThreshold: 0.1,
});

console.log("Recording for 60 seconds...");
recorder.start();
setTimeout(() => {
  console.log("Done!");
  recorder.stop();
}, 60000);
