import { useEffect, useState } from "react";
import axios from "axios";
import MyReport from "./MyReport";
import { useNavigate } from "react-router-dom";
import useAccessToken from "../components/useAccessToken";
import Sidebar from "../components/molecules/Sidebar";

const ReportDetail = () => {
    const navigate = useNavigate();
  const { accessToken } = useAccessToken();

  useEffect(() => {
    if (!accessToken) {
      navigate("/login");
    }
  }, [accessToken, navigate]);

    return(
        <>
        <Sidebar />
            <div>
                게시판 상세보기 페이지
            </div>
        </>
    );
};

export default ReportDetail;