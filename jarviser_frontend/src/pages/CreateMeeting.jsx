// import {useState} from "react";
// import VideoRoomComponent from "../components/openvidu/VideoRoomComponentid";
// // import registerServiceWorker from "../registerServiceWorker";
// import ReactDOM from "react-dom/client";
// console.log("방 생성하기 호출!");

// function CreateMeeting() {
//   return <VideoRoomComponent />;
// }
// // registerServiceWorker();

// export default CreateMeeting;

import { useState } from "react";
import VideoRoomComponent from "../components/openvidu/VideoRoomComponent"; // 경로는 실제 파일 위치에 따라 수정하세요.
import axios from "axios";

const CreateMeeting = () => {
  const [userName, setUserName] = useState("1234");
  const [roomName, setRoomName] = useState("My Room");
  const [showVideoRoom, setShowVideoRoom] = useState(false);
  const headers = {
    Authorization: `Bearer ${localStorage.getItem("access-token")}`,
  };
  const sendData = {
    meetingName: roomName,
  };
  const handleSubmit = (event) => {
    event.preventDefault();
    try {
      axios.post("http://localhost:8081/meeting/create/meetingtest", sendData, {
        headers,
      });
      console.log(headers);
      console.log(sendData);
      alert(JSON.stringify(sendData));

      reset();
    } catch (error) {
      console.error("There was an error!", error);
    }
    setShowVideoRoom(true); // 폼이 제출되었으므로 VideoRoomComponent를 보여줍니다.
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <label>
          User Name:
          <input
            type="text"
            value={userName}
            onChange={(event) => setUserName(event.target.value)}
          />
        </label>
        <label>
          Room Name:
          <input
            type="text"
            value={roomName}
            onChange={(event) => setRoomName(event.target.value)}
          />
        </label>
        <input type="submit" value="Submit" />
      </form>
      {showVideoRoom && (
        <VideoRoomComponent userName={userName} sessionName={roomName} />
      )}
    </div>
  );
};

export default CreateMeeting;
