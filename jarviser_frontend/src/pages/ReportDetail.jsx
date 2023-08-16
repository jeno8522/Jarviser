import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import useAccessToken from "../components/useAccessToken";
import Sidebar from "../components/molecules/Sidebar";
import AudioMessage from "../components/molecules/AudioMessage";
import Speech from "../components/molecules/Speech";
import Keyword from "../components/molecules/Keyword";
import MainHeader from "../components/molecules/MainHeader";
import styled from "styled-components";

const ReportDetail = () => {
  const navigate = useNavigate();
  const { accessToken } = useAccessToken();
  const { id } = useParams();
  const [audioMessages, setAudioMessages] = useState([]);
  const [speechPercentage, setSpeechPercentage] = useState({});
  const [staticsOfKeywords, setStaticsOfKeywords] = useState({});
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (!accessToken) {
      navigate("/login");
    } else {
      getMeetingDetails();
    }
  }, [accessToken, navigate]);

  const getMeetingDetails = async () => {
    try {

      // 원래는 미팅 이렇게 종료할때 우리의 통계들이 db에 저장됨.
      const meetingEnded = await axios.get(
        `http://localhost:8081/meeting/end/PUNQLHY4EEB3P23WO7CTEM2PFA`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          data: { meetingId: id },
        }
      );

      const responseAudioMessage = await axios.get(
        `http://localhost:8081/meeting/audiomessage/PUNQLHY4EEB3P23WO7CTEM2PFA`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          data: { meetingId: id },
        }
      );

      const responseSpeech = await axios.get(
        `http://localhost:8081/meeting/speech/PUNQLHY4EEB3P23WO7CTEM2PFA`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          data: { meetingId: id },
        }
      );

      const responseKeywords = await axios.get(
        `http://localhost:8081/meeting/keywords/PUNQLHY4EEB3P23WO7CTEM2PFA`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          data: { meetingId: id },
        }
      );

      setAudioMessages(
        responseAudioMessage.data.audioMessages.map((audioMessage) => ({
          ...audioMessage,
          isEditing: false,
        }))
      );
      setSpeechPercentage(responseSpeech.data.statistics);
      setStaticsOfKeywords(responseKeywords.data.statistics);
      setIsLoading(false);
    } catch (error) {
      console.log(error);
      setIsLoading(false);
    }
  };

  const handleEditClick = (index) => {
    const newAudioMessages = [...audioMessages];
    newAudioMessages[index].isEditing = true;
    setAudioMessages(newAudioMessages);
  };

  const handleSaveClick = (index, newContent) => {
    // TODO: 수정된 내용을 저장하고 DB에 업데이트

    const newAudioMessages = [...audioMessages];
    newAudioMessages[index].content = newContent;
    newAudioMessages[index].isEditing = false;
    setAudioMessages(newAudioMessages);
  };

  const downloadTextFile = () => {
    const combinedMessages = audioMessages
      .map((audioMessage) => `${audioMessage.name}: ${audioMessage.content}`)
      .join("\n");

    const blob = new Blob([combinedMessages], { type: "text/plain" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = "combined_audio_messages.txt";
    link.click();
  };

  return (
    <>
      <MainHeader />
      <MainContainer>
        <Sidebar />
        <ContentContainer>
          <h1>회의 상세 정보</h1>
          {!isLoading && (
            <ContentWrapper>
              <AudioWrapper>
                {audioMessages.map((audio, index) => (
                  <AudioBox key={index}>
                    <AudioMessage
                      audioMessage={audio}
                      onEditClick={() => handleEditClick(index)}
                      onSaveClick={(newContent) =>
                        handleSaveClick(index, newContent)
                      }
                    />
                  </AudioBox>
                ))}
                <DownloadButton onClick={downloadTextFile}>
                  오디오 메시지 다운로드
                </DownloadButton>
              </AudioWrapper>
              <GraphWrapper>
                <GraphTop>
                  <SpeechWrapper>
                    <Speech speechPercentage={speechPercentage} />
                  </SpeechWrapper>
                </GraphTop>
                <GraphBottom>
                  <KeywordWrapper>
                    <Keyword staticsOfKeywords={staticsOfKeywords} />
                  </KeywordWrapper>
                </GraphBottom>
              </GraphWrapper>
            </ContentWrapper>
          )}
        </ContentContainer>
      </MainContainer>
    </>
  );
};

export default ReportDetail;

const MainContainer = styled.div`
  display: flex;
`;

const ContentContainer = styled.div`
  flex: 1;
  padding: 20px;
  max-height: calc(
    100vh - 70px
  ); /* 페이지 높이 조절 (70px는 MainHeader의 높이) */
  overflow: auto; /* 내용이 페이지를 벗어나면 스크롤 생성 */
`;

const ContentWrapper = styled.div`
  display: flex;
  gap: 20px;
`;

const AudioWrapper = styled.div`
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  overflow-y: auto; /* 세로 스크롤 바 생성 */
  max-height: 520px; /* 최대 높이 설정 */
`;

const AudioBox = styled.div`
  margin: 10px;
  padding: 10px;
  width: 600px;
  border: 1px solid #dde1e6;
  border-radius: 10px;
`;

const GraphWrapper = styled.div`
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  overflow-y: auto; /* 세로 스크롤 바 생성 */
  max-height: 520px; /* 최대 높이 설정 */
  width: 600px;
`;

const GraphTop = styled.div`
  flex: 1;
`;

const GraphBottom = styled.div`
  flex: 1;
`;

const SpeechWrapper = styled.div`
  width: 30rem;
  height: 30rem;
  margin-bottom: 50px;
`;

const KeywordWrapper = styled.div`
  flex: 1;
`;

const DownloadButton = styled.button`
  background-color: #3742fa;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 5px 10px;
  margin-top: 10px;
  cursor: pointer;
`;

// ... (기타 스타일 계속 유지)
