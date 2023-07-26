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
});


// Variables for recording and audio stream
let mediaRecorder;
let recordedChunks = [];

// Get access to the microphone and start recording
async function startRecording() {
    try {
        recorder.start();
        const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
        mediaRecorder = new MediaRecorder(stream);

        // Event handlers for mediaRecorder
        mediaRecorder.ondataavailable = handleDataAvailable;
        mediaRecorder.onstop = handleRecordingStop;

        // Start recording
        mediaRecorder.start();

        // Disable/enable buttons accordingly
        document.getElementById("startButton").disabled = true;
        document.getElementById("stopButton").disabled = false;
    } catch (error) {
        console.error("Error accessing microphone:", error);
    }
}

// Handle recorded audio data
function handleDataAvailable(event) {
    if (event.data.size > 0) {
        recordedChunks.push(event.data);
    }
}

// Stop recording and create audio file to download
function stopRecording() {
    mediaRecorder.stop();
    recorder.stop();
}

// When recording is stopped, create audio file and enable buttons
function handleRecordingStop() {
    const audioBlob = new Blob(recordedChunks, { type: 'audio/wav' });
    recordedChunks = [];

    // Create a URL for the audio file and enable download link
    const audioURL = URL.createObjectURL(audioBlob);
    const downloadLink = document.getElementById("downloadLink");
    downloadLink.href = audioURL;
    downloadLink.download = "recorded_audio.wav";
    downloadLink.style.display = "block";

    // Disable/enable buttons accordingly
    document.getElementById("startButton").disabled = false;
    document.getElementById("stopButton").disabled = true;
}

// Event listeners for buttons
document.getElementById("startButton").addEventListener("click", startRecording);
document.getElementById("stopButton").addEventListener("click", stopRecording);