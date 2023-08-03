import { useEffect, useState } from "react";
import axios from "axios";
import jwt_decode from "jwt-decode";

function MyPage() {
  const accessToken = localStorage.getItem("access-token");
  const decodedToken = jwt_decode(accessToken);
  const userId = decodedToken.userId;
  const [userEmail, setUserEmail] = useState("");
  const [userPassword, setUserPassword] = useState("");
  const [userName, setUserName] = useState("");

  useEffect(() => {
    GetUser();
  }, []);

  async function GetUser() {
    try {
      const response = await axios.get(`http://localhost:8081/user/${userId}`);
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
      const response = await axios.patch(`http://localhost:8081/user/${userId}`, {
        name: userName,
        password: userPassword,
      });
      console.log(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div>
      <p>이메일 : {userEmail}</p>
      <form onSubmit={handleSubmit}>
        <p>이름 : <input type="text" required={true} value={userName} onChange={handleChangeName} /></p>
        <p>비밀번호 : <input type="password" required={true} value={userPassword} onChange={handleChangePassword} /></p>
        <button type="submit">수정</button>
      </form>
    </div>
  );
}

export default MyPage;