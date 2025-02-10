import { TransactionFormSchema } from "../types/schemas/transactionSchema";
import { TRANSACTIONS_ROUTES } from "../utility/API_ROUTES";
import { Transaction } from "../types/Transaction";

export const QUERY_KEY_TRANSACTIONS = "transactions";

export class UnauthorizedError extends Error {
  constructor(message = "Unauthorized") {
    super(message);
    this.name = "UnauthorizedError";
  }
}

export const getTransactions = async () => {
  const response = await fetch(TRANSACTIONS_ROUTES.GET_TRANSACTIONS, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
  });

  if (!response.ok) {
    if (response.status === 401) {
      throw new UnauthorizedError("Session expired. Please log in again.");
    }
    throw new Error(`Failed to get transactions: ${response.status}`);
  }
  return response.json();
};

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

export const addTransaction = async (data: TransactionFormSchema) => {
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

type updateTransactionPayload = {
  id: number;
  updates: Transaction;
};

export const updateTransaction = async ({
  id,
  updates,
}: updateTransactionPayload) => {
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
