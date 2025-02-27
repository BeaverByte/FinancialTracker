import { createFileRoute, Outlet, useNavigate } from "@tanstack/react-router";
import { APP_ROUTES } from "../pages/routes";
import Table from "../components/Table/Table";
import { transactionsQueryOptions } from "../transactionsQueryOptions";
import { useSuspenseQuery } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";

export const Route = createFileRoute("/_auth/transactions")({
  loader: ({ context: { queryClient } }) =>
    queryClient.ensureQueryData(transactionsQueryOptions),
  component: TransactionsComponent,
});

function TransactionsComponent() {
  // SuspenseQuery allows for throwOnError
  const transactionsQuery = useSuspenseQuery(transactionsQueryOptions);
  const transactions = transactionsQuery.data;

  const navigate = useNavigate();

  if (transactionsQuery.isLoading) return <p>Loading...</p>;
  if (transactionsQuery.error)
    return <p>Error getting transactions: {transactionsQuery.error.message}</p>;

  const handleEditTransaction = (id: number) => {
    // console.log("Opening transaction modal for editing transaction ");
    // navigate({ to: `/transactions/${id}` });
  };

  return (
    <div>
      <h1>Transactions</h1>
      <button onClick={() => navigate({ to: APP_ROUTES.CREATE_TRANSACTION })}>
        Add Transaction
      </button>
      <Outlet />
      <Table data={transactions} onEditTransaction={handleEditTransaction} />
      <ReactQueryDevtools initialIsOpen={false} />
    </div>
  );
}
