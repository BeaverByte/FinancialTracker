import { queryOptions } from "@tanstack/react-query";
import {
  getTransactions,
  QUERY_KEY_TRANSACTIONS,
} from "./services/transactions";

export const transactionsQueryOptions = queryOptions({
  queryKey: [QUERY_KEY_TRANSACTIONS],
  queryFn: () => getTransactions(),
});
