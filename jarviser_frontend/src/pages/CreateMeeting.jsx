import { useState } from "react";
import VideoRoomComponent from "../components/openvidu/VideoRoomComponent";
import registerServiceWorker from "../registerServiceWorker";
import ReactDOM from "react-dom/client";

function CreateMeeting() {
  return <VideoRoomComponent />;
}
registerServiceWorker();

export default CreateMeeting;
