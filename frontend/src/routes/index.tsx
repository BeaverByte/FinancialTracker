import { createFileRoute } from "@tanstack/react-router";
import { useFilters } from "../hooks/useFilters";
import { keepPreviousData, useQuery } from "@tanstack/react-query";
import {
  fetchTransactions,
  QUERY_KEY_TRANSACTIONS,
} from "../services/transactions";
import Table, {
  DEFAULT_PAGE_INDEX,
  DEFAULT_PAGE_SIZE,
} from "../components/Table/TanstackTable";
import {
  convertSortByInURLToState,
  convertStateToSortByInURL,
} from "../components/Table/tableSortMapper";
import { useMemo } from "react";
import { TRANSACTION_COLUMNS } from "../components/Table/TransactionColumns";
import { TransactionFilters } from "../types/Transaction";
import { SortingState } from "@tanstack/react-table";
import { z } from "zod";

const transactionsSearchSchema = z.object({
  page: z.number().optional().catch(1),
  filter: z.string().optional(),
  sortBy: z
    .string()
    .regex(/^(id|date|merchant|account|category|amount|note)\.(asc|desc)$/)
    .optional(),
});

type transactionsSearch = z.infer<typeof transactionsSearchSchema>;

export const Route = createFileRoute("/")({
  component: TransactionsPage,
  // validateSearch: () => ({}) as TransactionFilters,
  validateSearch: (search) =>
    transactionsSearchSchema.parse(search) as TransactionFilters,
});

function HomeComponent() {
  return (
    <div>
      <h3>Welcome Home!</h3>
    </div>
  );
}

function TransactionsPage() {
  // Filters are set based off path of current route
  const { filters, resetFilters, setFilters } = useFilters(Route.fullPath);

  console.log("Filters are " + JSON.stringify(filters));

  const { data: queriedData } = useQuery({
    queryKey: [QUERY_KEY_TRANSACTIONS, filters],
    queryFn: () => fetchTransactions(filters),
    placeholderData: keepPreviousData,
  });

  const transactions = queriedData?.result;

  const paginationState = {
    pageIndex: filters.pageIndex ?? DEFAULT_PAGE_INDEX,
    pageSize: filters.pageSize ?? DEFAULT_PAGE_SIZE,
  };
  const sortingState = convertSortByInURLToState(filters.sortBy);
  const transactionsColumns = useMemo(() => TRANSACTION_COLUMNS, []);

  // Tanstack Updaters can either be raw values or callback functions, parsing is needed
  function handleSortingChange(
    updaterOrValue: SortingState | ((prevState: SortingState) => SortingState)
  ) {
    console.log("Sort change handler triggered");
    const newSortingState =
      typeof updaterOrValue === "function"
        ? updaterOrValue(sortingState)
        : updaterOrValue;
    // Change to sorting will update Filter/URL Params
    console.log(convertStateToSortByInURL(newSortingState));
    return setFilters({ sortBy: convertStateToSortByInURL(newSortingState) });
  }

  return (
    <div className="flex flex-col gap-2 p-2">
      <h1 className="text-2xl font-semibold mb-1">Transactions</h1>
      <Table
        data={transactions ?? []}
        columns={transactionsColumns}
        pagination={paginationState}
        paginationOptions={{
          onPaginationChange: (pagination) => {
            setFilters(
              typeof pagination === "function"
                ? pagination(paginationState)
                : pagination
            );
          },
          rowCount: queriedData?.rowCount,
        }}
        filters={filters}
        onFilterChange={(filters) => setFilters(filters)}
        sorting={sortingState}
        onSortingChange={handleSortingChange}
      />
      <div className="flex items-center gap-2">
        {queriedData?.rowCount} users found
        <button
          className="border rounded p-1 disabled:text-gray-500 disabled:cursor-not-allowed"
          onClick={resetFilters}
          disabled={Object.keys(filters).length === 0}
        >
          Reset Filters
        </button>
      </div>
      <pre>{JSON.stringify(filters, null, 2)}</pre>
    </div>
  );
}
