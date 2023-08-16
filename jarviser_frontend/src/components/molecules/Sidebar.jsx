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
    { name: "회원정보", path: "/myPage", icon: User },
    { name: "캘린더", path: "/myCalendar", icon: Calendar },
    { name: "회의록", path: "/myReport", icon: Document },
    { name: "로그아웃", path: "/", icon: LogoutIcon },
  ];
  const Logout = () => {
    localStorage.removeItem("access-token");
    navigate("/");
  };
  return (
    <>
      <SidebarContianer>
        <MenuItem href="/myPage">
          <LogoImage src={User} alt="User" />
          <MenuText>회원정보</MenuText>
        </MenuItem>
        <MenuItem href="/myCalendar">
          <LogoImage src={Calendar} alt="MyCalendar" />
          <MenuText>캘린더</MenuText>
        </MenuItem>
        <MenuItem href="/myReport">
          <LogoImage src={Document} alt="Document" />
          <MenuText>회의록</MenuText>
        </MenuItem>
        <MenuItem href="/" onClick={Logout}>
          <LogoImage src={LogoutIcon} alt="Logout" />
          <MenuText>로그아웃</MenuText>
        </MenuItem>
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
  margin: 0px;
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

const MenuItem = styled.a`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 30px;
  text-decoration: none; // 링크 밑줄 제거
  color: #333; // 글자 색상
`;

const MenuText = styled.span`
  font-size: 14px;
  font-weight: 500;
  margin-top: 2px; // 이미지와의 간격
`;
