import { useQuery } from "@tanstack/react-query";
import {
  getTransactions,
  QUERY_KEY_TRANSACTIONS,
  UnauthorizedError,
} from "../services/transactions";
import { useNavigate } from "react-router";
import { useEffect } from "react";

export function useGetTransactions() {
  const navigate = useNavigate();

  const query = useQuery({
    queryKey: ["transactions"],
    queryFn: getTransactions,
  });

  useEffect(() => {
    if (query.error instanceof UnauthorizedError) {
      console.warn("Unauthorized error: Redirecting to login...");
      navigate("/auth/login", {
        replace: true,
        state: { message: query.error.message },
      });
    }
  }, [query.error, navigate]);

  return query;
}
