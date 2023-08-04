import React, {useState} from "react";
import VideoRoomComponent from "./VideoRoomComponent"; // 경로는 실제 파일 위치에 따라 수정하세요.

const SetMeeting = () => {
  const [id, setId] = useState("1234");
  const [roomName, setRoomName] = useState("My Room");
  const [showVideoRoom, setShowVideoRoom] = useState(false);

  const handleSubmit = (event) => {
    event.preventDefault();
    setShowVideoRoom(true); // 폼이 제출되었으므로 VideoRoomComponent를 보여줍니다.
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <label>
          ID:
          <input
            type="text"
            value={id}
            onChange={(event) => setId(event.target.value)}
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
      {showVideoRoom && <VideoRoomComponent user={id} sessionName={roomName} />}
    </div>
  );
};

export default SetMeeting;
