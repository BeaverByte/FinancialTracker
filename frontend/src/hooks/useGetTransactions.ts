import { useQuery } from "@tanstack/react-query";
import {
  getTransactions,
  QUERY_KEY_TRANSACTIONS,
} from "../services/transactions";

export function useGetTransactions() {
  return useQuery({
    queryKey: [QUERY_KEY_TRANSACTIONS],
    queryFn: getTransactions,
  });

  // const query = useQuery({
  //   queryKey: [QUERY_KEY_TRANSACTIONS],
  //   queryFn: getTransactions,
  // });

  // return query;
}
