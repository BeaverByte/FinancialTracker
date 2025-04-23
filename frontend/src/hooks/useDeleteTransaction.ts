import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  deleteTransaction,
  QUERY_KEY_TRANSACTIONS,
} from "../services/transactions";
import { Transaction } from "../types/Transaction";
import { PaginatedData } from "@/types/api/types";

export function useDeleteTransaction() {
  const queryClient = useQueryClient();
  const queryKey = [QUERY_KEY_TRANSACTIONS];

  return useMutation({
    mutationFn: deleteTransaction,

    onMutate: async (id) => {
      console.log(`UseDeleteHook sees id as ${id}`);
      await queryClient.cancelQueries({ queryKey });

      const previousTransactions = queryClient.getQueryData<
        PaginatedData<Transaction>[]
      >([QUERY_KEY_TRANSACTIONS]);

      queryClient.setQueryData<PaginatedData<Transaction>>(
        [QUERY_KEY_TRANSACTIONS],
        (oldTransactions) => {
          if (!oldTransactions) return oldTransactions;
          return {
            ...oldTransactions,
            content: oldTransactions.content.filter(
              (transaction) => transaction.id !== id
            ),
          };
        }
      );

      return { previousTransactions };
    },

    onError: (error, _id, context) => {
      console.error("Error deleting transaction:", error);
      if (context?.previousTransactions) {
        queryClient.setQueryData(queryKey, context.previousTransactions);
      }
    },

    onSettled: () => {
      console.log("Settling Deleting Transaction and invalidating Queries");
      queryClient.invalidateQueries({ queryKey });
    },
  });
}
