import { useState } from "react";
import VideoRoomComponent from "../components/openvidu/VideoRoomComponent";
import axios from "axios";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import useAccessToken from "../components/useAccessToken";
import { useParams } from "react-router-dom";
import styled from "styled-components";

const JoinMeeting = ({ closeModal }) => {
  const navigate = useNavigate();
  const { accessToken } = useAccessToken();

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
    <ModalBackground>
      <ModalContainer>
        <CloseButton onClick={closeModal}>×</CloseButton>
        <Form onSubmit={handleSubmit}>
          <Label>
            User Name:
            <Input type="text" value={payloadUserName} readOnly />
          </Label>
          <Label>
            Encrypted Key:
            <Input type="text" value={encryptedKey} readOnly />
          </Label>
          <Button type="submit">Submit</Button>
        </Form>
        {showVideoRoom && (
          <VideoRoomComponent
            userName={payloadUserName}
            meetingId={encryptedKey}
          />
        )}
      </ModalContainer>
    </ModalBackground>
  );
};

export default JoinMeeting;

const ModalBackground = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
`;

const ModalContainer = styled.div`
  position: relative;
  width: 70%;
  max-width: 600px;
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 15px;
`;

const Label = styled.label`
  display: flex;
  flex-direction: column;
  gap: 5px;
`;

const Input = styled.input`
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
`;

const Button = styled.button`
  padding: 10px 15px;
  border-radius: 5px;
  border: none;
  background-color: #007bff;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;

  &:hover {
    background-color: #0056b3;
  }
`;
const CloseButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: transparent;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #333;

  &:hover {
    color: #ff0000;
  }
`;
