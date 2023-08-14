import { useState } from "react";
import { useForm } from "react-hook-form";
import Signup from "../../pages/Signup";
import Login from "../../pages/Login";
import { Link } from "react-router-dom";
import SidebarItem from "./SidebarItem";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
import Calendar from "../../logo/calendar.png";
import User from "../../logo/user.png";
import Document from "../../logo/document.png";
import LogoutIcon from "../../logo/logout.png";
import MainHeader from "./MainHeader";
function Sidebar() {
  const navigate = useNavigate();
  const menus = [
    { name: "회원정보", path: "/myPage" },
    { name: "캘린더", path: "/myCalendar" },
    { name: "회의록", path: "/myReport" },
    { name: "로그아웃", path: "/" },
  ];
  const Logout = () => {
    localStorage.removeItem("access-token");
    navigate("/");
  };
  return (
    <>
      <SidebarContianer>
        <a href="/myPage">
          <LogoImage src={User} alt="User" />
        </a>
        <a href="/myCalendar">
          <LogoImage src={Calendar} alt="MyCalendar" />
        </a>
        <a href="/myReport">
          <LogoImage src={Document} alt="Document" />
        </a>
        <a href="/">
          <LogoImage src={LogoutIcon} alt="Logout" onClick={Logout} />
        </a>
      </SidebarContianer>
    </>
  );
}
export default Sidebar;

const LogoImage = styled.img`
  width: 80px;
  height: 80px;
  flex-shrink: 0;
  margin-left: 20px;
  margin: 40px;
  transition: transform 0.3s ease-in-out; // 부드러운 변화 효과

  &:hover {
    transform: scale(1.2); // 호버 시 이미지를 1.1배 확대
  }
`;

const SidebarContianer = styled.div`
  display: flex;
  flex-direction: column;
  width: 13.5%;
  height: 104%;
  flex-shrink: 0;
  background-color: #cae1fd;
`;
