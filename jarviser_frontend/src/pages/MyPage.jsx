import { useEffect, useState } from "react";
import axios from "axios";
import jwt_decode from "jwt-decode"; // jwt-decode 라이브러리를 사용합니다.

// Modal component
function ConfirmationModal({ isOpen, onCancel, onConfirm }) {
  if (!isOpen) return null;

  return (
    <div>
      <h2>정말로 탈퇴하시겠습니까?</h2>
      <button onClick={onConfirm}>확인</button>
      <button onClick={onCancel}>취소</button>
    </div>
  );
}

function MyPage({ user }) {
  const [isEditingPassword, setIsEditingPassword] = useState(false);
  const [isEditingName, setIsEditingName] = useState(false);
  const [newPassword, setNewPassword] = useState("");
  const [newName, setNewName] = useState("");
  const [isConfirmationModalOpen, setIsConfirmationModalOpen] =
    useState(false);
  // const [userId, setUserId] = useState(""); // user.id를 userId로 변경
  const [userInfo, setUserInfo] = useState(null);
  const [loading, setLoading] = useState(true);
  
  useEffect(() => {
    const accessToken = localStorage.getItem("access-token");
    if (accessToken) {
      try {
        const decodedToken = jwt_decode(accessToken); // 토큰 디코딩
        const userEmail = decodedToken?.sub; // 이메일 추출
        const userid = 
        axios
          .get(`http://localhost:8081/user/${userid}`, {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          })
          .then((response) => {
            const userData = response.data;
            setUserInfo(userData);
            setLoading(false);
          })
          .catch((error) => {
            console.error("사용자 정보 가져오기 실패:", error);
            setLoading(false);
          });
      } catch (error) {
        console.error("토큰 디코딩 오류:", error);
        setLoading(false);
      }
    }
  }, []);

  const handlePasswordUpdate = () => {
    // TODO: Password 업데이트 로직을 구현합니다.
    // newPassword 상태를 이용하여 새로운 비밀번호를 가져와 처리할 수 있습니다.
    // 서버로 비밀번호를 전송하여 업데이트하거나 다른 방식으로 처리합니다.
    console.log("New Password:", newPassword);
    setIsEditingPassword(false);
  };

  const handleNameUpdate = () => {
    // TODO: Name 업데이트 로직을 구현합니다.
    // newName 상태를 이용하여 새로운 이름을 가져와 처리할 수 있습니다.
    // 서버로 이름을 전송하여 업데이트하거나 다른 방식으로 처리합니다.
    console.log("New Name:", newName);
    setIsEditingName(false);
  };

  const handleWithdrawal = () => {
    // TODO: 회원탈퇴 로직 구현
    // ... (handle the withdrawal process, navigate to the main page, etc.)
    console.log("Withdrawal process completed.");
    setIsConfirmationModalOpen(false);
    // 메인 페이지 이동.    
    window.location.replace("/");
  };

  // 데이터가 로딩 중일 때 로딩 상태를 보여줌
  if (loading) {
    return <div>Loading...</div>;
  }

  // 데이터를 받아오지 못한 경우 에러 메시지를 보여줌
  if (!userInfo) {
    return <div>Error: 사용자 정보를 가져오지 못했습니다.</div>;
  }

  // 사용자 정보를 받아온 경우
  return (
    <>
      <h1>회원정보</h1>
      <div>프로필 이미지</div>
      <button type="button" id="update_image_button">
        변경하기
      </button>

      <dt>Email</dt>
      <dd>{userInfo.email}</dd>

      {isEditingPassword ? (
        <>
          <dt>New Password</dt>
          <input
            type="password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
          />
          <button type="button" onClick={handlePasswordUpdate}>
            확인
          </button>
        </>
      ) : (
        <>
          <dt>Password</dt>
          <dd>{userInfo.password}</dd>
          <button type="button" id="update_password_button" onClick={() => setIsEditingPassword(true)}>
            변경하기
          </button>
        </>
      )}

      {isEditingName ? (
        <>
          <dt>New Name</dt>
          <input
            type="text"
            value={newName}
            onChange={(e) => setNewName(e.target.value)}
          />
          <button type="button" onClick={handleNameUpdate}>
            확인
          </button>
        </>
      ) : (
        <>
          <dt>Name</dt>
          <dd>{userInfo.name}</dd>
          <button type="button" id="update_name_button" onClick={() => setIsEditingName(true)}>
            변경하기
          </button>
        </>
      )}

<div>
        <button
          type="button"
          id="withdrawal_button"
          onClick={() => setIsConfirmationModalOpen(true)}
        >
          회원탈퇴
        </button>
      </div>

      {/* Modal */}
      <ConfirmationModal
        isOpen={isConfirmationModalOpen}
        onCancel={() => setIsConfirmationModalOpen(false)}
        onConfirm={handleWithdrawal}
      />
    </>
  );
}

export default MyPage;