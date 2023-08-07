import { useState } from "react";
import { useForm } from "react-hook-form";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import useAccessToken from "../components/useAccessToken";
function MyReport() {
  const navigate = useNavigate();
  const { accessToken } = useAccessToken();

  useEffect(() => {
    if (!accessToken) {
      navigate("/login");
    }
  }, [accessToken, navigate]);
  return (
    <>
      <button>버튼</button>
      <div>
        <h1>회의록</h1>
      </div>
    </>
  );
}
export default MyReport;
