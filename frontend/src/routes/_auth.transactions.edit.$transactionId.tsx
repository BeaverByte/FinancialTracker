import {
  createFileRoute,
  useNavigate,
  useRouter,
} from "@tanstack/react-router";
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

  const editMutation = useUpdateTransaction();
  const { history } = useRouter();

  const id = Number(transactionId);

  if (!Number.isInteger(id)) {
    return <p>Error: "{transactionId}" is not a valid transaction ID</p>;
  }

  const handleSave = (updatedTransaction: Transaction) => {
    console.log(
      "Saving transaction," +
        JSON.stringify(updatedTransaction) +
        ", to id " +
        id
    );
    editMutation.mutate({
      id,
      updates: updatedTransaction,
    });
    navigate({ to: "/transactions" });
  };

  if (isLoading) return <p>Loading transaction...</p>;
  if (error) return <p>Error: {error.message}</p>;

  return (
    <EditTransactionModal
      key={transaction.id}
      transaction={transaction}
      onClose={() => history.go(-1)}
      onSave={handleSave}
    />
  );
}
