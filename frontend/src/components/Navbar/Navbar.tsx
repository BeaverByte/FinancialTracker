import { Link } from "react-router";
import { APP_ROUTES } from "../../pages/routes";
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
          <Link to={APP_ROUTES.ROOT} className="nav-link">
            Home
          </Link>
        </li>
        <li className="nav-item">
          <Link to={APP_ROUTES.TRANSACTIONS_LIST} className="nav-link">
            Transactions
          </Link>
        </li>
        <li className="nav-item">
          <Link to={APP_ROUTES.CREATE_TRANSACTION} className="nav-link">
            New Transaction
          </Link>
        </li>
        {!isLoggedIn ? (
          <li className="nav-item">
            <Link to={APP_ROUTES.LOGIN} className="nav-link">
              Login
            </Link>
          </li>
        ) : (
          <li className="nav-item">
            <Link to={APP_ROUTES.LOGOUT} className="nav-link">
              Logout
            </Link>
            {/* <button onClick={logout}>Logout</button> */}
          </li>
        )}
      </div>
    </nav>
  );
}
