import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { transactionQueryOptions } from "../transactionQueryOptions";
import { useSuspenseQuery } from "@tanstack/react-query";
import { useUpdateTransaction } from "../hooks/useUpdateTransaction";
import { Transaction } from "../types/Transaction";
import { TransactionForm } from "../components/Form/TransactionForm";
import { TransactionFormSchema } from "../types/schemas/transactionSchema";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "../components/ui/card";
import { Button } from "../components/ui/button";

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

    editTransactionMutation.mutate({
      id,
      transaction: updatedTransaction,
    });
  };

  if (isLoading) return <p>Loading transaction...</p>;
  if (error) return <p>Error: {error.message}</p>;

  return (
    <Card>
      <CardHeader>
        <CardTitle>Edit Transaction</CardTitle>
        <CardDescription>Makes changes to transaction below</CardDescription>
      </CardHeader>
      <CardContent>
        <TransactionForm
          onSubmit={handleEdit}
          formValues={transaction}
          onCancel={() =>
            navigate({ to: `/transactions`, search: (prev) => prev })
          }
        />
      </CardContent>
      <CardFooter />
    </Card>
  );
}
