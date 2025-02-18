import { Transaction } from "../../types/Transaction";
import { TransactionForm } from "./TransactionForm";

type EditTransactionModal = {
  transaction: Transaction | undefined;
  onClose: () => void;
  onSave: (updatedTransaction: Transaction) => void;
};

export function EditTransactionModal({
  transaction,
  onSave,
  onClose,
}: Readonly<EditTransactionModal>) {
  return (
    <TransactionForm
      initialValues={transaction}
      onSubmit={onSave}
      onCancel={onClose}
    />
  );
}
