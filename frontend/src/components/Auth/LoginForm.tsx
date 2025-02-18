import { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { useLocation } from "react-router";

const LoginForm = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);

  const { isLoggedIn, isLoading, login } = useAuth();

  const location = useLocation();
  const message = location.state?.message;

  const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      await login(username, password);
    } catch (err) {
      setError("Login failed. Please check your credentials: " + err);
    }
  };

  if (isLoading) {
    return <p>Loading...</p>;
  }

  if (isLoggedIn) {
    return (
      <p>
        You are already logged in. <a href="/transactions">View transactions</a>
      </p>
    );
  }

  return (
    <div>
      {message && <div>{message}</div>}
      <h1>Login page</h1>
      <h2>Please login</h2>
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
    </div>
  );
};

export default LoginForm;
