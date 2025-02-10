import { createContext, useContext, useEffect, useState } from "react";
import { useGetTransactions } from "../hooks/useGetTransactions";
import { useNavigate } from "react-router";

interface AuthContextType {
  isLoggedIn: boolean;
  isLoading: boolean;
}

// User can be logged in, would be undefined if not
const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  const { isLoading, isError } = useGetTransactions();
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(true);

  const navigate = useNavigate();

  useEffect(() => {
    if (!isLoading) {
      setIsLoggedIn(!isError); // If there's an error fetching transactions, assume not logged in
    }
  }, [isLoading, isError]);

  const logout = () => {
    console.log("Logging out... (must implement)");
    // setIsLoggedIn(false); // Update state
    // navigate("/auth/login", {
    //   replace: true,
    //   state: { message: "You have been logged out." },
    // });
  };

  return (
    <AuthContext.Provider value={{ isLoggedIn, isLoading, logout }}>
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
