import { useState } from "react";
import Calendar from "react-calendar";
import calendarCss from "../components/CSS/calendar.css";
import moment from "moment";
import MeetingInfo from "../components/meetingInfo"; // MeetingInfo 컴포넌트를 import합니다.
function MyCalendar() {
  const [date, setDate] = useState(new Date());
  const marks = [
    "15-08-2023",
    "03-01-2022",
    "07-01-2022",
    "12-01-2022",
    "13-01-2022",
    "15-01-2022",
  ];

  const handleDateChange = (date) => {
    // UTC 시간을 로컬 시간으로 변환합니다.
    setDate(moment(date).toDate());
  };

  return (
    <>
      <Calendar
        onChange={handleDateChange} // 수정된 핸들러를 사용합니다.
        value={date} // 'date' 대신 'value' prop을 사용합니다.
        locale="en-EN"
        tileClassName={({ date, view }) => {
          if (marks.find((x) => x === moment(date).format("DD-MM-YYYY"))) {
            return "highlight";
          }
        }}
      />
      {/* 선택한 날짜에 해당하는 회의 정보를 출력하는 MeetingInfo 컴포넌트를 추가합니다. */}
      <MeetingInfo date={date} />
    </>
  );
}
export default MyCalendar;
