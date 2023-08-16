import React, { useState, useEffect } from "react";
import axios from "axios";
import moment from "moment";
import useAccessToken from "./useAccessToken";

function MeetingInfo({ date }) {
  const [meetings, setMeetings] = useState([]);
  const { accessToken } = useAccessToken();
  const headers = {
    Authorization: `Bearer ${accessToken}`,
  };
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8081/user/meetinglist",
          {
            headers,
          }
        );

        const meetingList = response.data.meetinglist;
        console.log(meetingList);
        const selectedMeetings = meetingList.filter(
          (m) =>
            moment(m.startTime).format("DD-MM-YYYY") ===
            moment(date).format("DD-MM-YYYY")
        );

        setMeetings(selectedMeetings); // 선택된 회의를 상태로 설정
      } catch (error) {
        console.error(
          "An error occurred while fetching the meeting data:",
          error
        );
      }
    };

    fetchData();
  }, [date]);

  const handleDelete = async (reservatedMeetingId) => {
    try {
      await axios.delete(
        `http://localhost:8081/reservation/${reservatedMeetingId}`,
        {
          headers,
          data: { reservatedMeetingId },
        }
      );
      const updatedMeetings = meetings.filter(
        (meeting) => meeting.reservatedMeetingId !== reservatedMeetingId
      );
      setMeetings(updatedMeetings);
    } catch (error) {
      console.error("Failed to delete the meeting:", error);
    }
  };

  return (
    <div>
      {meetings.length ? (
        <>
          <h2>Meeting Information for {moment(date).format("YYYY-MM-DD")}</h2>
          {meetings.map((meeting) => (
            <div key={meeting.meetingName}>
              <p>Title: {meeting.meetingName}</p>
              <p>Host: {meeting.hostName}</p>
              <p>Start Time: {meeting.date}</p>
              <button onClick={() => handleDelete(meeting.reservatedMeetingId)}>
                삭제
              </button>
              <hr />
            </div>
          ))}
        </>
      ) : (
        <p>No meetings found for {moment(date).format("YYYY-MM-DD")}</p>
      )}
    </div>
  );
}

export default MeetingInfo;
