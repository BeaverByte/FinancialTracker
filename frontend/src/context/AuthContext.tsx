import { createContext } from "react";

export interface AuthContextType {
  isAuthenticated: boolean;
  hasConnectionError: boolean;
  isLoading: boolean;
  login: (username: string, password: string) => Promise<string>;
  logout: () => Promise<void>;
  error: Error | null;
}

// User can be logged in, would be undefined if not
export const AuthContext = createContext<AuthContextType | undefined>(
  undefined
);
