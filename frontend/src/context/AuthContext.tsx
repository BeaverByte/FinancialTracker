import { createContext, useContext, useEffect, useState } from "react";
import { useGetTransactions } from "../hooks/useGetTransactions";
import { useNavigate } from "react-router";
import { loginUser, logoutUser } from "../services/auth";
import { useQueryClient } from "@tanstack/react-query";
import { APP_ROUTES } from "../pages/routes";

interface AuthContextType {
  isLoggedIn: boolean;
  isLoading: boolean;
  logout: () => Promise<void>;
  login: (username: string, password: string) => Promise<void>;
  data: any;
  error: Error | null;
}

// User can be logged in, would be undefined if not
const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  const { data, error, isLoading, isError } = useGetTransactions();
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(true);

  const navigate = useNavigate();

  useEffect(() => {
    if (!isLoading) {
      setIsLoggedIn(!isError); // If there's an error fetching transactions, assume not logged in
    }
  }, [isLoading, isError]);

  // TODO: Need to refresh state after user logout

  const login = async (username: string, password: string) => {
    try {
      const { user } = await loginUser(username, password);
      setIsLoggedIn(true);
      navigate(APP_ROUTES.ROOT);
      console.log("User has logged in: " + user);
      return user;
    } catch (err) {
      throw new Error("Login failed.");
    }
  };

  const logout = async () => {
    console.log("Logging out...");

    try {
      await logoutUser();
      setIsLoggedIn(false);
      console.log("User has logged out");
    } catch (err) {
      console.error("Error without user logout");
    }
  };

  return (
    <AuthContext.Provider
      value={{ isLoggedIn, isLoading, logout, login, data, error }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
