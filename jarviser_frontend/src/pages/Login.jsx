import styled from "styled-components";
import { useForm } from "react-hook-form";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import useAccessToken from "../components/useAccessToken";
import jarviserImg from "../assets/images/Jarviser_logo.jpg"; // 로고 이미지 경로
import Header from "../components/molecules/Navigation"

function Login() {
  const navigate = useNavigate();
  const { accessToken } = useAccessToken();

  if (accessToken) {
    navigate("/usermain");
  }

  const onSubmit = async (data) => {
    try {
      await new Promise((r) => setTimeout(r, 1000));
      const response = await axios.post("http://localhost:8081/user/login", data);
      const accessToken = response.data["access-token"];
      localStorage.setItem("access-token", accessToken);
      navigate("/userMain");
    } catch (error) {
      console.error("로그인 요청 에러:", error);
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
        <LoginHeadLine>
          <h1>login</h1>
        </LoginHeadLine>
        <LoginForm onSubmit={handleSubmit(onSubmit)}>
          <LoginLabel htmlFor="email">이메일</LoginLabel>
          <LoginField
            id="email"
            type="email"
            placeholder="아이디를 입력해주세요."
            aria-invalid={isSubmitted ? (errors.email ? "true" : "false") : undefined}
            {...register("email", {
              required: "이메일은 필수 입력입니다.",
              pattern: {
                value: /\S+@\S+\.\S+/,
                message: "이메일 형식에 맞지 않습니다.",
              },
            })}
          />
          {errors.email && <small role="alert">{errors.email.message}</small>}
          <LoginLabel htmlFor="password">비밀번호</LoginLabel>
          <LoginField
            id="password"
            type="password"
            placeholder="비밀번호를 입력해주세요."
            aria-invalid={isSubmitted ? (errors.password ? "true" : "false") : undefined}
            {...register("password", {
              required: "비밀번호는 필수 입력입니다.",
              minLength: {
                value: 8,
                message: "8자리 이상 비밀번호를 사용하세요.",
              },
            })}
          />
          {errors.password && <small role="alert">{errors.password.message}</small>}
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

const Whole = styled.div`
  display: flex;
  width: 100%;
  height: 60vh;
  flex-shrink: 0;
  justify-content: center;
  align-items: center;
  margin-top: 50px;
  margin-bottom: 100px;
`;

const ImageLogo = styled.img`
  width: 50%;
  height: 90%;
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  margin-left: 20px;
  margin-right: -20px;
`;

const RightColumn = styled.div`
  display: flex;
  width: 50%;
  height: 80%;
  padding: 40px;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  box-sizing: border-box;
`;

const LoginForm = styled.form`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 40%;
  gap: 8px;
`;

const LoginLabel = styled.label`
  align-self: stretch;
  color: var(--cool-gray-70, #4d5358);
  font-family: 'Roboto', sans-serif;
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 4px;
  letter-spacing: 0.5px;
  text-transform: uppercase;
`;

const LoginField = styled.input`
  width: 200%;
  height: 15px;
  padding: 6px;
  border: 1px solid var(--cool-gray-30, #C1C7CD);
  border-radius: 4px;
  background: var(--cool-gray-10, #F2F4F8);
  color: var(--cool-gray-60, #697077);
  font-family: 'Roboto', sans-serif;
  font-size: 16px;
  transition: border 0.3s ease;

  &:focus {
    border: 1px solid var(--primary-60, #0F62FE);
  }

  &::placeholder {
    color: var(--cool-gray-30, #C1C7CD);
  }
`;

const LoginButton = styled.button`
  width: 100%;
  height: 48px;
  padding: 12px;
  border: none;
  border-radius: 4px;
  background: var(--primary-60, #0F62FE);
  color: white;
  font-family: 'Roboto', sans-serif;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.3s ease;

  &:hover {
    background: var(--primary-50, #0043CE);
  }

  &:disabled {
    background: var(--cool-gray-30, #C1C7CD);
    cursor: not-allowed;
  }
`;

const LoginHeadLine = styled.h1`
  font-family: 'Roboto', sans-serif;
  font-size: 24px;
  font-weight: 500;
  color: var(--primary-60, #0F62FE);
  text-align: left;
  letter-spacing: -1px;
  margin-bottom: 16px;
`;

export default Login;
