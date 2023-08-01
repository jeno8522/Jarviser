import React from "react";
import moment from "moment";
function MeetingInfo({ date }) {
  // 날짜에 따른 회의 정보를 가져오는 함수 (가상의 함수로 가정)
  const getMeetingInfo = (selectedDate) => {
    // 이 함수는 selectedDate에 해당하는 회의 정보를 가져오는 로직을 구현해야 합니다.
    // 가상의 데이터를 사용하겠습니다.
    const meetingData = [
      {
        date: "15-08-2023",
        title: "HONGWOONG's MEETING",
        description: "Meeting description 1",
      },
      {
        date: "03-01-2022",
        title: "Meeting 2",
        description: "Meeting description 2",
      },
      // ...
    ];

    // selectedDate와 일치하는 회의 정보를 찾아 반환합니다.
    const selectedMeeting = meetingData.find(
      (meeting) => meeting.date === moment(date).format("DD-MM-YYYY")
    );

    return selectedMeeting;
  };

  // 선택한 날짜에 따른 회의 정보를 가져옵니다.
  const meeting = getMeetingInfo(date.toISOString().split("T")[0]);

  return (
    <div>
      {meeting ? (
        <>
          <h2>Meeting Information for {moment(date).format("YYYY-MM-DD")}</h2>
          <p>Title: {meeting.title}</p>
          <p>Description: {meeting.description}</p>
        </>
      ) : (
        <p>No meetings found for {date.toISOString().split("T")[0]}</p>
      )}
    </div>
  );
}

export default MeetingInfo;
