import {useState} from "react";
import VideoRoomComponent from "../components/openvidu/VideoRoomComponent";
import axios from "axios";
import {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import useAccessToken from "../components/useAccessToken";

const JoinMeeting = () => {
  const navigate = useNavigate();
  const {accessToken} = useAccessToken();

  useEffect(() => {
    if (!accessToken) {
      navigate("/login");
    }
  }, [accessToken, navigate]);
  const [userName, setUserName] = useState("1234");
  const [sessionName, setSessionName] = useState("My Room");
  const [showVideoRoom, setShowVideoRoom] = useState(false);

  const handleSubmit = (event) => {
    event.preventDefault();
    setShowVideoRoom(true);
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
          Session Name:
          <input
            type="text"
            value={sessionName}
            onChange={(event) => setSessionName(event.target.value)}
          />
        </label>
        <input type="submit" value="Submit" />
      </form>
      {showVideoRoom && (
        <VideoRoomComponent userName={userName} sessionName={sessionName} />
        // <VideoRoomComponent />
      )}
    </div>
  );
};

export default JoinMeeting;
