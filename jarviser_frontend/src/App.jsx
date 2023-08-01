import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./components/molecules/Header";
import Footer from "./components/molecules/Footer";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Main from "./pages/Main";
import Reservation from "./pages/Reservation";



function App() {
  return (
    <>
      <Header></Header>
      <Router>
        <Routes>
          <Route path="/" element={<Main />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/reservation" element={<Reservation />} />
        </Routes>
      </Router>
      <Footer></Footer>
    </>
  );
}

export default App;
