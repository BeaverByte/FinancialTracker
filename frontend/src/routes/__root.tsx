import {
  Link,
  Outlet,
  createRootRouteWithContext,
} from "@tanstack/react-router";
import AuthLayout from "../components/AuthLayout";
import { MyRouterContext } from "../router";

export const Route = createRootRouteWithContext<MyRouterContext>()({
  component: RootComponent,
  notFoundComponent: () => {
    return (
      <div>
        <p>This is the notFoundComponent configured on root route</p>
        <Link to="/">Start Over</Link>
      </div>
    );
  },
});

function RootComponent() {
  return (
    <>
      <AuthLayout />
      <main>
        <Outlet />
      </main>
    </>
  );
}
