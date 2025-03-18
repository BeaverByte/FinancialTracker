import { Transaction } from "../../types/Transaction";
import { TransactionForm } from "./TransactionForm";

type EditTransactionModal = {
  transaction: Transaction | undefined;
  onCancel: () => void;
  onSave: (updatedTransaction: Transaction) => void;
};

export function EditTransactionModal({
  transaction,
  onSave,
  onCancel,
}: Readonly<EditTransactionModal>) {
  return (
    <TransactionForm
      initialValues={transaction}
      onSubmit={onSave}
      onCancel={onCancel}
    />
  );
}
