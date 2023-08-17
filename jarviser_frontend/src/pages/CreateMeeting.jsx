import { useState, useEffect } from "react";
import VideoRoomComponent from "../components/openvidu/VideoRoomComponent";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import useAccessToken from "../components/useAccessToken";
import SttChatComponent from "../components/openvidu/chat/SttChatComponent";
import styled from "styled-components";

const HTTP_STATUS = {
  OK: 200,
  CREATED: 201,
  ACCEPTED: 202,
  BAD_REQUEST: 400,
  UNAUTHORIZED: 401,
};

const CreateMeeting = ({ closeModal }) => {
  const navigate = useNavigate();
  const { accessToken } = useAccessToken();

  useEffect(() => {
    if (!accessToken) {
      navigate("/login");
    }
  }, [accessToken, navigate]);

  const [userName, setUserName] = useState();
  const [sessionName, setSessionName] = useState("test방");
  const [showVideoRoom, setShowVideoRoom] = useState(false);
  const [encryptedKey, setEncryptedKey] = useState("");
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

  console.log("페이로드 정보!!! === ", payload); // 이렇게 하면 payload의 내용을 볼 수 있습니다.

  const handleSubmit = async (event) => {
    event.preventDefault();
    console.log("sessionName === ", sessionName);
    console.log("accessToken === ", accessToken);
    const endpoint = `http://localhost:8081/meeting/create/${sessionName}`;
    // 미팅을 생성하기 위해 서버에 요청을 보냅니다.
    try {
      const response = await axios.post(
        endpoint,
        {},
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );

      console.log("response === ", response);
      if (response.status === 202) {
        console.log(
          "Meeting created successfully!",
          response.data.encryptedKey
        );

        // 미팅 생성에 성공했을 때만 VideoRoomComponent를 보여줍니다.
        setEncryptedKey(response.data.encryptedKey);
        // console.log("typeof encryptedKey === ", typeof encryptedKey);
        setShowVideoRoom(true);
      } else {
        console.error("Error creating meeting:", response.data);
        alert("Error creating meeting. Please try again.");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("An error occurred while creating the meeting. Please try again.");
    }
  };
  const handleCopy = () => {
    navigator.clipboard
      .writeText(sessionName)
      .then(() => {
        alert("Meeting URL이 복사됐습니다.");
      })
      .catch((err) => {
        alert("Meeting URL 복사에 실패했습니다.");
      });
  };

  const handleSessionNameChange = (event) => {
    setSessionName(event.target.value);
  };
  const handleUserNameChange = (event) => {
    setUserName(payloadUserName);
  };

  return (
    <ModalBackground>
      <ModalContainer>
        <CloseButton onClick={closeModal}>×</CloseButton>
        <Form onSubmit={handleSubmit}>
          <Label>
            User Name:
            <Input
              type="text"
              value={payloadUserName}
              onChange={handleUserNameChange}
            />
          </Label>
          <Label>
            Meeting Name:
            <Input
              type="text"
              value={sessionName}
              onChange={handleSessionNameChange}
            />
            <Button type="button" onClick={handleCopy}>
              Copy
            </Button>
          </Label>
          <Button type="submit">Submit</Button>
        </Form>
        {showVideoRoom && (
          <VideoRoomComponent
            userName={payloadUserName}
            sessionName={sessionName}
            meetingId={encryptedKey}
          />
        )}
      </ModalContainer>
    </ModalBackground>
  );
};

export default CreateMeeting;

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
