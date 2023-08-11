import { useEffect, useState } from "react";
import axios from "axios";
import MyReport from "./MyReport";
import { useNavigate } from "react-router-dom";
import useAccessToken from "../components/useAccessToken";
import Sidebar from "../components/molecules/Sidebar";

const ReportDetail = () => {
  const navigate = useNavigate();
  const { accessToken } = useAccessToken();
  const headers = {
    Authorization: `Bearer ${accessToken}`,
  };

  try {
    axios
      .get(`http://localhost:8081/meeting/audiomessage/${1}`, {
        headers,
      })
      .then((response) => {
        console.log(response);
      });
  } catch (error) {
    console.error("There was an error!", error);
  }

  try {
    axios
      .get(`http://localhost:8081/meeting/meetingSpeech/${1}`, {
        headers,
      })
      .then((response) => {
        console.log(response);
      });
  } catch (error) {
    console.error("There was an error!", error);
  }

  useEffect(() => {
    if (!accessToken) {
      navigate("/login");
    }
  }, [accessToken, navigate]);

  return (
    <>
      <Sidebar />
      <div>게시판 상세보기 페이지</div>
    </>
  );
};

export default ReportDetail;
