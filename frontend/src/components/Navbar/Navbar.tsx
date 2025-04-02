import { Link } from "@tanstack/react-router";

/**
 * Navbar that provides sets of links to navigate between pages
 *
 * @component
 */
export default function Navbar() {
  return (
    <nav className="border border-yellow-500">
      <div>
        <li className="nav-item">
          <Link
            to="/"
            activeProps={{
              className: "font-bold",
            }}
            activeOptions={{ exact: true }}
          >
            Home
          </Link>
        </li>
        <li className="nav-item">
          <Link to="/transactions" className="nav-link">
            Transactions
          </Link>
        </li>
      </div>
    </nav>
  );
}
