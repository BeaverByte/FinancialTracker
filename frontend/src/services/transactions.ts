import { TransactionFormSchema } from "../types/schemas/transactionSchema";
import { API_ROUTES } from "../utils/API_ROUTES";
import { Transaction, TransactionFilters } from "../types/Transaction";
import { PaginatedData } from "../types/api/types";

export const QUERY_KEY_TRANSACTIONS = "transactions";

const DEFAULT_PAGE = 0;
const DEFAULT_PAGE_SIZE = 10;

export class UnauthorizedError extends Error {
  constructor(message) {
    super(message);
    this.name = "UnauthorizedError";
  }
}

export class NetworkError extends Error {
  constructor(message) {
    super(message);
    this.name = "NetworkError";
  }
}

interface FetchItemOptions {
  method: string;
  body?: unknown;
}

class FetchError extends Error {
  constructor(
    public res: Response,
    message?: string
  ) {
    super(message);
  }
}

export async function fetchTransactions(
  filtersAndPagination: TransactionFilters
): Promise<PaginatedData<Transaction>> {
  console.log("fetchTransactions", filtersAndPagination);

  const {
    pageIndex = DEFAULT_PAGE,
    pageSize = DEFAULT_PAGE_SIZE,
    sortBy,
    ...filters
  } = filtersAndPagination;

  console.log("SortBy is " + sortBy);

  try {
    const response = await fetch(API_ROUTES.TRANSACTIONS.GET_TRANSACTIONS, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    if (!response.ok) {
      if (response.status === 401) {
        throw new UnauthorizedError("Session expired. Please log in again: ");
      }
      throw new Error(`Failed to get transactions: ${response.status}`);
    }
    const requestedData = await response.json();

    if (sortBy) {
      const [field, order] = sortBy.split(".");
      requestedData.sort((a, b) => {
        const aValue = a[field as keyof Transaction];
        const bValue = b[field as keyof Transaction];

        if (aValue === bValue) return 0;
        if (order === "asc") return aValue > bValue ? 1 : -1;
        return aValue < bValue ? 1 : -1;
      });
    }

    const filteredData = requestedData.filter((transaction: Transaction) => {
      return Object.keys(filters).every((key) => {
        const filter = filters[key as keyof Transaction];
        if (filter === undefined || filter === "") return true;

        const value = transaction[key as keyof Transaction];
        if (typeof value === "number") return value === +filter;

        return value.toLowerCase().includes(`${filter}`.toLowerCase());
      });
    });

    return {
      result: filteredData.slice(
        pageIndex * pageSize,
        (pageIndex + 1) * pageSize
      ),
      rowCount: filteredData.length,
    };

    // return response.json();
  } catch (error) {
    console.error(`Response not returned in fetch:
    (${error})`);
    if (error instanceof TypeError) {
      throw new NetworkError(
        "Could not connect to server. Please check your internet connection"
      );
    }
    throw error;
  }
}

export const getTransactions = async () => {
  try {
    const response = await fetch(API_ROUTES.TRANSACTIONS.GET_TRANSACTIONS, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    if (!response.ok) {
      if (response.status === 401) {
        throw new UnauthorizedError("Session expired. Please log in again: ");
      }
      throw new Error(`Failed to get transactions: ${response.status}`);
    }
    return response.json();
  } catch (error) {
    console.error(`Response not returned in fetch:
    (${error})`);
    if (error instanceof TypeError) {
      throw new NetworkError(
        "Could not connect to server. Please check your internet connection"
      );
    }
    throw error;
  }
};

export class TransactionNotFoundError extends Error {}

export const getTransactionById = async (id: string) => {
  const response = await fetch(
    `${API_ROUTES.TRANSACTIONS.GET_TRANSACTIONS}/${id}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    }
  );

  if (!response.ok)
    throw new TransactionNotFoundError(
      `Transaction with id "${id}" not found!`
    );

  return response.json();
};

export const addTransaction = async (data: TransactionFormSchema) => {
  const response = await fetch(API_ROUTES.TRANSACTIONS.POST_TRANSACTION, {
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
  const response = await fetch(
    `${API_ROUTES.TRANSACTIONS.PUT_TRANSACTION}/${id}`,
    {
      method: "PUT",
      headers: {
        "content-type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(updates),
    }
  );

  if (!response.ok) throw new Error("Failed to update transaction");
  return response.json();
};

export const deleteTransaction = async (id: number) => {
  const response = await fetch(
    `${API_ROUTES.TRANSACTIONS.DELETE_TRANSACTION}/${id}`,
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
