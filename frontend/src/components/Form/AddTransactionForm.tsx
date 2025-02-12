import {
  FormSchema,
  TransactionFormSchema,
} from "../../types/schemas/transactionSchema";
import { useNavigate } from "react-router";
import { TransactionForm } from "./TransactionForm";
import { useAddTransaction } from "../../hooks/useAddTransaction";
import { APP_ROUTES } from "../../pages/routes";

function AddTransactionForm() {
  const navigate = useNavigate();
  const transactions = useAddTransaction();

  const handleSave = (data: TransactionFormSchema) => {
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

  const initialTransaction: TransactionFormSchema = {
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
      onCancel={() => navigate(APP_ROUTES.BACK)}
    />
  );
}

export { AddTransactionForm };
