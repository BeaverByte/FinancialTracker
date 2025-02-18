import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  QUERY_KEY_TRANSACTIONS,
  updateTransaction,
} from "../services/transactions";
import { Transaction } from "../types/Transaction";

export function useUpdateTransaction() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: updateTransaction,

    onMutate: async (updatedTransaction) => {
      await queryClient.cancelQueries({ queryKey: [QUERY_KEY_TRANSACTIONS] });

      const previousTransactions = queryClient.getQueryData<Transaction[]>([
        QUERY_KEY_TRANSACTIONS,
      ]);

      queryClient.setQueryData<Transaction[]>(
        [QUERY_KEY_TRANSACTIONS],
        (oldTransactions = []) =>
          oldTransactions.map((oldTransaction) =>
            oldTransaction.id === updatedTransaction.id
              ? { ...oldTransaction, ...updatedTransaction.updates }
              : oldTransaction
          )
      );

      return { previousTransactions };
    },
    onError: (_error, _updatedTransaction, context) => {
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
