import "./App.css";

import Home from "./pages/Home/Home";
import { BrowserRouter, Route, Routes } from "react-router";
import { ROUTES } from "./pages/routes";
import Transactions from "./pages/Transactions/Transactions";
import Layout from "./components/Layout";
import LoginForm from "./components/LoginForm/LoginForm";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path={ROUTES.ROOT} element={<Layout />}>
          <Route index element={<Home />} />
          <Route path={ROUTES.TRANSACTIONS} element={<Transactions />} />
          <Route path={ROUTES.LOGIN} element={<LoginForm />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
