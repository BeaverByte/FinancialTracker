// useAuthStatus.ts
import { useAuth } from "../context/AuthContext"; // Assuming useAuth() provides authentication context

export function useAuthStatus() {
  const { error: authError, isLoading: isAuthLoading, isLoggedIn } = useAuth();

  return {
    authError,
    isAuthLoading,
    isLoggedIn,
  };
}
