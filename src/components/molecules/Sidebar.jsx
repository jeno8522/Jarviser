import { useState } from "react";
import { useForm } from "react-hook-form";
import Signup from "../../pages/Signup";
import Login from "../../pages/Login";
import { Link } from "react-router-dom";
import SidebarItem from "./SidebarItem";
import { useNavigate } from "react-router-dom";

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
    <div className="sidebar">
      {menus.map((menu, index) => {
        if (menu.name === "로그아웃") {
          return (
            <div key={index} onClick={Logout}>
              <SidebarItem menu={menu} />
            </div>
          );
        } else {
          return (
            <Link to={menu.path} key={index}>
              <SidebarItem menu={menu} />
            </Link>
          );
        }
      })}
    </div>
  );
}
export default Sidebar;
