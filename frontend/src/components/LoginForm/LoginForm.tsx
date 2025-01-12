import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { loginUser } from "../../services/auth";
import { setCredentials } from "../../redux/slices/authSlice";

const LoginForm = () => {
  const dispatch = useDispatch();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const { user, token } = await loginUser(username, password);

      // Dispatch the setCredentials action with the user and token to authSlice
      dispatch(setCredentials({ user, token }));
    } catch (err) {
      setError("Login failed. Please check your credentials.");
    }
  };

  return (
    <form onSubmit={handleLogin}>
      <div>
        <label htmlFor="username">Username:</label>
        <input
          id="username"
          type="text"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
      </div>
      <div>
        <label htmlFor="password">Password:</label>
        <input
          id="password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
      </div>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <button type="submit">Login</button>
    </form>
  );
};

export default LoginForm;
