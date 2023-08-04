import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import useAccessToken from "../components/useAccessToken";
// import jwt_decode from "jwt-decode"; // jwt-decode 라이브러리를 사용합니다.

function MyPage() {
  const navigate = useNavigate();
  const { accessToken } = useAccessToken();

  useEffect(() => {
    if (!accessToken) {
      navigate("/login");
    }
  }, [accessToken, navigate]);

  // const { userId } = jwt_decode(accessToken); // 토큰에서 userId 가져오기
  const [userEmail, setUserEmail] = useState("");
  const [userPassword, setUserPassword] = useState("");
  const [userName, setUserName] = useState("");
  const [modalOpen, setModalOpen] = useState(false); // 모달창 띄우기 여부 상태
  const [isDeleting, setIsDeleting] = useState(false); // 회원 탈퇴 중 여부 상태

  useEffect(() => {
    GetUser();
  }, []);

  async function GetUser() {
    try {
      const response = await axios.get("http://localhost:8081/user/mypage", {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      const { email, name } = response.data.response; // 객체에서 이메일과 이름 정보 가져오기
      setUserEmail(email);
      setUserName(name);
    } catch (error) {
      console.log(error);
    }
  }

  const handleChangeName = (e) => {
    e.preventDefault();
    setUserName(e.target.value);
  };

  const handleChangePassword = (e) => {
    e.preventDefault();
    setUserPassword(e.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.patch(
        "http://localhost:8081/user/update",
        {
          name: userName,
          password: userPassword,
        },
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      console.log(response.data);
      window.location.href = "/usermain";
    } catch (error) {
      console.log(error);
    }
  };

  const handleDelete = async () => {
    try {
      setIsDeleting(true); // 탈퇴 중 상태로 변경
      await axios.delete("http://localhost:8081/user/delete", {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      // 회원 탈퇴 성공시 토큰 삭제 및 "/" 페이지로 이동
      localStorage.removeItem("access-token");
      window.location.href = "/";
    } catch (error) {
      console.log(error);
    }
  };

  const handleModalOpen = () => {
    setModalOpen(true);
  };

  const handleModalClose = () => {
    setModalOpen(false);
  };

  return (
    <div>
      <p>이메일 : {userEmail}</p>
      <form onSubmit={handleSubmit}>
        <p>
          이름 :{" "}
          <input
            type="text"
            required={true}
            value={userName}
            onChange={handleChangeName}
          />
        </p>
        <p>
          비밀번호 :{" "}
          <input
            type="password"
            required={true}
            value={userPassword}
            onChange={handleChangePassword}
          />
        </p>
        <button type="submit">수정</button>
      </form>
      <button onClick={handleModalOpen}>탈퇴하기</button>
      {modalOpen && (
        <div>
          <div>정말로 탈퇴하시겠습니까?</div>
          <button onClick={handleModalClose}>취소</button>
          <button onClick={handleDelete} disabled={isDeleting}>
            확인
          </button>
        </div>
      )}
    </div>
  );
}

export default MyPage;
