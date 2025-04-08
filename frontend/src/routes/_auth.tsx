import { Outlet, createFileRoute, redirect } from "@tanstack/react-router";

export const Route = createFileRoute("/_auth")({
  beforeLoad: ({ context, location }) => {
    console.log(`Context error from auth route is ${context.auth.error}`);
    if (context.auth.hasConnectionError) {
      console.log("Connection issues...");
      throw new Error("Network Issues detected, please try again.");
    }

    if (!context.auth.isAuthenticated) {
      console.log("User not authenticated, redirecting...");
      throw redirect({
        to: "/login",
        search: {
          // Use the current location to power a redirect after login
          // (Do not use `router.state.resolvedLocation` as it can
          // potentially lag behind the actual current location)
          redirect: location.href,
        },
      });
    }
  },
  component: AuthLayout,
});

function AuthLayout() {
  return (
    <div>
      <Outlet />
    </div>
  );
}
