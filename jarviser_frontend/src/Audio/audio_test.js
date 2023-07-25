const { SpeechRecorder } = require("speech-recorder");
const fs = require("fs");

// Create a write stream to save the audio data
const writeStream = fs.createWriteStream("output_audio.raw");

// Create a new SpeechRecorder instance with appropriate callbacks
const recorder = new SpeechRecorder({
  onChunkStart: ({ audio }) => {
    console.log(Date.now(), "Chunk start");
  },
  onAudio: ({ audio }) => {
    // Write the audio data to the file as it comes in
    writeStream.write(audio);
  },
  onChunkEnd: () => {
    console.log(Date.now(), "Chunk end");
  },
});

// Start recording
console.log("Recording for 5 seconds...");
recorder.start();

// Stop recording after 5 seconds
setTimeout(() => {
  console.log("Done!");
  recorder.stop();
  writeStream.end(); // Close the write stream when recording is done
}, 5000);
