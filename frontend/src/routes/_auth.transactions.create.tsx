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
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";

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
    <Card>
      <CardHeader>
        <CardTitle>Add Transaction</CardTitle>
        <CardDescription>Enter in transaction below</CardDescription>
      </CardHeader>
      <CardContent>
        <TransactionForm
          initialValues={{ initialTransaction }}
          onSubmit={handleSave}
          onCancel={() =>
            navigate({ to: `/transactions`, search: (prev) => prev })
          }
        />
      </CardContent>
      <CardFooter className="flex justify-between">
        <Button variant="outline">Cancel</Button>
        <Button>Deploy</Button>
      </CardFooter>
    </Card>
  );
}
