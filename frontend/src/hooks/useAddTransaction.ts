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
      await queryClient.cancelQueries({ queryKey: [QUERY_KEY_TRANSACTIONS] });

      const previousTransactions = queryClient.getQueryData<Transaction[]>([
        QUERY_KEY_TRANSACTIONS,
      ]);

      queryClient.setQueryData<Transaction[]>(
        [QUERY_KEY_TRANSACTIONS],
        (oldTransactions = []) => ({
          ...oldTransactions,
          newTransaction,
          // TODO: This works for now but has indirection, need to refactor pagination
          // content: [[...(oldTransactions?.content || []), newTransaction]],
        })
      );

      return { previousTransactions }; //  This is considered 'context' for Tanstack Query, for rollback
    },

    onError: (_error, _newTransaction, context) => {
      const queryExists = context?.previousTransactions;
      console.log("Error with add query");
      if (queryExists) {
        console.log(
          `Rolling back query to ${[QUERY_KEY_TRANSACTIONS]}: ${context.previousTransactions}`
        );
        queryClient.setQueryData(
          [QUERY_KEY_TRANSACTIONS],
          context.previousTransactions
        );
      }
    },

    onSettled: () => {
      console.log("Add query settled");
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY_TRANSACTIONS] });
    },
  });
}
