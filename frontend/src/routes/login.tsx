import {
  createFileRoute,
  redirect,
  useNavigate,
  useRouter,
  useRouterState,
} from "@tanstack/react-router";
import { useState } from "react";
import { useAuth } from "../context/AuthContext";
import { z } from "zod";

// eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
const fallback = "/" as const;

export const Route = createFileRoute("/login")({
  validateSearch: z.object({
    redirect: z.string().optional().catch(""),
  }),
  beforeLoad: ({ context, search }) => {
    if (context.auth.isAuthenticated) {
      console.log("User already authenticated, redirecting...");
      throw redirect({ to: search.redirect ?? fallback });
    }
  },
  component: LoginComponent,
});

function LoginComponent() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const router = useRouter();

  const isLoading = useRouterState({ select: (s) => s.isLoading });
  const [isSubmitting, setIsSubmitting] = useState(false);

  const search = Route.useSearch();

  const auth = useAuth();

  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      await auth.login(username, password);
      await router.invalidate();

      await navigate({ to: search.redirect ?? fallback });
    } catch (err) {
      setError("Login failed. Please check your credentials: " + err);
      setIsSubmitting(false);
    }
  };

  const isLoggingIn = isLoading || isSubmitting;

  return (
    <div>
      <h1>Login page</h1>
      {search.redirect ? (
        <p className="text-red-500">You need to login to access this page.</p>
      ) : (
        <p>Please login.</p>
      )}
      <form onSubmit={handleLogin}>
        <fieldset disabled={isLoggingIn}>
          <div>
            <label htmlFor="username">Username:</label>
            <input
              id="username"
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              autoComplete="username"
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
              autoComplete="current-password"
            />
          </div>
        </fieldset>
        {error && <p style={{ color: "red" }}>{error}</p>}
        {isLoggingIn ? <p>"Loading..."</p> : ""}
        <button type="submit">Login</button>
      </form>
    </div>
  );
}
