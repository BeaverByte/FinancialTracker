import { APP_ROUTES } from "../../pages/routes";
import { useAuth } from "../../context/AuthContext";
import { useNavigate, useRouter } from "@tanstack/react-router";

export default function Logout() {
  const { logout } = useAuth();
  const navigate = useNavigate();
  const { history } = useRouter();

  console.log("Logout Confirmation page");

  const handleLogout = () => {
    logout();
    navigate({ to: "/auth/login" });
  };

  return (
    <div>
      <h2>Are you sure you want to log out?</h2>
      <button onClick={handleLogout}>Yes, Logout</button>
      <button onClick={() => history.go(-1)}>Cancel</button>
    </div>
  );
}
