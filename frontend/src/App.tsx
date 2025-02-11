import "./App.css";

import Home from "./pages/Home/Home";
import { BrowserRouter, Route, Routes } from "react-router";
import { APP_ROUTES } from "./pages/routes";
import TransactionsList from "./pages/Transactions/Transactions";
import Layout from "./components/Layout";
import {
  MutationCache,
  QueryCache,
  QueryClient,
  QueryClientProvider,
} from "@tanstack/react-query";
import { EditTransaction } from "./components/Form/EditTransaction";
import { AddTransactionForm } from "./components/Form/AddTransactionForm";
import { AuthProvider } from "./context/AuthContext";
import LoginForm from "./components/Auth/LoginForm";
import Logout from "./components/Auth/Logout";
import AuthRoute from "./routes/AuthRoute";

const queryClient = new QueryClient({
  queryCache: new QueryCache({
    onError: (error, query) => {
      console.log(`Error was produced from Query ${error}`);
      if (query.state.data !== undefined) {
        console.log(
          `Global QueryClient has found error successfully: ${error}`
        );
      }
    },
  }),
  mutationCache: new MutationCache({
    // onError,
  }),
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            <Route path={APP_ROUTES.LOGIN} element={<LoginForm />} />
            <Route element={<AuthRoute />}>
              <Route path={APP_ROUTES.ROOT} element={<Layout />}>
                <Route index element={<Home />} />
                <Route
                  path={APP_ROUTES.TRANSACTIONS_LIST}
                  element={<TransactionsList />}
                />
                <Route
                  path="transactions/edit/:id"
                  element={<EditTransaction />}
                />
                <Route
                  path={APP_ROUTES.CREATE_TRANSACTION}
                  element={<AddTransactionForm />}
                />
                <Route path={APP_ROUTES.LOGOUT} element={<Logout />} />
              </Route>
            </Route>
          </Routes>
        </AuthProvider>
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default App;
