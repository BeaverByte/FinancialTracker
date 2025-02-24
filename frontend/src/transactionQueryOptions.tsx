import { queryOptions } from "@tanstack/react-query";
import {
  getTransactionById,
  QUERY_KEY_TRANSACTIONS,
} from "./services/transactions";

export const transactionQueryOptions = (transactionId: string) =>
  queryOptions({
    queryKey: [QUERY_KEY_TRANSACTIONS, { transactionId }],
    queryFn: () => getTransactionById(transactionId),
  });
