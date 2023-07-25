import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";

function App() {
  return (
    <>
    <div><h1>JARVISER HEADER</h1></div>
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
