import { Link } from "react-router";
import { ROUTES } from "../../pages/routes";
import { useAuth } from "../../context/AuthContext";

/**
 * Navbar that provides sets of links to navigate between pages
 *
 * @component
 */
export default function Navbar() {
  const { isLoggedIn, logout } = useAuth();

  return (
    <nav>
      <div>
        <li className="nav-item">
          <Link to={ROUTES.ROOT} className="nav-link">
            Home
          </Link>
        </li>
        <li className="nav-item">
          <Link to={ROUTES.TRANSACTIONSLIST} className="nav-link">
            Transactions
          </Link>
        </li>
        <li className="nav-item">
          <Link to={ROUTES.NEWTRANSACTION} className="nav-link">
            New Transaction
          </Link>
        </li>
        {!isLoggedIn ? (
          <li className="nav-item">
            <Link to={ROUTES.LOGIN} className="nav-link">
              Login
            </Link>
          </li>
        ) : (
          <li className="nav-item">
            <Link to={""}>Logout</Link>
          </li>
        )}
      </div>
    </nav>
  );
}
