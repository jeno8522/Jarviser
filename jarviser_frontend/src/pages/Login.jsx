import styled from "styled-components";
import { useForm } from "react-hook-form";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import useAccessToken from "../components/useAccessToken";
import jarviserImg from "../assets/images/내 프로젝트.png"; // 로고 이미지 경로
import Header from "../components/molecules/Navigation";
import { useEffect } from "react";

function Login() {
  const navigate = useNavigate();
  const { accessToken } = useAccessToken();

  useEffect(() => {
    if (accessToken) {
      navigate("/usermain");
    }
  }, [accessToken, navigate]);

  const onSubmit = async (data) => {
    try {
      await new Promise((r) => setTimeout(r, 1000));
      const response = await axios.post(
        "https://i9a506.p.ssafy.io:8081/user/login",
        data
      );
      const accessToken = response.data["access-token"];
      localStorage.setItem("access-token", accessToken);
      navigate("/userMain");
    } catch (error) {
      console.error("로그인 요청 에러:", error);
      alert("아이디 또는 비밀번호가 틀렸습니다"); // 로그인 실패 시 알림 추가
    }
  };

  const {
    register,
    handleSubmit,
    formState: { isSubmitting, isSubmitted, errors },
  } = useForm();

  return (
    <>
      <Header />

      <Whole>
        <ImageLogo src={jarviserImg} alt="Jarviser Logo" />
        <RightColumn>
          <LoginHeadLine>login</LoginHeadLine>
          <LoginForm onSubmit={handleSubmit(onSubmit)}>
            <LoginLabel htmlFor="email">이메일</LoginLabel>
            <LoginField
              id="email"
              type="email"
              placeholder="아이디를 입력해주세요."
              aria-invalid={
                isSubmitted ? (errors.email ? "true" : "false") : undefined
              }
              {...register("email", {
                required: "이메일은 필수 입력입니다.",
                pattern: {
                  value: /\S+@\S+\.\S+/,
                  message: "이메일 형식에 맞지 않습니다.",
                },
              })}
            />
            <ErrorMessage>
              {errors.email ? errors.email.message : ""}
            </ErrorMessage>
            <LoginLabel htmlFor="password">비밀번호</LoginLabel>
            <LoginField
              id="password"
              type="password"
              placeholder="비밀번호를 입력해주세요."
              aria-invalid={
                isSubmitted ? (errors.password ? "true" : "false") : undefined
              }
              {...register("password", {
                required: "비밀번호는 필수 입력입니다.",
                minLength: {
                  value: 8,
                  message: "8자리 이상 비밀번호를 사용하세요.",
                },
              })}
            />
            <ErrorMessage>
              {errors.password ? errors.password.message : ""}
            </ErrorMessage>
            <LoginButton type="submit" disabled={isSubmitting}>
              로그인
            </LoginButton>
          </LoginForm>
        </RightColumn>
      </Whole>
    </>
  );
}

// Styled Components (스타일 부분)
const ErrorMessage = styled.small`
  min-height: 20px; // Adjust this value based on your design
  display: block;
  color: red;
`;
const Whole = styled.div`
  display: flex;
  width: 100%;
  height: 60vh;
  flex-shrink: 0;
  justify-content: center;
  align-items: center;
  margin-top: 6%;
  margin-bottom: 100px;
`;

const Wrapper = styled.div`
  width: 88%;
  height: 70vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #ffffff; // 옅은 회색 배경
  padding: 40px;
  border-radius: 20px; // 둥근 모서리
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3); // 그림자 효과
`;

const ImageLogo = styled.img`
  width: 30%;
  height: 80%;
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  margin-left: 0px;
  margin-right: 50px;
`;

const RightColumn = styled.div`
  margin-left = 100px;
  display: flex;
  width: 40%;
  height: 90%;
  padding: 40px;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  box-sizing: border-box;
  background: linear-gradient(45deg, #b3daff, #4da6ff);
  color: white; // 텍스트 색상 변경
  border-radius: 20px; // 모서리 둥글게
  left-margin:100%;
`;

const LoginForm = styled.form`
  margin-top = 0px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 40%;
  gap: 8px;
`;

const LoginLabel = styled.label`
  align-self: stretch;
  color: var(--cool-gray-70, #4d5358);
  font-family: "Roboto", sans-serif;
  font-size: 18px; // 크기 조정
  font-weight: 600; // 굵기 조정
  margin-bottom: 4px;
  letter-spacing: 0.5px;
  text-transform: uppercase;
`;

const LoginField = styled.input`
  width: 200%;
  height: 15px;
  padding: 6px;
  border: 1px solid var(--cool-gray-30, #c1c7cd);
  border-radius: 4px;
  background: var(--cool-gray-10, #f2f4f8);
  color: var(--cool-gray-60, #697077);
  font-family: "Roboto", sans-serif;
  font-size: 16px;
  transition: border 0.3s ease;

  &:focus {
    border: 1px solid var(--primary-60, #0f62fe);
  }

  &::placeholder {
    color: var(--cool-gray-30, #c1c7cd);
  }
`;

const LoginButton = styled.button`
  width: 200px; // 너비 조정
  height: 48px;
  padding: 12px;
  border: none;
  border-radius: 4px;
  background: linear-gradient(45deg, #2e2efe, #4da6ff);
  color: white;
  font-family: "Roboto", sans-serif;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.3s ease, transform 0.2s ease, box-shadow 0.2s ease;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); // 그림자 효과 추가
  margin-top: 100px;
  margin-left: 70%;
  border-radius: 999px;
  &:hover {
    background: var(--primary-50, #0043ce);
    transform: translateY(-2px); // 호버 시 약간 위로 이동
  }

  &:active {
    transform: translateY(0); // 클릭 시 원래 위치로
    box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1); // 클릭 시 그림자 크기 조정
  }

  &:disabled {
    background: var(--cool-gray-30, #c1c7cd);
    cursor: not-allowed;
  }
`;

const LoginHeadLine = styled.h1`
  font-family: "Roboto", sans-serif;
  font-size: 32px; // 크기 조정
  font-weight: 700; // 굵기 조정
  color: white; // 색상 변경
  text-align: left;
  letter-spacing: -1px;
  margin-top: 50px;
  margin-bottom: 0px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1); // 텍스트 그림자 추가
`;

export default Login;
