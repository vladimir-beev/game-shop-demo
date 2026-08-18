import { useState, useContext } from "react";
import { useNavigate, Navigate } from "react-router-dom";
import { AuthContext } from "../auth/AuthContext";

export default function RegisterPage() {
  const navigate = useNavigate();
  const { register, accessToken } = useContext(AuthContext);
  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState({});
  const [invalidMessage, setInvalidMessage] = useState("");

  if (accessToken) {
    return <Navigate to="/" replace />;
  }

  async function handleSubmit(e) {
    e.preventDefault();

    const newErrors = {};
    setInvalidMessage("");

    if (!email) {
      newErrors.email = "Email is required";
    } 

    if (!username) {
      newErrors.username = "Username is required";
    } 
    else if (username.length < 5) {
      newErrors.username = "Username must be at least 5 characters";
    }

    if (!password) {
      newErrors.password = "Password is required";
    } 
    else if (password.length < 6) {
      newErrors.password = "Password must be at least 6 characters";
    }

    setErrors(newErrors);

    if (Object.keys(newErrors).length > 0) return;

    try {
      await register(email, username, password);
      navigate("/login");
    } 
    catch (error) {
      if (error.response && error.response.status === 409) {
        setInvalidMessage(error.response.data || "Email or username already exists.");
        return;
      }

      console.error("Registration failed:", error);
      setInvalidMessage("Registration failed. Please try again.");
    }
  }

  return (
    <div className="flex flex-1 justify-center items-center">
      <form
        onSubmit={handleSubmit}
        className="flex bg-gray-200 p-6 flex-col justify-center rounded-xl shadow-lg w-90"
      >
        <h2 className="text-2xl font-bold mb-6 text-center">Register</h2>
        {invalidMessage && (
          <p className="bg-red-200 text-red-700 font-semibold px-2 py-2 mb-2 rounded">
            {invalidMessage}
          </p>
        )}

        <input
          type="email"
          placeholder="Email"
          className="w-full p-2 border border-gray-600 rounded-md"
          value={email.trim()}
          maxLength={254}
          onChange={(e) => setEmail(e.target.value.replace(/\s+/g, ""))}
          onKeyDown={(e) => {
            if (e.key === " ") {
              e.preventDefault();
            }
          }}
        />
        {errors.email && (
          <p className="text-red-600 text-sm font-semibold mt-1">
            {errors.email}
          </p>
        )}

        <input
          type="text"
          placeholder="Username"
          className="w-full p-2 border border-gray-600 rounded-md mt-3"
          value={username}
          maxLength={32}
          onChange={(e) => setUsername(e.target.value.replace(/\s+/g, ""))}
          onKeyDown={(e) => {
            if (e.key === " ") {
              e.preventDefault();
            }
          }}
        />
        {errors.username && (
          <p className="text-red-600 text-sm font-semibold mt-1">
            {errors.username}
          </p>
        )}

        <input
          type="password"
          placeholder="Password"
          className="w-full p-2 border border-gray-600 rounded-md mt-3"
          value={password}
          maxLength={32}
          onChange={(e) => setPassword(e.target.value.replace(/\s+/g, ""))}
          onKeyDown={(e) => {
            if (e.key === " ") {
              e.preventDefault();
            }
          }}
        />
        {errors.password && (
          <p className="text-red-600 text-sm font-semibold mt-1">
            {errors.password}
          </p>
        )}

        <button className="w-full bg-green-600 text-white font-bold py-2 mt-4 cursor-pointer rounded-md hover:bg-green-700">
          Register
        </button>
        <p className="mt-4 text-sm text-gray-700">
          Already have an account?{" "}
          <span
            onClick={() => navigate("/login")}
            className="text-blue-600 cursor-pointer hover:underline"
          >
            Sign in here
          </span>
        </p>
      </form>
    </div>
  );
}
