import {useState} from "react";
import VideoRoomComponent from "../components/openvidu/VideoRoomComponent";
import registerServiceWorker from "../registerServiceWorker";
import ReactDOM from "react-dom/client";
console.log("방 생성하기 호출!");

function CreateMeeting() {
  return <VideoRoomComponent />;
}
registerServiceWorker();

export default CreateMeeting;
