import {
  createFileRoute,
  Link,
  Outlet,
  useMatchRoute,
} from "@tanstack/react-router";
import { transactionsQueryOptions } from "../transactionsQueryOptions";
import { TransactionFilters } from "../types/Transaction";
import { useFilters } from "../hooks/useFilters";
import { keepPreviousData, useQuery } from "@tanstack/react-query";
import {
  fetchTransactions,
  QUERY_KEY_TRANSACTIONS,
} from "../services/transactions";
import {
  convertSortByInURLToState,
  convertStateToSortByInURL,
} from "../components/Table/tableSortMapper";
import { useMemo } from "react";
import { getTransactionColumns } from "../components/Table/TransactionColumns";
import TransactionsDataTable, {
  DEFAULT_PAGE_INDEX,
  DEFAULT_PAGE_SIZE,
} from "../components/Table/TransactionsDataTable";
import { PaginationState, SortingState, Updater } from "@tanstack/react-table";

export const Route = createFileRoute("/_auth/transactions")({
  loader: ({ context: { queryClient } }) =>
    queryClient.ensureQueryData(transactionsQueryOptions),
  validateSearch: () => ({}) as TransactionFilters,
  component: TransactionsPage,
});

function TransactionsPage() {
  console.log("Transactions Page route is ", "/_auth/transactions");
  // Filters are set based off path of current route
  const { filters, resetFilters, setFilters } = useFilters(
    "/_auth/transactions"
  );
  console.log("Filters are " + JSON.stringify(filters));

  const matchRoute = useMatchRoute();
  const matchesTransactionsRoute = matchRoute({ to: "/transactions" });

  const { data: transactionsData } = useQuery({
    queryKey: [QUERY_KEY_TRANSACTIONS, filters],
    queryFn: () => fetchTransactions(filters),
    placeholderData: keepPreviousData,
  });

  const transactions = transactionsData?.content;
  const totalElements = transactionsData?.totalElements;

  const paginationState = {
    pageIndex: filters.pageIndex ?? DEFAULT_PAGE_INDEX,
    pageSize: filters.pageSize ?? DEFAULT_PAGE_SIZE,
  };
  const sortingState = convertSortByInURLToState(filters.sortBy);
  const transactionsColumns = useMemo(() => getTransactionColumns, []);

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
    const internalSortByParam = convertStateToSortByInURL(newSortingState);
    console.log(`Internal SortByParam is ${internalSortByParam}`);

    return setFilters({ sortBy: internalSortByParam });
  }

  function handlePaginationChange(pagination: Updater<PaginationState>) {
    console.log("Sort change handler triggered");
    setFilters(
      typeof pagination === "function"
        ? pagination(paginationState)
        : pagination
    );
  }

  return (
    <div className="flex flex-col gap-2 p-2">
      {/* <div> */}
      <h1 className="text-2xl font-semibold mb-1">Transactions</h1>
      <Outlet />

      {matchesTransactionsRoute && (
        <Link
          to="/transactions/create"
          search={(prev) => prev}
          className="inline-block px-6 py-3 text-white font-semibold text-sm rounded-lg shadow-md transition-all duration-300 hover:scale-105 hover:shadow-lg focus:outline-none"
        >
          Add Transaction
        </Link>
      )}

      {/* Transactions Table */}
      <TransactionsDataTable
        data={transactions ?? []}
        columns={transactionsColumns}
        pagination={paginationState}
        paginationOptions={{
          onPaginationChange: handlePaginationChange,
          rowCount: totalElements,
        }}
        filters={filters}
        onFilterChange={(filters) => setFilters(filters)}
        sorting={sortingState}
        onSortingChange={handleSortingChange}
      />
      <div>
        {totalElements} transactions found
        <button
          className="border rounded p-1 disabled:text-gray-500 disabled:cursor-not-allowed"
          onClick={resetFilters}
          disabled={Object.keys(filters).length === 1}
        >
          Reset Filters
        </button>
      </div>
      {/* <pre>{JSON.stringify(filters, null, 2)}</pre> */}
    </div>
  );
}
