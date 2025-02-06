import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  deleteTransaction,
  QUERY_KEY_TRANSACTIONS,
} from "../services/transactions";
import { Transaction } from "../types/Transaction";

export function useDeleteTransaction() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: deleteTransaction,

    onMutate: async (id) => {
      await queryClient.cancelQueries({ queryKey: [QUERY_KEY_TRANSACTIONS] });

      const previousTransactions = queryClient.getQueryData<Transaction[]>([
        QUERY_KEY_TRANSACTIONS,
      ]);

      queryClient.setQueryData<Transaction[]>(
        [QUERY_KEY_TRANSACTIONS],
        (oldTransactions = []) =>
          oldTransactions.filter((transaction) => transaction.id !== id)
      );

      return { previousTransactions };
    },

    onError: (_error, _id, context) => {
      if (context?.previousTransactions) {
        queryClient.setQueryData(
          [QUERY_KEY_TRANSACTIONS],
          context.previousTransactions
        );
      }
    },

    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY_TRANSACTIONS] });
    },
  });
}
