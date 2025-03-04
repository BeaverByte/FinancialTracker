import { createFileRoute } from "@tanstack/react-router";
import { useFilters } from "../hooks/useFilters";
import { keepPreviousData, useQuery } from "@tanstack/react-query";
import {
  fetchTransactions,
  getTransactions,
  QUERY_KEY_TRANSACTIONS,
} from "../services/transactions";
import Table, {
  DEFAULT_PAGE_INDEX,
  DEFAULT_PAGE_SIZE,
} from "../components/Table/TanstackTable";
import {
  sortByToState,
  stateToSortBy,
} from "../components/Table/tableSortMapper";
import { useMemo } from "react";
import { TRANSACTION_COLUMNS } from "../components/Table/TransactionColumns";
import { TransactionFilters } from "../types/Transaction";

export const Route = createFileRoute("/")({
  component: TransactionsPage,
  validateSearch: () => ({}) as TransactionFilters,
});

function HomeComponent() {
  return (
    <div>
      <h3>Welcome Home!</h3>
    </div>
  );
}

function TransactionsPage() {
  const { filters, resetFilters, setFilters } = useFilters(Route.fullPath);

  const { data } = useQuery({
    queryKey: [QUERY_KEY_TRANSACTIONS, filters],
    queryFn: () => fetchTransactions(filters),
    placeholderData: keepPreviousData,
  });

  const paginationState = {
    pageIndex: filters.pageIndex ?? DEFAULT_PAGE_INDEX,
    pageSize: filters.pageSize ?? DEFAULT_PAGE_SIZE,
  };
  const sortingState = sortByToState(filters.sortBy);
  const columns = useMemo(() => TRANSACTION_COLUMNS, []);

  console.log("Filters is " + JSON.stringify(filters));
  console.log(JSON.stringify(data));

  return (
    <div className="flex flex-col gap-2 p-2">
      <h1 className="text-2xl font-semibold mb-1">
        TanStack Table + Query + Router
      </h1>
      <Table
        data={data?.result ?? []}
        columns={columns}
        pagination={paginationState}
        paginationOptions={{
          onPaginationChange: (pagination) => {
            setFilters(
              typeof pagination === "function"
                ? pagination(paginationState)
                : pagination
            );
          },
          rowCount: data?.rowCount,
        }}
        filters={filters}
        onFilterChange={(filters) => setFilters(filters)}
        sorting={sortingState}
        onSortingChange={(updaterOrValue) => {
          const newSortingState =
            typeof updaterOrValue === "function"
              ? updaterOrValue(sortingState)
              : updaterOrValue;
          return setFilters({ sortBy: stateToSortBy(newSortingState) });
        }}
      />
      <div className="flex items-center gap-2">
        {data?.rowCount} users found
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
