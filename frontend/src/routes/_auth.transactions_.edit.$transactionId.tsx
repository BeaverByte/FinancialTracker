import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { transactionQueryOptions } from "../transactionQueryOptions";
import { useSuspenseQuery } from "@tanstack/react-query";
import { useUpdateTransaction } from "../hooks/useUpdateTransaction";
import { Transaction } from "../types/Transaction";
import { TransactionFormSchema } from "../types/schemas/transactionSchema";
import { TransactionCard } from "../components/ui/transaction-card";

export const Route = createFileRoute(
  "/_auth/transactions_/edit/$transactionId"
)({
  loader: ({ context: { queryClient }, params: { transactionId } }) => {
    return queryClient.ensureQueryData(transactionQueryOptions(transactionId));
  },
  component: EditTransactionForm,
});

function EditTransactionForm() {
  const transactionId = Route.useParams().transactionId;

  const {
    data: transaction,
    isLoading,
    error,
  } = useSuspenseQuery(transactionQueryOptions(transactionId));

  const navigate = useNavigate();

  const editTransactionMutation = useUpdateTransaction();

  const id = Number(transactionId);

  if (!Number.isInteger(id)) {
    return <p>Error: "{transactionId}" is not a valid transaction ID</p>;
  }

  console.log(
    `Edit transaction form with transaction of ${JSON.stringify(transaction)}`
  );

  const handleEdit = (data: TransactionFormSchema) => {
    const updatedTransaction: Transaction = {
      id,
      ...data,
    };
    console.log(
      `Saving transaction, ${JSON.stringify(updatedTransaction)}, to id, ${id}`
    );

    editTransactionMutation.mutate(
      {
        id,
        transaction: updatedTransaction,
      },
      {
        onSettled: () =>
          navigate({ to: "/transactions", search: (prev) => prev }),
      }
    );
  };

  if (isLoading) return <p>Loading transaction...</p>;
  if (error) return <p>Error: {error.message}</p>;

  return (
    <TransactionCard
      title="Edit Transaction"
      description="Makes changes to transaction below"
      onSubmit={handleEdit}
      formValues={transaction}
      onCancel={() => navigate({ to: `/transactions`, search: (prev) => prev })}
    />
  );
}
