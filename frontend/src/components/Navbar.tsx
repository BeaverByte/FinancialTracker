import { Link, useRouter } from "@tanstack/react-router";
import { useAuth } from "../hooks/useAuth";
import { Button } from "./ui/button";

export function Navbar() {
  const auth = useAuth();
  const router = useRouter();

  const handleLogout = () => {
    if (window.confirm("Are you sure you want to logout?")) {
      auth.logout().then(() => {
        router.invalidate().finally(() => {
          router.navigate({ to: "/" });
        });
      });
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
        {auth.isAuthenticated ? (
          <button
            onClick={handleLogout}
            className="px-4 py-2 bg-red-500 rounded"
          >
            Logout
          </button>
        ) : (
          <Link to="/login" className="px-4 py-2 bg-blue-500 rounded">
            Login
          </Link>
        )}
      </div>
    </nav>
  );
}
