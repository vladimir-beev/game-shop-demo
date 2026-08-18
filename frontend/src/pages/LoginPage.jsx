import { useState, useContext } from "react";
import { AuthContext } from "../auth/AuthContext";
import { useNavigate, useSearchParams, Navigate } from "react-router-dom";

export default function LoginPage() {
  const navigate = useNavigate();
  const { login, accessToken } = useContext(AuthContext);
  const [params] = useSearchParams();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [invalidCreds, setInvalidCreds] = useState(false);
  const [errors, setErrors] = useState({});

  const redirectTo = params.get("redirect") || "/";

  if (accessToken) {
    return <Navigate to={redirectTo} replace />;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setInvalidCreds(false);

    try {
      const newErrors = {};

      if (!email) {
        newErrors.email = "Email is required";
      }

      if (!password) {
        newErrors.password = "Password is required";
      }

      setErrors(newErrors);

      if (Object.keys(newErrors).length > 0) return;

      await login(email, password);
      navigate(redirectTo);
    } 
    catch (error) {
      setInvalidCreds(true);
    }
  }

  return (
    <div className="flex flex-1 justify-center items-center">
      <form
        onSubmit={handleSubmit}
        className="flex bg-gray-200 p-6 flex-col justify-center rounded-xl shadow-lg w-90"
      >
        <h2 className="text-2xl font-bold mb-6 text-center">Login</h2>
        {invalidCreds && (
          <p className="bg-red-200 text-red-700 font-semibold px-2 py-2 mb-2 rounded">
            Invalid email or password.
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
          <p className="text-red-600 text-sm font-semibold mt-1">{errors.email}</p>
        )}

        <input
          type="password"
          placeholder="Password"
          className="w-full p-2 border border-gray-600 rounded-md mt-3"
          value={password.trim()}
          maxLength={32}
          onChange={(e) => setPassword(e.target.value.replace(/\s+/g, ""))}
          onKeyDown={(e) => {
            if (e.key === " ") {
              e.preventDefault();
            }
          }}
        />
        {errors.password && (
          <p className="text-red-600 text-sm font-semibold mt-1">{errors.password}</p>
        )}

        <button className="w-full bg-blue-600 text-white font-bold py-2 mt-4 cursor-pointer rounded-md hover:bg-blue-700">
          Login
        </button>

        <p className="mt-4 text-sm text-gray-700">
          Don't have an account?{" "}
          <span
            onClick={() => navigate("/register")}
            className="text-blue-600 cursor-pointer hover:underline"
          >
            Register here
          </span>
        </p>
      </form>
    </div>
  );
}
