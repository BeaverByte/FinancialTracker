import { redirect } from "@tanstack/react-router";
import { useState, useEffect } from "react";
import { useGetTransactions } from "../hooks/useGetTransactions";
import { loginUser, logoutUser } from "../services/auth";
import { AuthContext } from "./AuthContext";

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

  const login = async (username: string, password: string) => {
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
  };

  const logout = async () => {
    console.log("Logging out...");

    try {
      await logoutUser();
      setIsAuthenticated(false);
      console.log("User has logged out");
    } catch (error) {
      throw new Error(`Error without user logout: ${error}`);
    }
  };

  const contextValue = { isAuthenticated, isLoading, logout, login, error };

  return (
    <AuthContext.Provider value={{ ...contextValue }}>
      {isLoading ? null : children}
    </AuthContext.Provider>
  );
}
