import { Link, useRouter } from "@tanstack/react-router";
import { useAuth } from "../hooks/useAuth";
import { Button } from "./ui/button";
import { useState } from "react";

export function Navbar() {
  const { logout, isPending, isAuthenticated } = useAuth();
  const router = useRouter();
  const [isLoggingOut, setIsLoggingOut] = useState<boolean>(false);

  console.log(`useAuth isLoading is ${isPending}`);

  const handleLogout = async () => {
    if (window.confirm("Are you sure you want to logout?")) {
      setIsLoggingOut(true);
      try {
        await logout();
        await router.invalidate();
        router.navigate({ to: "/" });
      } catch (error) {
        throw new Error(`Logout failed. ${error}`);
      } finally {
        setIsLoggingOut(false);
      }
    }
  };

  return (
    <nav className="flex justify-between p-4">
      <div className="flex gap-4">
        <Button variant="link" asChild>
          <Link
            to="/"
            activeProps={{
              className: `focus:ring-primary text-decoration-line: underline cursor-default`,
              onClick: (e) => e.preventDefault(),
            }}
          >
            Home
          </Link>
        </Button>
        <Button variant="link" asChild>
          <Link
            to="/transactions"
            activeProps={{
              className: `focus:ring-primary text-decoration-line: underline cursor-default`,
              onClick: (e) => e.preventDefault(),
            }}
          >
            Transactions
          </Link>
        </Button>
      </div>
      <div>
        {isAuthenticated ? (
          <Button
            onClick={handleLogout}
            disabled={isPending}
            className="bg-red-400 rounded"
          >
            {isPending || isLoggingOut ? "Logging out..." : "Logout"}
          </Button>
        ) : (
          <Button variant="link" asChild>
            <Link to="/login" className="bg-blue-500 rounded">
              Login
            </Link>
          </Button>
        )}
      </div>
    </nav>
  );
}
