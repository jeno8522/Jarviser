import { useState } from "react";
import { useForm } from "react-hook-form";

function Login(){
    return(
    <>
        <div><h1>로그인</h1></div> 
        <label id = "id">아이디</label>
        <input for = "id" placeholder="아이디를 입력하세요"></input>
    </>  
    );
}
export default Login;