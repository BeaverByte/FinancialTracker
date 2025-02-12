import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  addTransaction,
  QUERY_KEY_TRANSACTIONS,
} from "../services/transactions";
import { Transaction } from "../types/Transaction";

export function useAddTransaction() {
  const queryClient = useQueryClient();
  const queryKey = [QUERY_KEY_TRANSACTIONS];

  return useMutation({
    mutationFn: addTransaction,

    onMutate: async (newTransaction) => {
      await queryClient.cancelQueries({ queryKey });

      const previousTransactions =
        queryClient.getQueryData<Transaction[]>(queryKey);

      queryClient.setQueryData<Transaction[]>(
        queryKey,
        (oldTransactions = []) => [
          ...oldTransactions,
          { ...newTransaction, id: Math.random() },
        ]
      );

      return { previousTransactions }; //  This is considered 'context' for Tanstack Query, for rollback
    },

    onError: (_error, _newTransaction, context) => {
      const queryExists = context?.previousTransactions;
      console.log("Error with add query");
      if (queryExists) {
        console.log(
          `Rolling back query to ${queryKey}: ${context.previousTransactions}`
        );
        queryClient.setQueryData(queryKey, context.previousTransactions);
      }
    },

    onSettled: () => {
      console.log("Add query settled");
      queryClient.invalidateQueries({ queryKey });
    },
  });
}
