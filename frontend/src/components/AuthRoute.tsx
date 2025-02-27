import { Outlet, useNavigate } from "@tanstack/react-router";
import { useAuthStatus } from "../context/useAuthStatus";
import { APP_ROUTES } from "../pages/routes";

const AuthRoute = () => {
  const { authError, isAuthLoading, isLoggedIn } = useAuthStatus();
  const navigate = useNavigate();

  if (isAuthLoading) {
    return <p>Loading...</p>;
  }

  if (!isLoggedIn) {
    navigate({ to: APP_ROUTES.LOGIN });
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
