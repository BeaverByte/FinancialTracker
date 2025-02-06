import { useQuery } from "@tanstack/react-query";
import { Transaction } from "../types/Transaction";
import { getTransactionById } from "../services/transactions";

export function useGetTransactionById(id: number) {
  return useQuery<Transaction>({
    queryKey: ["transaction", id],
    queryFn: async () => {
      console.log(`Fetching transaction with ID: ${id}`);
      return getTransactionById(id);
    },
    enabled: !!id, // Lazy Query to id
  });
}
