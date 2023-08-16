import React, { useEffect, useState } from "react";
import axios from "axios";
import MyReport from "./MyReport";
import { useNavigate, useParams } from "react-router-dom";
import useAccessToken from "../components/useAccessToken";
import Sidebar from "../components/molecules/Sidebar";
import AudioMessage from "../components/molecules/AudioMessage";
import Speech from "../components/molecules/Speech";
import Keyword from "../components/molecules/Keyword";

const ReportDetail = () => {
  const navigate = useNavigate();
  const { accessToken } = useAccessToken();
  const { id } = useParams();
  const [audioMessages, setAudioMessages] = useState([]);
  const [speechPercentage, setSpeechPercentage] = useState({});
  const [staticsOfKeywords, setStaticsOfKeywords] = useState({});
  const [isLoading, setIsLoading] = useState(true); // Loading 상태 추가

  useEffect(() => {
    if (!accessToken) {
      navigate("/login");
    } else {
      getMeetingDetails();
    }
  }, [accessToken, navigate]);

  const getMeetingDetails = async () => {
    try {
      const responseAudioMessage = await axios.get(
        `http://localhost:8081/meeting/audiomessage/fRsFnxwhA7frdnfFMjNPKA==`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          data: { meetingId: id },
        }
      );

      const responseSpeech = await axios.get(
        `http://localhost:8081/meeting/speech/fRsFnxwhA7frdnfFMjNPKA==`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          data: { meetingId: id },
        }
      );

      const responseKeywords = await axios.get(
        `http://localhost:8081/meeting/keywords/fRsFnxwhA7frdnfFMjNPKA==`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          data: { meetingId: id },
        }
      );

      setAudioMessages(responseAudioMessage.data.audioMessages);
      setSpeechPercentage(responseSpeech.data.statistics);
      setStaticsOfKeywords(responseKeywords.data.statistics);
      setIsLoading(false); // 데이터 로딩 완료 후 Loading 상태 변경
    } catch (error) {
      console.log(error);
      setIsLoading(false); // 데이터 로딩 실패 시 Loading 상태 변경
    }
  };

  return (
    <>
      <Sidebar />
      <div>
        <h1>회의 상세 정보</h1>
        {/* 조건부 렌더링 */}
        {!isLoading && (
          <>
            <AudioMessage
              audioMessages={audioMessages}
              setAudioMessages={setAudioMessages}
            />
            <Speech speechPercentage={speechPercentage} />
            <Keyword staticsOfKeywords={staticsOfKeywords} />
          </>
        )}
      </div>
    </>
  );
};

export default ReportDetail;
