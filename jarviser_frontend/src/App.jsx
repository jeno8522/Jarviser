import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./components/molecules/Header";
import Footer from "./components/molecules/Footer";
import Login from "./pages/Login";
import Register from "./pages/Signup";
import Main from "./pages/Main";
import UserMain from "./pages/UserMain";
import MyPage from "./pages/MyPage";
import MyCalendar from "./pages/MyCalendar";
import MyReport from "./pages/MyReport";

function App() {
  return (
    <>
    <div><h1>JARVISER HEADER</h1></div>
   <Router>
    <Header/>
      <Routes>
        <Route path ="/" element={<Main />} />
        <Route path ="/login" element={<Login />} />
        <Route path ="/register" element={<Register />} />
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
