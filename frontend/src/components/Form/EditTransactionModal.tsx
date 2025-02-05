import { TransactionForm } from "./TransactionForm";

export function EditTransactionModal({ transaction, onSave, onClose }) {
  return (
    <TransactionForm
      initialValues={transaction}
      onSubmit={onSave}
      onCancel={onClose}
    />
  );
}
