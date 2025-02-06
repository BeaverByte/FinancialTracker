import { useState } from "react";
import { loginUser } from "../../services/auth";
import { useGetTransactions } from "../../hooks/useGetTransactions";

const LoginForm = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);

  const { isLoading, isError } = useGetTransactions();

  const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const { user } = await loginUser(username, password);
      console.log(user + "has logged in");
    } catch (err) {
      setError("Login failed. Please check your credentials: " + err);
    }
  };

  if (!isError && !isLoading) {
    return (
      <p>
        You are already logged in. <a href="/transactions">View transactions</a>
      </p>
    );
  }

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
