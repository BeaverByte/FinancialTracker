import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { useAddTransaction } from "../hooks/useAddTransaction";
import {
  FormSchema,
  TransactionFormSchema,
} from "../types/schemas/transactionSchema";
import { TransactionForm } from "../components/Form/TransactionForm";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "../components/ui/card";

export const Route = createFileRoute("/_auth/transactions/create")({
  component: AddTransactionForm,
});

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
  };

  return (
    <Card>
      <CardHeader>
        <CardTitle>Add Transaction</CardTitle>
        <CardDescription>Enter in transaction below</CardDescription>
      </CardHeader>
      <CardContent>
        <TransactionForm
          onSubmit={handleSave}
          onCancel={() =>
            navigate({ to: `/transactions`, search: (prev) => prev })
          }
        />
      </CardContent>
      <CardFooter />
    </Card>
  );
}
