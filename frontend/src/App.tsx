import "./App.css";

import { QueryClientProvider } from "@tanstack/react-query";
import { globalQueryClient as queryClient } from "./services/queryClientConfig";
import { RouterProvider } from "@tanstack/react-router";
import { AuthProvider, useAuth } from "./context/AuthContext";
import { router } from "./router";

declare module "@tanstack/react-router" {
  interface Register {
    router: typeof router;
  }
}

function InnerApp() {
  const auth = useAuth();
  return <RouterProvider router={router} context={{ auth }} />;
}

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <InnerApp />
      </AuthProvider>
    </QueryClientProvider>
  );
}

export default App;
