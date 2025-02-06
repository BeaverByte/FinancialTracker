import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  addTransaction,
  QUERY_KEY_TRANSACTIONS,
} from "../services/transactions";
import { Transaction } from "../types/Transaction";

export function useAddTransaction() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: addTransaction,
    onMutate: async (newTransaction) => {
      // Cancel queries to avoid conflicts
      await queryClient.cancelQueries({ queryKey: [QUERY_KEY_TRANSACTIONS] });

      const previousTransactions = queryClient.getQueryData<Transaction[]>([
        QUERY_KEY_TRANSACTIONS,
      ]);

      queryClient.setQueryData<Transaction[]>(
        [QUERY_KEY_TRANSACTIONS],
        (oldTransactions = []) => [
          ...oldTransactions,
          { ...newTransaction, id: Math.random() },
        ]
      );

      return { previousTransactions };
    },

    onError: (_error, _newTransaction, context) => {
      if (context?.previousTransactions) {
        queryClient.setQueryData(
          ["transactions"],
          context.previousTransactions
        );
      }
    },

    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY_TRANSACTIONS] });
    },
  });
}
