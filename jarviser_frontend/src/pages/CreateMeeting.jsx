import {useState, useEffect} from "react";
import VideoRoomComponent from "../components/openvidu/VideoRoomComponent"; // 경로는 실제 파일 위치에 따라 수정하세요.

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
  const [userName, setUserName] = useState("1234");
  const [sessionName, setSessionName] = useState("My Room");
  const [showVideoRoom, setShowVideoRoom] = useState(false);

  useEffect(() => {
    async function initializeSessionName() {
      const generatedName = await generateInitialSessionName();
      setSessionName(generatedName);
    }

    initializeSessionName();
  }, []);

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
            onChange={handleSessionNameChange}
          />
          <button type="button" onClick={handleCopy}>
            Copy
          </button>
        </label>
        <input type="submit" value="Submit" />
      </form>
      {showVideoRoom && (
        <VideoRoomComponent userName={userName} sessionName={sessionName} />
      )}
    </div>
  );
};

export default CreateMeeting;
