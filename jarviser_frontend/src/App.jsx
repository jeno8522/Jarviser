import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Header from "./molecules/Header";

function App() {
  return (
    <>
    <div><Header>g  </Header></div>
   <Router>
      <Routes>
        <Route path ="/login" element={<Login />} />
        <Route path ="/register" element={<Register />} />
      </Routes>
     </Router>
     </>
  );
}

export default App;
