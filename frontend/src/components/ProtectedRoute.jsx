import { Navigate, Outlet, useOutletContext } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "../auth/AuthContext";

export default function ProtectedRoute() {
  const { accessToken, authReady } = useContext(AuthContext);
  const showAlert = useOutletContext();

  if (!accessToken) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet context={showAlert} />;
}
