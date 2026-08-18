import { Outlet, useLocation } from "react-router-dom";
import { useState, useContext } from "react";
import { AuthContext } from "../auth/AuthContext";
import Navbar from "./Navbar";
import Alert from "./Alert";
import Footer from "./Footer";

export default function Layout() {
  const { accessToken } = useContext(AuthContext);
  const [alert, setAlert] = useState(null);
  const location = useLocation();

  const hideBackground =
    location.pathname === "/login" || location.pathname === "/register";

  function showAlert(alertData) {
    setAlert(null);
    setTimeout(() => setAlert(alertData), 10); //adding small delay so React can remount
  }

  return (
    <div className="flex flex-col min-h-screen">
      <Navbar />
      <main className="flex-1 flex flex-col">
        {alert && (
          <Alert
            message={alert.message}
            type={alert.type}
            onClose={() => setAlert(null)}
          />
        )}
        <div className="flex flex-1 w-[70%] mx-auto">
          <Outlet context={showAlert} />
        </div>
      </main>
      <Footer />
    </div>
  );
}
