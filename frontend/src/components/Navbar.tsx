import { Link, useMatchRoute, useRouter } from "@tanstack/react-router";
import { useAuth } from "../hooks/useAuth";
import { Button } from "./ui/button";
import { useState } from "react";
import { NavLinkButton } from "./NavLinkButton";

export function Navbar() {
  const { logout, isPending, isAuthenticated } = useAuth();
  const [isLoggingOut, setIsLoggingOut] = useState<boolean>(false);
  const router = useRouter();

  const matchRoute = useMatchRoute();
  const isLoginRoute = matchRoute({ to: "/login" });

  const handleLogout = async () => {
    if (!window.confirm("Are you sure you want to logout?")) return;

    setIsLoggingOut(true);
    try {
      await logout();
      await router.invalidate();
      router.navigate({ to: "/" });
    } catch (error) {
      console.error("Logout failed", error);
      throw new Error(`Logout failed. ${error}`);
    } finally {
      setIsLoggingOut(false);
    }
  };

  const shouldShowLogin = !isAuthenticated && !isLoginRoute;

  return (
    <nav className="flex justify-between p-4">
      <div className="flex gap-4">
        <NavLinkButton to="/">Home</NavLinkButton>
        <NavLinkButton to="/transactions">Transactions</NavLinkButton>
      </div>
      <div>
        {isAuthenticated ? (
          <Button
            onClick={handleLogout}
            disabled={isPending}
            className="select-none bg-red-400 rounded"
          >
            {isPending || isLoggingOut ? "Logging out..." : "Logout"}
          </Button>
        ) : null}

        {shouldShowLogin ? (
          <Button variant="link" className="select-none" asChild>
            <Link to="/login" className="bg-blue-500 rounded">
              Login
            </Link>
          </Button>
        ) : null}
      </div>
    </nav>
  );
}
