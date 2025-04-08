import { Link, useRouter } from "@tanstack/react-router";
import { useAuth } from "../hooks/useAuth";

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
        <Link to="/" className="text-white no-underline hover:text-gray-300">
          Home
        </Link>
        <Link to="/transactions">Transactions</Link>
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
