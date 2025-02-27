import { createRouter } from "@tanstack/react-router";
import { routeTree } from "./routeTree.gen";
import { globalQueryClient } from "./services/queryClientConfig";
import { QueryClient } from "@tanstack/react-query";
import { AuthContextType } from "./context/AuthContext";

export interface MyRouterContext {
  auth: AuthContextType;
  queryClient: QueryClient;
}

// Set up a Router instance
export const router = createRouter({
  routeTree,
  context: {
    auth: undefined!, // This will be set after we wrap the app in an AuthProvider
    queryClient: globalQueryClient,
  },
  defaultPreload: "intent",
  // Since we're using React Query, we don't want loader calls to ever be stale
  // This will ensure that the loader is always called when the route is preloaded or visited
  defaultPreloadStaleTime: 0,
  scrollRestoration: true,
});
