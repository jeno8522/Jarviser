import { useState } from "react";
import { useForm } from "react-hook-form";
import { BrowserRouter, Route } from "react-router-dom";
import Signup from "./Signup";
import Sidebar from "../components/molecules/Sidebar";
import MyPage from "./MyPage";
import MyCalendar from "./MyCalendar";
import MyReport from "./MyReport";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
function UserMain() {
  const navigate = useNavigate();
  const reservation = () => {
    navigate("/reservation");
  };
  return (
    <>
      {/* <div className="App"> */}
      <Sidebar />
      {/* </div> */}
      <Link to="/createmeeting" target="_blank">
        <button type="button" id="create_meeting_button">
          생성하기
        </button>
      </Link>
      <button type="button" id="join_meeting_button">
        입장하기
      </button>
      <button type="button" id="reserve_meeting_button" onClick={reservation}>
        예약하기
      </button>
    </>
  );
}
export default UserMain;
