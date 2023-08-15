import {useState} from "react";
import {useForm} from "react-hook-form";
import {BrowserRouter, Route} from "react-router-dom";
import Signup from "./Signup";
import Sidebar from "../components/molecules/Sidebar";
import MyPage from "./MyPage";
import MyCalendar from "./MyCalendar";
import MyReport from "./MyReport";
import {Link} from "react-router-dom";
import {useNavigate} from "react-router-dom";
import useAccessToken from "../components/useAccessToken";
import {useEffect} from "react";
import WebSocketComponent from "../components/web-socket/WebSocketComponent";

function UserMain() {
  const navigate = useNavigate();
  const {accessToken} = useAccessToken();
  const reservation = () => {
    navigate("/reservation");
  };
  useEffect(() => {
    if (!accessToken) {
      navigate("/login");
    }
  }, [accessToken, navigate]);
  return (
    <>
      {/* <div className="App"> */}
      <Sidebar />
      {/* </div> */}
      <Link to="/createmeeting">
        <button type="button" id="create_meeting_button">
          생성하기
        </button>
      </Link>
      <Link to="/joinMeeting">
        <button type="button" id="join_meeting_button">
          입장하기
        </button>
      </Link>

      <button type="button" id="reserve_meeting_button" onClick={reservation}>
        예약하기
      </button>
      <div>
        <a href="/stt-test/test/test-main-page.html">STT + 웹소켓 테스트</a>
      </div>
    </>
  );
}
export default UserMain;
