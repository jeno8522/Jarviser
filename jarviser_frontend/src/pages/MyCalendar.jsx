import React, { useState, useEffect } from "react";
import axios from "axios";
import Calendar from "react-calendar";
import calendarCss from "../components/CSS/calendarCSS.css";
import moment from "moment";
import MeetingInfo from "../components/meetingInfo";
import { useNavigate } from "react-router-dom";
import useAccessToken from "../components/useAccessToken";
import Sidebar from "../components/molecules/Sidebar";

function MyCalendar() {
  const navigate = useNavigate();
  const { accessToken } = useAccessToken();
  const [date, setDate] = useState(new Date());
  const [marks, setMarks] = useState([]); // marks 상태를 추가

  useEffect(() => {
    if (!accessToken) {
      navigate("/login");
    }
  }, [accessToken, navigate]);

  // meetinglist에서 날짜 데이터를 가져와 marks를 업데이트
  useEffect(() => {
    if (accessToken) {
      const headers = {
        Authorization: `Bearer ${accessToken}`,
      };
      axios
        .get("http://localhost:8081/user/meetinglist", { headers })
        .then((response) => {
          const meetingDates = response.data.meetinglist.map((meeting) =>
            moment(meeting.startTime).format("DD-MM-YYYY")
          );
          setMarks(meetingDates);
        })
        .catch((error) => {
          console.error(
            "An error occurred while fetching the meeting data:",
            error
          );
        });
    }
  }, [accessToken]);

  const handleDateChange = (date) => {
    setDate(moment(date).toDate());
  };

  return (
    <>
      <Sidebar />
      <Calendar
        onChange={handleDateChange}
        value={date}
        locale="en-EN"
        tileClassName={({ date, view }) => {
          if (marks.find((x) => x === moment(date).format("DD-MM-YYYY"))) {
            return "highlight";
          }
        }}
      />
      <MeetingInfo date={date} />
    </>
  );
}
export default MyCalendar;
