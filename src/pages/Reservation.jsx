import { useState } from "react";
import { useForm } from "react-hook-form";
import axios from "axios";

function Reservation() {
  const onSubmit = async (data) => {
    const date = data.date.replaceAll("-", "");
    const time = data.time.replaceAll(":", "");
    const ReservatedRoom = {
      meetingName: data.title,
      startTime: date + time,
      description: data.content,
    };

    const sendData = {
      reservatedRoom: ReservatedRoom,
      emails: userEmail, // 이메일 배열을 바로 할당
    };
    const headers = {
      Authorization: `Bearer ${localStorage.getItem("access-token")}`,
    };

    await new Promise((r) => setTimeout(r, 1000));
    console.log(headers);
    console.log(sendData);
    try {
      await axios.post("http://localhost:8081/reservation", sendData, {
        headers,
      });

      alert(JSON.stringify(sendData));

      setUserEmail([]);
      reset();
    } catch (error) {
      console.error("There was an error!", error);
    }
  };
  const [userEmail, setUserEmail] = useState([]);
  const addUserEmail = (email) => {
    if (isValidEmail(email)) {
      if (!userEmail.includes(email)) {
        setUserEmail([...userEmail, email]);
        setValue("userEmail", "");
      } else {
        alert("이미 추가된 이메일입니다.");
      }
    } else {
      alert("유효한 이메일 주소를 입력하세요.");
    }
  };
  const deleteUser = (index) => {
    const updatedUser = [...userEmail];
    updatedUser.splice(index, 1);
    setUserEmail(updatedUser);
  };
  const isValidEmail = (email) => {
    const emailPattern = /\S+@\S+\.\S+/;
    return emailPattern.test(email);
  };
  const {
    register,
    handleSubmit,
    reset,
    setValue,
    formState: { isSubmitting, isSubmitted, errors },
  } = useForm();

  return (
    <>
      <div>
        <h1>미팅 예약하기</h1>
      </div>

      <form onSubmit={handleSubmit(onSubmit)}>
        <label htmlFor="title">제목</label>
        <input
          id="title"
          type="text"
          placeholder="제목을 입력해주세요."
          aria-invalid={
            isSubmitted ? (errors.title ? "true" : "false") : undefined
          }
          {...register("title", {
            required: "제목은 필수 입력입니다.",
            pattern: {},
          })}
        />
        {errors.title && <small role="alert">{errors.title.message}</small>}
        <br />
        <label htmlFor="date">날짜</label>
        <input
          id="date"
          type="date"
          placeholder="날짜를 선택해주세요."
          aria-invalid={
            isSubmitted ? (errors.date ? "true" : "false") : undefined
          }
          {...register("date", {
            required: "날짜는 필수 입력입니다.",
          })}
        />
        {errors.date && <small role="alert">{errors.date.message}</small>}
        <br />
        <label htmlFor="time">시간</label>
        <input
          id="time"
          type="time"
          placeholder="시간을 선택해주세요."
          aria-invalid={
            isSubmitted ? (errors.time ? "true" : "false") : undefined
          }
          {...register("time", {
            required: "시간은 필수 입력입니다.",
          })}
        />
        {errors.time && <small role="alert">{errors.time.message}</small>}
        <br />
        <label htmlFor="content">내용</label>
        <input
          id="content"
          type="textarea"
          placeholder="내용을 입력해주세요."
          aria-invalid={
            isSubmitted ? (errors.content ? "true" : "false") : undefined
          }
          {...register("content", {
            required: "내용은 필수 입력입니다.",
          })}
        />
        {errors.content && <small role="alert">{errors.content.message}</small>}
        <br />
        <label htmlFor="userEmail">초대 이메일</label>
        <input
          id="userEmail"
          type="email"
          placeholder="이메일을 입력해주세요."
          aria-invalid={
            isSubmitted ? (errors.userEmail ? "true" : "false") : undefined
          }
          {...register("userEmail", {})}
        />

        <button
          type="button" // Add this line to prevent the button from submitting the form
          disabled={isSubmitting}
          onClick={() =>
            addUserEmail(document.getElementById("userEmail").value)
          }
        >
          추가
        </button>
        <br />
        {userEmail.map((email, index) => (
          <div key={index}>
            {email}
            <button type="button" onClick={() => deleteUser(index)}>
              삭제
            </button>
          </div>
        ))}
        <button type="submit" disabled={isSubmitting || userEmail.length === 0}>
          예약하기
        </button>
      </form>
    </>
  );
}
export default Reservation;
