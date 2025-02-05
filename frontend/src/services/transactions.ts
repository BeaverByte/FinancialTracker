import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { FormSchemaType } from "../types/schemas/transactionSchema";
import { TRANSACTIONS_ROUTES } from "../utility/API_ROUTES";

const QUERY_KEY_TRANSACTIONS = "transactions";

const getTransactions = async () => {
  const response = await fetch(TRANSACTIONS_ROUTES.GET_TRANSACTIONS, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
  });

  if (!response.ok) throw new Error("Failed to get transactions");

  return response.json();
};

export function UseGetTransactions() {
  return useQuery({
    queryKey: [QUERY_KEY_TRANSACTIONS],
    queryFn: getTransactions,
  });
}

export const getTransactionById = async (id: number) => {
  const response = await fetch(
    `${TRANSACTIONS_ROUTES.GET_TRANSACTIONS}/${id}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    }
  );

  if (!response.ok) throw new Error("Transaction not found");

  return response.json();
};

export function UseGetTransactionById(id: number) {
  return useQuery({
    queryKey: ["transaction", id],
    queryFn: async () => {
      console.log(`Fetching transaction with ID: ${id}`);
      return getTransactionById(id);
    },
    enabled: !!id, // Lazy Query to id
  });
}

export const addTransaction = async (data: FormSchemaType) => {
  const response = await fetch(TRANSACTIONS_ROUTES.POST_TRANSACTION, {
    method: "POST",
    headers: {
      "content-type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify(data),
  });

  if (!response.ok) throw new Error("Failed to add transaction");
  return response.json();
};

export function UseAddTransaction() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: addTransaction,
    onMutate: async (newTransaction) => {
      // Cancel queries to avoid conflicts
      await queryClient.cancelQueries({ queryKey: [QUERY_KEY_TRANSACTIONS] });

      const previousTransactions = queryClient.getQueryData([
        QUERY_KEY_TRANSACTIONS,
      ]);

      queryClient.setQueryData(
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

export const updateTransaction = async ({ id, updates }) => {
  const response = await fetch(`${TRANSACTIONS_ROUTES.PUT_TRANSACTION}/${id}`, {
    method: "PUT",
    headers: {
      "content-type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify(updates),
  });

  if (!response.ok) throw new Error("Failed to update transaction");
  return response.json();
};

export function UseUpdateTransaction() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: updateTransaction,

    onMutate: async (updatedTransaction) => {
      await queryClient.cancelQueries({ queryKey: [QUERY_KEY_TRANSACTIONS] });

      const previousTransactions = queryClient.getQueryData([
        QUERY_KEY_TRANSACTIONS,
      ]);

      queryClient.setQueryData(
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

export const deleteTransaction = async (id: number) => {
  const response = await fetch(
    `${TRANSACTIONS_ROUTES.DELETE_TRANSACTION}/${id}`,
    {
      method: "DELETE",
      headers: {
        "content-type": "application/json",
      },
      credentials: "include",
    }
  );

  if (!response.ok) throw new Error("Failed to delete transaction");
};

export function UseDeleteTransaction() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: deleteTransaction,

    onMutate: async (id) => {
      await queryClient.cancelQueries({ queryKey: [QUERY_KEY_TRANSACTIONS] });

      const previousTransactions = queryClient.getQueryData([
        QUERY_KEY_TRANSACTIONS,
      ]);

      queryClient.setQueryData(
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
