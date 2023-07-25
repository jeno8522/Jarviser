import { useState } from "react";
import { useForm } from "react-hook-form";

function Register(){
    return(
    <>
        <div>
            <h1>회원가입</h1>
        </div>
        <form>
            <label htmlFor="email">이메일</label>
            <input id="email" type="email" placeholder="이메일을 입력해주세요."/>
            <label htmlFor="password">비밀번호</label>
            <input id="password" type="password" placeholder="비밀번호를 입력해주세요."/>
            <label htmlFor="name">이름</label>
            <input id="name" type="text" placeholder="이름을 입력해주세요."/>
            <button type="submit">회원가입</button>
        </form>  
    </>  
    );
}
export default Register;