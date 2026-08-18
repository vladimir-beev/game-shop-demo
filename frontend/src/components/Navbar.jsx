import { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../auth/AuthContext";
import UserMenu from "./UserMenu";

export default function Navbar() {
  const navigate = useNavigate();
  const { accessToken, logout, user } = useContext(AuthContext);
  const isLoggedIn = Boolean(accessToken); 

  const [isMenuOpen, setIsMenuOpen] = useState(false);

  async function handleLogout() {
    await logout();
    navigate("/login");
  }

  return (
    <header className="relative flex flex-wrap items-center bg-gray-800 text-white px-10 py-4 text-center">
      <h1
        onClick={() => navigate("/")}
        className="text-3xl font-bold cursor-pointer"
      >
        <span className="text-yellow-300">Game</span>Shop
      </h1>
      <div className="ml-auto flex items-center gap-4">
        {!isLoggedIn ? (
          <p
            onClick={() => navigate("/login")}
            className=" text-lg font-semibold cursor-pointer hover:underline hover:text-yellow-200"
          >
            Sign In
          </p>
        ) : (
          <div className="flex flex-wrap items-center gap-8">
            <img 
              onClick={() => navigate("/cart/items")}
              src="/icons/cart-icon.png" alt="Cart" 
              className="w-7 h-7 cursor-pointer hover:scale-115 transition-transform" />
            <p onClick={() => setIsMenuOpen(!isMenuOpen)}
               className="text-lg text-blue-200 font-semibold cursor-pointer hover:text-blue-100"
            >
              {user}
              <span className=" ml-1 text-md">{isMenuOpen ? "▾" : "▸"}</span>
            </p>
            {isMenuOpen && <UserMenu handleLogout={handleLogout} setIsMenuOpen={setIsMenuOpen} />}
          </div>
        )}
      </div>
    </header>
  );
}
