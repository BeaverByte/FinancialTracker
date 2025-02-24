import { APP_ROUTES } from "../../pages/routes";
import { useAuth } from "../../context/AuthContext";
import { Link } from "@tanstack/react-router";

/**
 * Navbar that provides sets of links to navigate between pages
 *
 * @component
 */
export default function Navbar() {
  return (
    <nav>
      <div>
        <li className="nav-item">
          <Link
            to={APP_ROUTES.ROOT}
            activeProps={{
              className: "font-bold",
            }}
            activeOptions={{ exact: true }}
          >
            Home
          </Link>
        </li>{" "}
        <li className="nav-item">
          <Link to={APP_ROUTES.TRANSACTIONS_LIST} className="nav-link">
            Transactions
          </Link>
        </li>{" "}
        <li className="nav-item">
          <Link to={APP_ROUTES.CREATE_TRANSACTION} className="nav-link">
            New Transaction
          </Link>
        </li>{" "}
      </div>
    </nav>
  );
}
