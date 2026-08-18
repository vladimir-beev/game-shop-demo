import { useNavigate } from "react-router-dom";

export default function UserMenu({ handleLogout, setIsMenuOpen }) {
  const navigate = useNavigate();

  const menuItemStyle = "px-4 py-2 font-semibold border border-gray-700 hover:bg-gray-700 cursor-pointer";

  return (
    <div className="absolute top-full right-0 w-50 bg-gray-800 shadow-xl border border-gray-700 z-50">
      <ul className="flex flex-col">
        <li
          onClick={() => {
            navigate("/user/orders");
            setIsMenuOpen(false);
          }}
          className={menuItemStyle + " text-yellow-200"}
        >
          Orders
        </li>
        <li
          onClick={() => {
            navigate("/user/profile");
            setIsMenuOpen(false);
          }}
          className={menuItemStyle}
        >
          Profile
        </li>
        <li
          onClick={() => {
            handleLogout();
            setIsMenuOpen(false);
          }}
          className={menuItemStyle + " text-red-300"}
        >
          Sign Out
        </li>
      </ul>
    </div>
  );
}
