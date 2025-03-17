import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { transactionQueryOptions } from "../transactionQueryOptions";
import { useSuspenseQuery } from "@tanstack/react-query";
import { useUpdateTransaction } from "../hooks/useUpdateTransaction";
import { EditTransactionModal } from "../components/Form/EditTransactionModal";
import { Transaction } from "../types/Transaction";

export const Route = createFileRoute("/_auth/transactions/edit/$transactionId")(
  {
    loader: ({ context: { queryClient }, params: { transactionId } }) => {
      return queryClient.ensureQueryData(
        transactionQueryOptions(transactionId)
      );
    },
    component: EditTransactionComponent,
  }
);

function EditTransactionComponent() {
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

  const handleSave = (updatedTransaction: Transaction) => {
    console.log(
      `Saving transaction, ${JSON.stringify(updatedTransaction)}, to id, ${id}`
    );

    editTransactionMutation.mutate({
      id,
      transaction: updatedTransaction,
    });

    navigate({ to: "/transactions", search: (prev) => prev });
  };

  if (isLoading) return <p>Loading transaction...</p>;
  if (error) return <p>Error: {error.message}</p>;

  return (
    <>
      <h2>Edit transaction</h2>
      <EditTransactionModal
        key={transaction.id}
        transaction={transaction}
        onCancel={() =>
          navigate({ to: `/transactions`, search: (prev) => prev })
        }
        onSave={handleSave}
      />
    </>
  );
}
