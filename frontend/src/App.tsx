import "./App.css";

import Home from "./pages/Home/Home";
import { BrowserRouter, Route, Routes } from "react-router";
import { ROUTES } from "./pages/routes";
import TransactionsList from "./pages/Transactions/Transactions";
import Layout from "./components/Layout";
import LoginForm from "./components/LoginForm/LoginForm";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { EditTransaction } from "./components/Form/EditTransaction";
import { AddTransactionForm } from "./components/Form/AddTransactionForm";

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <Routes>
          <Route path={ROUTES.ROOT} element={<Layout />}>
            <Route index element={<Home />} />
            <Route
              path={ROUTES.TRANSACTIONSLIST}
              element={<TransactionsList />}
            />
            <Route path="transactions/edit/:id" element={<EditTransaction />} />
            <Route
              path={ROUTES.NEWTRANSACTION}
              element={<AddTransactionForm />}
            />
            <Route path={ROUTES.LOGIN} element={<LoginForm />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default App;
