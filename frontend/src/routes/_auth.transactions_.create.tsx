import {
  createFileRoute,
  useNavigate,
  useRouter,
} from "@tanstack/react-router";
import { useAddTransaction } from "../hooks/useAddTransaction";
import {
  FormSchema,
  TransactionFormSchema,
} from "../types/schemas/transactionSchema";
import { TransactionForm } from "../components/Form/TransactionForm";

export const Route = createFileRoute("/_auth/transactions_/create")({
  component: AddTransactionForm,
});

function AddTransactionForm() {
  const navigate = useNavigate();
  const transactions = useAddTransaction();
  const { history } = useRouter();

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
    navigate({ to: "/transactions" });
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
      onCancel={() => history.go(-1)}
    />
  );
}
