import {
  QueryClient,
  useMutation,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";
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
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY_TRANSACTIONS] }); // ✅ Refetch transactions
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
    onSuccess: () => {
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
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY_TRANSACTIONS] });
    },
  });
}
