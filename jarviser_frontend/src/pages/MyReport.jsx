import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate, Link } from "react-router-dom";
import useAccessToken from "../components/useAccessToken";
import Sidebar from "../components/molecules/Sidebar";

function MyReport() {
  const navigate = useNavigate();
  const { accessToken } = useAccessToken();

  useEffect(() => {
    if (!accessToken) {
      navigate("/login");
    }
  }, [accessToken, navigate]);

  const [myReport, setMyReport] = useState([]);

  useEffect(() => {
    getMyReport();
  }, []);

  async function getMyReport() {
    try {
      const response = await axios.get("http://localhost:8081/user/meetinglist", {
        headers: { Authorization: `Bearer ${accessToken}` }
      });
      setMyReport(response.data.meetinglist);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
    <Sidebar />
      <div>
        <h1>회의록</h1>
      </div>
      <div>
        <ul>
          {myReport.map((report, index) => (
            <li key={index}>
              <h2>{report.meetingName}</h2>
              <p>Host: {report.hostName}</p>
              <p>Date: {report.date}</p>
              <Link to={"/reportdetail"}>{report.meetingName} 상세보기</Link>
            </li>
          ))}
        </ul>
      </div>
    </>
  );
};

export default MyReport;
