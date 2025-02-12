import { useNavigate, useParams } from "react-router";
import { EditTransactionModal } from "./EditTransactionModal";
import { Transaction } from "../../types/Transaction";
import { useUpdateTransaction } from "../../hooks/useUpdateTransaction";
import { useGetTransactionById } from "../../hooks/useGetTransactionById";
import { APP_ROUTES } from "../../pages/routes";

export function EditTransaction() {
  const { id } = useParams();
  const navigate = useNavigate();
  const editMutation = useUpdateTransaction();

  const transactionId = Number(id);

  const {
    data: transaction,
    isLoading,
    error,
  } = useGetTransactionById(transactionId);

  if (!Number.isInteger(transactionId)) {
    return <p>Error: "{id}" is not a valid transaction ID</p>;
  }

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
      onClose={() => navigate(APP_ROUTES.BACK)}
      onSave={handleSave}
    />
  );
}
