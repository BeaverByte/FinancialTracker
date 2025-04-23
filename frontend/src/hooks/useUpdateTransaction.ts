import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  QUERY_KEY_TRANSACTIONS,
  updateTransaction,
} from "../services/transactions";
import { Transaction } from "../types/Transaction";
import { useNavigate } from "@tanstack/react-router";

export function useUpdateTransaction() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: updateTransaction,

    onMutate: async (updatedTransaction) => {
      // Cancel any outgoing refetches
      // (so they don't overwrite optimistic update)
      await queryClient.cancelQueries({
        queryKey: [
          QUERY_KEY_TRANSACTIONS,
          { transactionId: updatedTransaction.id },
        ],
      });

      const previousTransaction = queryClient.getQueryData([
        QUERY_KEY_TRANSACTIONS,
        { transactionId: updatedTransaction.id },
      ]);

      console.log("PreviousTransaction is ", previousTransaction);

      queryClient.setQueryData<Transaction[]>(
        [QUERY_KEY_TRANSACTIONS, { transactionId: updatedTransaction.id }],
        (oldTransactions = []) =>
          oldTransactions.map(
            (transaction) =>
              transaction.id === updatedTransaction.id
                ? { ...transaction, ...updatedTransaction } // Replace updated transaction
                : transaction // Keep other transactions unchanged
          )
      );

      return { previousTransaction, updatedTransaction };
    },
    onError: (error, _updatedTransaction, context) => {
      console.error("Error updating transaction:", error);
      queryClient.setQueryData(
        [QUERY_KEY_TRANSACTIONS, context?.updatedTransaction.id],
        context?.previousTransaction
      );
    },

    onSettled: () => {
      queryClient.invalidateQueries({
        queryKey: [QUERY_KEY_TRANSACTIONS],
      });

      navigate({ to: "/transactions", search: (prev) => prev });
    },
  });
}
