     
    import { useState } from "react";
    import { useForm } from "react-hook-form";
    import { Link } from "react-router-dom";
    import Signup from "../pages/Signup";
    import Navigation from "../components/molecules/Navigation";
    import { useEffect } from "react";
    import { useNavigate } from "react-router-dom";
    import useAccessToken from "../components/useAccessToken";
    import styled from "styled-components";
    import MainImage from "../logo/Main.png";
    import Chart from "../logo/chart.png";
    import Keyword from "../logo/keyword.png";
    import Music from "../logo/music.png";
    import Stream from "../logo/stream.png";
    import { Icon } from "@material-ui/core";
    
    function Main() {
      const navigate = useNavigate();
      const { accessToken } = useAccessToken();
    
      useEffect(() => {
        if (accessToken) {
          navigate("/usermain");
        }
      }, [accessToken, navigate]);
      return (
        <>
          <Navigation></Navigation>
          <MainContianer>
            <TextsContainer>
              <TextContainer>
                <h1>업무를 더욱 가볍게</h1>
              </TextContainer>
              <TextContainer>
                <h2>그럴싸한 설명</h2>
              </TextContainer>
              <MoreButton as="a" href="#needs">
                더 알아보기 &rarr;
              </MoreButton>
            </TextsContainer>
            <MainPageImage src={MainImage} alt="JARVISER" />
          </MainContianer>
    
          <MainContianer2>
            <TextContainer2 id="needs">
              jarviser is the tool you need
            </TextContainer2>
            <TextContainer2>
              <h1>한 번 사용하면 빠져나올 수 없다</h1>
            </TextContainer2>
            <AllIconContainer>
              <BigIconContainer>
                <IconContainer src={Stream} alt="STREAM" />
                <IconText>실시간 반영</IconText>
                <IconText>실시간 음성을 채팅형식으로 받아보기</IconText>
              </BigIconContainer>
              <BigIconContainer>
                <IconContainer src={Music} alt="REPLAY" />
                <IconText>다시 듣기</IconText>
                <IconText>텍스트를 클릭하여 음성 다시듣기</IconText>
              </BigIconContainer>
              <BigIconContainer>
                <IconContainer src={Keyword} alt="Keywod" />
                <IconText>통계</IconText>
                <IconText>오늘의 키워드를 받아보기</IconText>
              </BigIconContainer>
              <BigIconContainer>
                <IconContainer src={Chart} alt="Chart" />
                <IconText>랭킹</IconText>
                <IconText>참여도가 가장 높은 유저 확인하기</IconText>
              </BigIconContainer>
            </AllIconContainer>
            <Link to="/signup">
              <SignupButton type="button" id="register_button">
                가입하러 가기
              </SignupButton>
            </Link>
          </MainContianer2>
          <p>INSTRUCTION</p>
          <div>1단계</div>
          <div>2단계</div>
          <div>3단계</div>
          <div>4단계</div>
    
          <p>개발한 사람들</p>
          <div>나현웅</div>
          <div>주창훈</div>
          <div>김민우</div>
          <div>김태현</div>
          <div>문홍웅</div>
          <div>최우석</div>
    
          <div>
            <a href="/">처음으로</a>
          </div>
    
          <div>
            <a href="/stt-test/test/test_code.html">STT 테스트</a>
          </div>
          <div>
            <a href="/stt-test/test/test-page.html">웹소켓 테스트</a>
          </div>
        </>
      );
    }
    export default Main;
    
    const TextContainer = styled.div`
      align-self: stretch;
      color: var(--cool-gray-90, #21272a);
      font-family: Roboto;
      font-size: 20px;
      font-style: normal;
      font-weight: 350px;
      line-height: 59.4px;
    `;
    
    const TextsContainer = styled.div`
      display: flex;
      width: 797px;
      height: 301px;
      flex-direction: column;
      align-items: flex-start;
      gap: 48px;
      flex-shrink: 0;
      margin-top: 100px;
    `;
    
    const MoreButton = styled.button`
      display: flex;
      width: 108px;
      height: 20px;
      padding: 16px 12px;
      justify-content: center;
      align-items: center;
      flex-shrink: 0;
      border-radius: 999px;
      border: 2px solid var(--primary-60, #4682A9);
      background: var(--primary-60, #4682A9);
      color: #F6F4EB;
      margin: 2px;
      font-size: 15px;
    `;
    
    const SignupButton = styled.button`
      display: flex;
      width: 160px;
      height: 70px;
      padding: 16px 12px;
      justify-content: center;
      align-items: center;
      flex-shrink: 0;
      border-radius: 999px;
      border: 2px solid #4682A9;
      background: #4682A9;
      color: #F6F4EB;
      margin-left: 700px;
      font-size: 15px;
    `;
    
    const MainPageImage = styled.img`
      width: 570px;
      height: 500px;
      flex-shrink: 0;
      background: url(<path-to-image>), lightgray 50% / cover no-repeat;
      margin-top: 100px;
    `;
    
    const MainContianer = styled.div`
      display: flex;
      justify-content: center;
      height: 680px;
      background: #F6F4EB;
    `;
    
    const MainContianer2 = styled.div`
      justify-content: center;
      height: 680px;
      background: white;
    `;
    
    const TextContainer2 = styled.div`
      align-self: stretch;
      color: var(--primary-90, #749BC2);
      text-align: center;
      /* Other/Caption */
      font-family: Roboto;
      font-size: 20px;
      font-style: normal;
      font-weight: 700;
      line-height: 100%; /* 20px */
      letter-spacing: 1px;
      text-transform: uppercase;
      margin-top: 100px;
    `;
    
    const IconContainer = styled.img`
      width: 80px;
      height: 80px;
      flex-shrink: 0;
      fill: var;
    `;
    
    const IconText = styled.div`
      color: var(--cool-gray-90, #21272a);
      text-align: center;
      font-family: Roboto;
      font-size: 18px;
      font-style: normal;
      font-weight: 400;
      line-height: 140%;
    `;
    
    const BigIconContainer = styled.div`
      display: flex;
      padding: 0px 16px;
      flex-direction: column;
      align-items: center;
      gap: 16px;
      flex: 1 0 0;
    `;
    
    const AllIconContainer = styled.div`
      display: flex;
      width: 1532px;
      height: 256px;
      align-items: flex-start;
      gap: 16px;
      flex-shrink: 0;
      margin-top: 100px;
    `;