import { EditTransactionModal } from "./EditTransactionModal";
import { Transaction } from "../../types/Transaction";
import { useUpdateTransaction } from "../../hooks/useUpdateTransaction";
import { useGetTransactionById } from "../../hooks/useGetTransactionById";
import { useNavigate, useParams, useRouter } from "@tanstack/react-router";

export function EditTransaction() {
  const params = useParams({ from: "/transaction/$transactionId" });
  const navigate = useNavigate();
  const editMutation = useUpdateTransaction();
  const { history } = useRouter();

  const id = Number(params.transactionId);

  const { data: transaction, isLoading, error } = useGetTransactionById(id);

  if (!Number.isInteger(id)) {
    return <p>Error: "{params.transactionId}" is not a valid transaction ID</p>;
  }

  const handleSave = (updatedTransaction: Transaction) => {
    console.log("Saving transaction to id " + id);
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
      transaction={transaction}
      onClose={() => history.go(-1)}
      onSave={handleSave}
    />
  );
}
