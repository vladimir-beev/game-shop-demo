import { createContext, useState, useEffect, useRef } from "react";
import { jwtDecode } from "jwt-decode";
import api, { setupInterceptors } from "../api";

export const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [authReady, setAuthReady] = useState(false);
  const [accessToken, setAccessToken] = useState(null);
  const [roles, setRoles] = useState([]);
  const [user, setUser] = useState(null);

  const didRun = useRef(false);

  function getAccessToken() {
    return accessToken;
  }

  useEffect(() => {
    setupInterceptors(refresh, getAccessToken);
  }, []);

  useEffect(() => {
    if (didRun.current) return;
    didRun.current = true;

    const wasLoggedIn = localStorage.getItem("loggedIn");
    const hadCookie = localStorage.getItem("hasRefreshCookie");

    if (wasLoggedIn && hadCookie) {
      refresh().finally(() => setAuthReady(true));
    }
    else {
      setAuthReady(true);
    }
  }, []);

  async function login(email, password) {
    const response = await api.post("/auth/login", { email, password });
    const token = response.data.accessToken;
    const decoded = jwtDecode(token);

    setUser(decoded.username);
    setRoles(decoded.roles);
    setAccessToken(token);

    api.defaults.headers.common["Authorization"] = "Bearer " + token;

    localStorage.setItem("loggedIn", "true");
    localStorage.setItem("hasRefreshCookie", "true");

    setAuthReady(true);
  }

  async function register(email, username, password) {
    await api.post("/auth/register", { email, username, password });
  }

  async function logout() {
    await api.post("/auth/logout");

    setAccessToken(null);
    setUser(null);
    setRoles([]);

    localStorage.removeItem("loggedIn");
    localStorage.removeItem("hasRefreshCookie");

    delete api.defaults.headers.common["Authorization"];

    setAuthReady(true);
  }

  //refresh() function used by the interceptor
  async function refresh() {
    const response = await fetch(`${import.meta.env.VITE_GATEWAY_URL}/auth/refresh`, {
      method: "POST",
      credentials: "include",
    });

    if (!response.ok) {
      setAccessToken(null);
      setRoles([]);
      setUser(null);

      localStorage.removeItem("loggedIn");
      localStorage.removeItem("hasRefreshCookie");

      return null;
    }

    const data = await response.json();
    const token = data.accessToken;
    const decoded = jwtDecode(token);

    setAccessToken(token);
    setUser(decoded.username);
    setRoles(decoded.roles);

    api.defaults.headers.common["Authorization"] = "Bearer " + token;

    return token;
  }

  if (!authReady) {
    return null;
  }

  return (
    <AuthContext.Provider
      value={{ authReady, accessToken, user, roles, login, register, logout }}
    >
      {children}
    </AuthContext.Provider>
  );
}
