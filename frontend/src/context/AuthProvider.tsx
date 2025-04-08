import { redirect } from "@tanstack/react-router";
import { useState, useEffect, useMemo, useCallback } from "react";
import { useGetTransactions } from "../hooks/useGetTransactions";
import { loginUser, logoutUser } from "../services/auth";
import { AuthContext, AuthContextType } from "./AuthContext";
import { NetworkError } from "../services/errors";

export function AuthProvider({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  const { error, isLoading, isError } = useGetTransactions();
  const isLoggedIn = !isError;
  const hasConnectionError = error instanceof NetworkError;
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

  console.log(`AuthProvider error is ${error}`);
  console.log(`hasConnectionError is currently ${hasConnectionError}`);
  useEffect(() => {
    if (!isLoading) {
      setIsAuthenticated(isLoggedIn);
    }
  }, [isLoading, isLoggedIn, hasConnectionError]);

  const login = useCallback(async (username: string, password: string) => {
    try {
      const { user } = await loginUser(username, password);
      setIsAuthenticated(true);
      redirect({
        to: "/login",
      });
      console.log("User has logged in: " + user);
      return user;
    } catch (error) {
      throw new Error(`Login failed. Please check credentials: ${error}`);
    }
  }, []);

  const logout = useCallback(async () => {
    console.log("Logging out...");

    try {
      await logoutUser();
      setIsAuthenticated(false);
      console.log("User has logged out");
    } catch (error) {
      throw new Error(`Error without user logout: ${error}`);
    }
  }, []);

  const contextValue = useMemo<AuthContextType>(
    () => ({
      isAuthenticated,
      hasConnectionError,
      isLoading,
      logout,
      login,
      error,
    }),
    [isAuthenticated, hasConnectionError, isLoading, logout, login, error]
  );

  return (
    <AuthContext.Provider value={contextValue}>
      {isLoading ? null : children}
    </AuthContext.Provider>
  );
}
