import { useState } from "react";
import { useForm } from "react-hook-form";
import Signup from "../../pages/Signup";
import Login from "../../pages/Login";
import { Link } from "react-router-dom";

function Navigation() {
  return <nav>
    <Link to="/Login">
    <button type="button" id="login_button">로그인</button>
    </Link>
    <Link to="/Signup">
    <button type="button" id="signup_button">회원가입</button>
    </Link>
  </nav>
}
export default Navigation;