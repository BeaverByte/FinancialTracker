import { TransactionFormSchema } from "../types/schemas/transactionSchema";
import { API_ROUTES } from "../utils/API_ROUTES";
import { Transaction, TransactionFilters } from "../types/Transaction";
import { PaginatedData } from "../types/api/types";
import { fetchData } from "./fetch";

export const QUERY_KEY_TRANSACTIONS = "transactions";

const DEFAULT_PAGE = 0;
const DEFAULT_PAGE_SIZE = 10;

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
    const sortFields = sortBy.split(",");

    sortFields.forEach((fieldOrder) => {
      const [field, order] = fieldOrder.split(".");
      if (field && order) {
        searchParams.append("sort", `${field},${order}`);
      }
    });
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

export const deleteTransaction = async (id: number | string) => {
  const url = `${API_ROUTES.TRANSACTIONS.DELETE_TRANSACTION}/${id}`;
  await fetchData<void>(url, { method: "DELETE" });
};
