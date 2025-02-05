import { useNavigate, useParams } from "react-router";
import { EditTransactionModal } from "./EditTransactionModal";
import {
  UseGetTransactionById,
  UseUpdateTransaction,
} from "../../services/transactions";
import { Transaction } from "../../types/schemas/transactionSchema";

export function EditTransaction() {
  const { id } = useParams();
  const navigate = useNavigate();
  const editMutation = UseUpdateTransaction();

  const transactionId = Number(id);

  if (!Number.isInteger(transactionId)) {
    return <p>Error: "{id}" is not a valid transaction ID</p>;
  }

  const {
    data: transaction,
    isLoading,
    error,
  } = UseGetTransactionById(transactionId);

  const handleSave = (updatedTransaction: Transaction) => {
    console.log("Saving transaction to id " + transactionId);
    editMutation.mutate({
      id: transactionId,
      updates: updatedTransaction,
    });
    navigate("/transactions");
  };

  if (isLoading) return <p>Loading transaction...</p>;
  if (error) return <p>Error: {error.message}</p>;

  return (
    <EditTransactionModal
      transaction={transaction}
      onClose={() => navigate("/transactions")}
      onSave={handleSave}
    />
  );
}
