import { useNavigate, useParams } from "react-router";
import { EditTransactionModal } from "./EditTransactionModal";
import { useQuery } from "@tanstack/react-query";
import {
  getTransactionById,
  UseUpdateTransaction,
} from "../../services/transactions";
import { Transaction } from "../../types/schemas/transactionSchema";

export function EditTransaction() {
  const { id } = useParams();
  const navigate = useNavigate();
  const editMutation = UseUpdateTransaction();

  const {
    data: transaction,
    isLoading,
    error,
  } = useQuery({
    queryKey: ["transaction", id],
    queryFn: () => getTransactionById(id),
    enabled: !!id, // Only fetch if ID exists
  });

  const handleSave = (updatedTransaction: Transaction) => {
    console.log("Saving transaction to id " + id);
    editMutation.mutate({
      id,
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
