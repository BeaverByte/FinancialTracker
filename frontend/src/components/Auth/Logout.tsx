import { useNavigate } from "react-router";
import { APP_ROUTES } from "../../pages/routes";
import { useAuth } from "../../context/AuthContext";

export default function Logout() {
  const { logout } = useAuth();
  const navigate = useNavigate();

  console.log("Logout Confirmation page");

  const handleLogout = () => {
    logout();
    navigate(`${APP_ROUTES.LOGIN}`, {
      replace: true,
      state: { message: "You have been logged out." },
    });
  };

  return (
    <div>
      <h2>Are you sure you want to log out?</h2>
      <button onClick={handleLogout}>Yes, Logout</button>
      <button onClick={() => navigate(APP_ROUTES.BACK)}>Cancel</button>
    </div>
  );
}
