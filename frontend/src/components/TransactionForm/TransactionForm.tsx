// TransactionForm.tsx

import { Form } from "../Form/AddTransactionForm";

function TransactionForm({ onSubmit, setFormData, formData }) {
  return (
    <div>
      <Form onSubmit={onSubmit} setFormData={setFormData} formData={formData} />
    </div>
  );
}

export default TransactionForm;
