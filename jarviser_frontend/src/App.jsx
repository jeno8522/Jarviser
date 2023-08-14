import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./components/molecules/Header";
import Footer from "./components/molecules/Footer";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Main from "./pages/Main";
import UserMain from "./pages/UserMain";
import MyPage from "./pages/MyPage";
import MyCalendar from "./pages/MyCalendar";
import MyReport from "./pages/MyReport";
import Reservation from "./pages/Reservation";
import CreateMeeting from "./pages/CreateMeeting";
import JoinMeeting from "./pages/JoinMeeting";
import ReportDetail from "./pages/ReportDetail";
function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<Main />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/usermain" element={<UserMain />} />
          <Route path="/mypage" element={<MyPage />} />
          <Route path="/mycalendar" element={<MyCalendar />} />
          <Route path="/myreport" element={<MyReport />} />
          <Route path="/reportdetail" element={<ReportDetail />} />
          <Route path="/reservation" element={<Reservation />} />
          <Route path="/createmeeting" element={<CreateMeeting />} />
          <Route path="/joinmeeting" element={<JoinMeeting />} />
        </Routes>
      </Router>
      <Footer></Footer>
    </>
  );
}

export default App;
