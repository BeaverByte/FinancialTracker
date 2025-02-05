import {
  FormSchema,
  FormSchemaType,
} from "../../types/schemas/transactionSchema";
import { UseAddTransaction } from "../../services/transactions";
import { useNavigate } from "react-router";
import { TransactionForm } from "./TransactionForm";

function AddTransactionForm() {
  const navigate = useNavigate();
  const transactions = UseAddTransaction();

  const handleSave = (data: FormSchemaType) => {
    const postTransaction = data;

    const result = FormSchema.safeParse(postTransaction);

    console.log(
      "Submitting FormData transaction: " + JSON.stringify(postTransaction)
    );

    if (!result.success) {
      console.log("Zod Error: " + result.error);
    }

    transactions.mutate(postTransaction);
    navigate("/transactions");
  };

  const initialTransaction = {
    date: "",
    merchant: "",
    account: "",
    category: "",
    amount: "",
    note: "",
  };

  return (
    <TransactionForm
      initialValues={{ initialTransaction }}
      onSubmit={handleSave}
      onCancel={() => navigate("/transactions")}
    />
  );
}

export { AddTransactionForm };
