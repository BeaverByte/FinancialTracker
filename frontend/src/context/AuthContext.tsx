import { createContext, useContext, useEffect, useState } from "react";
import { useGetTransactions } from "../hooks/useGetTransactions";
import { loginUser, logoutUser } from "../services/auth";
import { redirect } from "@tanstack/react-router";

export interface AuthContextType {
  isAuthenticated: boolean;
  isLoading: boolean;
  logout: () => Promise<void>;
  login: (username: string, password: string) => Promise<void>;
  error: Error | null;
}

// User can be logged in, would be undefined if not
const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  const { error, isLoading, isError } = useGetTransactions();
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

  useEffect(() => {
    if (!isLoading) {
      setIsAuthenticated(!isError); // If there's an error fetching transactions, assume not logged in
    }
  }, [isLoading, isError]);

  // TODO: Need to refresh state after user logout

  const login = async (username: string, password: string) => {
    try {
      const { user } = await loginUser(username, password);
      setIsAuthenticated(true);
      redirect({
        // to: APP_ROUTES.LOGIN,
        to: "/login",
      });
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
      setIsAuthenticated(false);
      console.log("User has logged out");
    } catch (err) {
      console.error("Error without user logout");
    }
  };

  const contextValue = { isAuthenticated, isLoading, logout, login, error };

  return (
    <AuthContext.Provider value={{ ...contextValue }}>
      {isLoading ? null : children}
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
