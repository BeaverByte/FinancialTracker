import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  deleteTransaction,
  QUERY_KEY_TRANSACTIONS,
} from "../services/transactions";
import { Transaction } from "../types/Transaction";

export function useDeleteTransaction() {
  const queryClient = useQueryClient();
  const queryKey = [QUERY_KEY_TRANSACTIONS];

  return useMutation({
    mutationFn: deleteTransaction,

    onMutate: async (id) => {
      await queryClient.cancelQueries({ queryKey });

      const previousTransactions =
        queryClient.getQueryData<Transaction[]>(queryKey);

      queryClient.setQueryData<Transaction[]>(
        queryKey,
        (oldTransactions = []) =>
          oldTransactions.filter((transaction) => transaction.id !== id)
      );

      return { previousTransactions };
    },

    onError: (_error, _id, context) => {
      if (context?.previousTransactions) {
        queryClient.setQueryData(queryKey, context.previousTransactions);
      }
    },

    onSettled: () => {
      queryClient.invalidateQueries({ queryKey });
    },
  });
}
