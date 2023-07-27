import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./components/molecules/Header";
import Footer from "./components/molecules/Footer";
import Login from "./pages/Login";
import Main from "./pages/Main";
import UserMain from "./pages/UserMain";
import MyPage from "./pages/MyPage";
import MyCalendar from "./pages/MyCalendar";
import MyReport from "./pages/MyReport";
import Signup from "./pages/Signup";

function App() {
  return (
    <>
   <Router>
    <Header/>
      <Routes>
        <Route path ="/" element={<Main />} />
        <Route path ="/login" element={<Login />} />
        <Route path ="/signup" element={<Signup />} />
        <Route path ="/userMain" element={<UserMain />} />
        <Route path ="/mypage" element={<MyPage />} />
        <Route path ="/mycalendar" element={<MyCalendar />} />
        <Route path ="/myreport" element={<MyReport />} />
      </Routes>
      <Footer/>
     </Router>
     </>
  );
}

export default App;
