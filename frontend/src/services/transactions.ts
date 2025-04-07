import { TransactionFormSchema } from "../types/schemas/transactionSchema";
import { API_ROUTES } from "../utils/API_ROUTES";
import { Transaction, TransactionFilters } from "../types/Transaction";
import { PaginatedData } from "../types/api/types";

export const QUERY_KEY_TRANSACTIONS = "transactions";

const DEFAULT_PAGE = 0;
const DEFAULT_PAGE_SIZE = 10;

export class UnauthorizedError extends Error {
  constructor(message: string) {
    super(message);
    this.name = "UnauthorizedError";
  }
}

export class NetworkError extends Error {
  constructor(message: string) {
    super(message);
    this.name = "NetworkError";
  }
}

const fetchData = async <Type>(
  url: string,
  options: RequestInit = {}
): Promise<Type> => {
  try {
    console.log(
      `Fetching Data at ${url} with options of ${JSON.stringify(options)}`
    );
    const response = await fetch(url, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        ...(options.headers || {}),
      },
      credentials: "include",
    });

    if (!response.ok) {
      if (response.status === 401) {
        throw new UnauthorizedError("Session expired. Please log in again.");
      }
      throw new Error(`Failed to fetch: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error(`Error fetching data: ${error}`);
    if (error instanceof TypeError) {
      throw new NetworkError(
        "Could not connect to server. Please check your internet connection."
      );
    }
    throw error;
  }
};

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

  const searchParams = new URLSearchParams();

  if (sortBy) {
    const [field, order] = sortBy.split(".");
    const sort = `${field},${order}`;
    searchParams.append("sort", sort);
  }

  searchParams.append("page", pageIndex.toString());
  searchParams.append("size", pageSize.toString());

  // Appending filter property to url
  Object.keys(filters).forEach((key) => {
    const filterValue = filters[key as keyof Transaction];
    if (filterValue !== undefined && filterValue !== "") {
      searchParams.append(key, filterValue.toString());
    }
  });

  const url = `${API_ROUTES.TRANSACTIONS.GET_TRANSACTIONS}?${searchParams.toString()}`;
  console.log(`Final URL: ${url}`);

  const response = await fetchData<PaginatedData<Transaction>>(url, {
    method: "GET",
  });

  console.log(`Fetched Data is ${JSON.stringify(response)}`);

  const transactions = response.content;
  const totalElements = response.totalElements;

  return {
    content: transactions,
    totalElements,
  };
}

export const getTransactions = async (): Promise<
  PaginatedData<Transaction>
> => {
  const url = API_ROUTES.TRANSACTIONS.GET_TRANSACTIONS;
  return await fetchData<PaginatedData<Transaction>>(url, { method: "GET" });
};

export class TransactionNotFoundError extends Error {}

export const getTransactionById = async (id: string): Promise<Transaction> => {
  const url = `${API_ROUTES.TRANSACTIONS.GET_TRANSACTIONS}/${id}`;
  const transaction = await fetchData<Transaction>(url, { method: "GET" });
  if (!transaction) {
    throw new TransactionNotFoundError(
      `Transaction with id "${id}" not found!`
    );
  }
  return transaction;
};

export const addTransaction = async (
  data: TransactionFormSchema
): Promise<Transaction> => {
  const url = API_ROUTES.TRANSACTIONS.POST_TRANSACTION;
  const options = {
    method: "POST",
    body: JSON.stringify(data),
  };
  return await fetchData<Transaction>(url, options);
};

type updateTransactionPayload = {
  id: number;
  transaction: Transaction;
};

export const updateTransaction = async ({
  id,
  transaction,
}: updateTransactionPayload): Promise<Transaction> => {
  const url = `${API_ROUTES.TRANSACTIONS.PUT_TRANSACTION}/${id}`;
  const options = {
    method: "PUT",
    body: JSON.stringify(transaction),
  };
  return await fetchData<Transaction>(url, options);
};

export const deleteTransaction = async (id: number) => {
  const url = `${API_ROUTES.TRANSACTIONS.DELETE_TRANSACTION}/${id}`;
  await fetchData<void>(url, { method: "DELETE" });
};
