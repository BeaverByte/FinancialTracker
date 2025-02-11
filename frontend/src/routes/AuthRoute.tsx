import { Outlet, useNavigate } from "react-router";
import { useAuthStatus } from "../context/useAuthStatus";

const AuthRoute = () => {
  const { authError, isAuthLoading, isLoggedIn } = useAuthStatus();
  const navigate = useNavigate();

  if (isAuthLoading) {
    return <p>Loading...</p>;
  }

  if (!isLoggedIn) {
    navigate("/auth/login");
    return <p>You are not logged in. Redirecting to login...</p>;
  }

  if (authError) {
    return <p>{authError.message}</p>;
  }

  return (
    <div>
      <Outlet />
    </div>
  );
};

export default AuthRoute;
