     
    import styled from "styled-components";
    import Header from "./Header";
    function MainHeader() {
      return (
        <nav>
          <HeaderContainer>
            <Header />
          </HeaderContainer>
        </nav>
      );
    }
    export default MainHeader;
    
    const HeaderContainer = styled.div`
      display: flex;
      width: 1800;
      height: 94px;
      flex-shrink: 0;
      background-color: #cae1fd;
      justify-content: space-between;
      align-items: center;
    `;