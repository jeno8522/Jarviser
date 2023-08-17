import {useState} from "react";
import VideoRoomComponent from "../components/openvidu/VideoRoomComponent";
import axios from "axios";
import {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import useAccessToken from "../components/useAccessToken";
import {useParams} from "react-router-dom";

const JoinMeeting = () => {
  const navigate = useNavigate();
  const {accessToken} = useAccessToken();

  useEffect(() => {
    if (!accessToken) {
      navigate("/login");
    }
  }, [accessToken, navigate]);

  const [userName, setUserName] = useState();
  const encryptedKey = useParams().urlKey;
  const [showVideoRoom, setShowVideoRoom] = useState(false);

  console.log("URL 주소창에 입력한 encrypted key 값 === ", encryptedKey);

  const handleSubmit = (event) => {
    event.preventDefault();
    setShowVideoRoom(true);
    handleJoinMeeting();
  };
  function base64UrlDecode(str) {
    // Base64Url로 인코딩된 문자열을 일반 Base64로 변환
    str = str.replace(/-/g, "+").replace(/_/g, "/");

    // 패딩 추가
    const pad = str.length % 4;
    if (pad) {
      if (pad === 1) {
        throw new Error("Invalid length while decoding base64url");
      }
      str += new Array(5 - pad).join("=");
    }

    return atob(str);
  }

  const token = accessToken;
  const segments = token.split(".");
  const payload = JSON.parse(base64UrlDecode(segments[1]));
  const payloadUserName = payload["username"];

  const handleJoinMeeting = async () => {
    console.log("encryptedKey === ", encryptedKey);
    console.log("accessToken === ", accessToken);
    const endpoint = `http://localhost:8081/meeting/joinMeeting/${encryptedKey}`;

    // 미팅에 참여하기 위해 서버에 요청을 보냅니다.
    try {
      const response = await axios.get(endpoint, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });

      console.log("response === ", response);
      if (response.status === 202) {
        console.log("Successfully joined the meeting!", response.data.meeting);

        // 미팅 참여에 성공했을 때 원하는 추가적인 로직을 수행할 수 있습니다.
      } else {
        console.error("Error joining the meeting:", response.data);
        alert("Error joining the meeting. Please try again.");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("An error occurred while joining the meeting. Please try again.");
    }
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <label>
          User Name:
          <input type="text" value={payloadUserName} readOnly />
        </label>
        <label>
          Encrypted Key:
          <input type="text" value={encryptedKey} readOnly />
        </label>
        <input type="submit" value="Submit" />
      </form>
      {showVideoRoom && (
        <VideoRoomComponent
          userName={payloadUserName}
          meetingId={encryptedKey}
        />
        // <VideoRoomComponent />
      )}
    </div>
  );
};

export default JoinMeeting;
