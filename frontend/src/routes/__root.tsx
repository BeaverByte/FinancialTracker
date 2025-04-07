import {
  ErrorComponent,
  Link,
  Outlet,
  createRootRouteWithContext,
  retainSearchParams,
} from "@tanstack/react-router";
import MainLayout from "../components/MainLayout";
import { MyRouterContext } from "../router";
import { NetworkError } from "../services/transactions";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { z } from "zod";
import { zodValidator } from "@tanstack/zod-adapter";

const searchSchema = z.object({
  rootValue: z.string().optional(),
});

export const Route = createRootRouteWithContext<MyRouterContext>()({
  component: RootComponent,
  validateSearch: zodValidator(searchSchema),
  search: {
    middlewares: [retainSearchParams(["rootValue"])],
  },
  errorComponent: ({ error }) => {
    if (error instanceof NetworkError) {
      // Render a custom error message
      return <div>{error.message}</div>;
    }
    // Fallback to the default ErrorComponent
    return <CustomErrorComponent error={error} />;
  },
  notFoundComponent: NotFound,
});

export function CustomErrorComponent({ error }: Readonly<{ error: Error }>) {
  return (
    <div>
      <ErrorComponent error={error} />
      <Link to="/">Start Over</Link>
    </div>
  );
}

function NotFound() {
  return (
    <div>
      <p>This is the notFoundComponent configured on root route</p>
      <Link to="/">Start Over</Link>
    </div>
  );
}

function RootComponent() {
  return (
    <>
      <MainLayout />
      <main>
        <Outlet />
        <ReactQueryDevtools initialIsOpen={false} />
      </main>
    </>
  );
}
