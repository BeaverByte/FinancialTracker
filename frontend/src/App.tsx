import "./App.css";

import Home from "./pages/Home/Home";
import { BrowserRouter, Route, Routes } from "react-router";
import { ROUTES } from "./pages/routes";
import TransactionsList from "./pages/Transactions/Transactions";
import Layout from "./components/Layout";
import LoginForm from "./components/Auth/LoginForm";
import {
  MutationCache,
  QueryCache,
  QueryClient,
  QueryClientProvider,
} from "@tanstack/react-query";
import { EditTransaction } from "./components/Form/EditTransaction";
import { AddTransactionForm } from "./components/Form/AddTransactionForm";
import { AuthProvider } from "./context/AuthContext";

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
            <Route path={ROUTES.ROOT} element={<Layout />}>
              <Route index element={<Home />} />
              <Route
                path={ROUTES.TRANSACTIONSLIST}
                element={<TransactionsList />}
              />
              <Route
                path="transactions/edit/:id"
                element={<EditTransaction />}
              />
              <Route
                path={ROUTES.NEWTRANSACTION}
                element={<AddTransactionForm />}
              />
              <Route path={ROUTES.LOGIN} element={<LoginForm />} />
            </Route>
          </Routes>
        </AuthProvider>
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default App;
