import {BrowserRouter, Routes, Route } from "react-router-dom";
import Flights from "@/pages/Flights"
import Seats from "@/pages/Seats"
import Register from "@/pages/Register"
import Login from "@/pages/Login"
import Profile from "@/pages/Profile"

function App() {
  return(
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Flights />} />
        <Route path="/seats:/id" element={<Seats />} />
        <Route path="register" element={<Register />} />
        <Route path="login" element={<Login />} />
        <Route path="profile" element={<Profile />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App


