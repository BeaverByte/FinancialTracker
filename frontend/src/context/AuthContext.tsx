import { createContext } from "react";

export interface AuthContextType {
  isAuthenticated: boolean;
  isLoading: boolean;
  logout: () => Promise<void>;
  login: (username: string, password: string) => Promise<void>;
  error: Error | null;
}

// User can be logged in, would be undefined if not
export const AuthContext = createContext<AuthContextType | undefined>(
  undefined
);
