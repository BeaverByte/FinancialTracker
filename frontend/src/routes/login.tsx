import {
  createFileRoute,
  redirect,
  useNavigate,
  useRouterState,
} from "@tanstack/react-router";
import { useState } from "react";
import { z } from "zod";
import { LoginCard } from "../components/ui/login-card";
import {
  LoginFormSchema,
  LoginFormValidationSchema,
} from "../types/schemas/loginSchema";
import { useAuth } from "../hooks/useAuth";
import { router } from "../router";

// eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
const fallback = "/" as const;

export const Route = createFileRoute("/login")({
  component: LoginComponent,
  validateSearch: z.object({
    redirect: z.string().optional().catch(""),
  }),
  beforeLoad: ({ context, search }) => {
    if (context.auth.isAuthenticated) {
      console.log("User already authenticated, redirecting...");
      throw redirect({ to: search.redirect ?? fallback });
    }
  },
});

function LoginComponent() {
  const [error, setError] = useState<string | undefined>(undefined);

  const isLoading = useRouterState({ select: (s) => s.isLoading });
  const [isSubmitting, setIsSubmitting] = useState(false);

  const search = Route.useSearch();

  const auth = useAuth();

  const navigate = useNavigate();

  const handleLogin = async (data: LoginFormSchema) => {
    console.log(`Handling Login, data: ${JSON.stringify(data)}`);
    const result = LoginFormValidationSchema.safeParse(data);

    setIsSubmitting(true);

    if (!result.success) {
      console.log("Zod Error: " + result.error);
    }

    const username = data.username ?? "";
    const password = data.password ?? "";

    try {
      await auth.login(username, password);
      await router.invalidate();
      await navigate({ to: search.redirect ?? fallback });
    } catch (err) {
      setError(`${err}. Please try logging in again`);
    } finally {
      setIsSubmitting(false);
    }
  };

  const isLoggingIn = isLoading || isSubmitting;

  return (
    <LoginCard
      onSubmit={handleLogin}
      isLoggingIn={isLoggingIn}
      onCancel={() => navigate({ to: "/" })}
      error={error}
      showRedirectMessage={!!search.redirect}
    />
  );
}
