import {useState, useEffect} from "react";
import VideoRoomComponent from "../components/openvidu/VideoRoomComponent";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import useAccessToken from "../components/useAccessToken";
import SttChatComponent from "../components/openvidu/chat/SttChatComponent";

const generateInitialSessionName = () => {
  const array = new Uint32Array(1);
  window.crypto.getRandomValues(array);
  const randomValue = array[0];
  const encoder = new TextEncoder();
  const data = encoder.encode(randomValue.toString());

  return window.crypto.subtle.digest("SHA-256", data).then((hashBuffer) => {
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    const hashHex = hashArray
      .map((b) => b.toString(16).padStart(2, "0"))
      .join("");
    return hashHex;
  });
};

const CreateMeeting = () => {
  const navigate = useNavigate();
  const {accessToken} = useAccessToken();

  useEffect(() => {
    if (!accessToken) {
      navigate("/login");
    }
  }, [accessToken, navigate]);

  const [userName, setUserName] = useState();
  const [sessionName, setSessionName] = useState("My Room");
  const [showVideoRoom, setShowVideoRoom] = useState(false);

  useEffect(() => {
    async function initializeSessionName() {
      const generatedName = await generateInitialSessionName();
      setSessionName(generatedName);
    }

    initializeSessionName();
  }, []);

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

  console.log("페이로드 정보!!! === ", payloadUserName); // 이렇게 하면 payload의 내용을 볼 수 있습니다.

  const handleSubmit = (event) => {
    event.preventDefault();
    setShowVideoRoom(true);
  };

  const handleCopy = () => {
    navigator.clipboard
      .writeText(sessionName)
      .then(() => {
        alert("Session Name copied to clipboard!");
      })
      .catch((err) => {
        alert("Failed to copy!");
      });
  };

  const handleSessionNameChange = (event) => {
    setSessionName(event.target.value);
  };
  const handleUserNameChange = (event) => {
    setUserName(payloadUserName);
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <label>
          User Name:
          <input
            type="text"
            value={payloadUserName}
            onChange={handleUserNameChange}
          />
        </label>
        <label>
          Session Name:
          <input
            type="text"
            value={sessionName}
            onChange={handleSessionNameChange}
          />
          <button type="button" onClick={handleCopy}>
            Copy
          </button>
        </label>
        <input type="submit" value="Submit" />
      </form>
      {showVideoRoom && (
        <VideoRoomComponent
          userName={payloadUserName}
          sessionName={sessionName}
        />
      )}
    </div>
  );
};

export default CreateMeeting;
