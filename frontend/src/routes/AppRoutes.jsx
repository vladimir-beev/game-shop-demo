import { Routes, Route } from "react-router-dom";

import ProtectedRoute from "../components/ProtectedRoute";
import Layout from "../components/Layout";
import LandingPage from "../pages/LandingPage";
import GamesPage from "../pages/GamesPage";
import GameDetailsPage from "../pages/GameDetailsPage";
import UserProfilePage from "../pages/UserProfilePage";
import CartPage from "../pages/CartPage";
import OrdersPage from "../pages/OrdersPage";

import LoginPage from "../pages/LoginPage";
import RegisterPage from "../pages/RegisterPage";


export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        {/* Public */}
        <Route index element={<LandingPage />} />
        <Route path="login" element={<LoginPage />} />
        <Route path="register" element={<RegisterPage />} />
        <Route path="/products/games" element={<GamesPage />} />
        <Route path="/products/games/details/:id" element={<GameDetailsPage />} />
        {/* Logged-In Users Only */}
        <Route element={<ProtectedRoute />}>
          <Route path="/cart/items" element={<CartPage />}/>
          <Route path="/user/orders" element={<OrdersPage />} />
          <Route path="/user/profile" element={<UserProfilePage />} />
        </Route>
      </Route>
    </Routes>
  );
}
