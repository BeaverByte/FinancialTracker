import { useAuth } from "../context/AuthContext";

export function useAuthStatus() {
  const { error: authError, isLoading: isAuthLoading, isLoggedIn } = useAuth();

  return {
    authError,
    isAuthLoading,
    isLoggedIn,
  };
}
