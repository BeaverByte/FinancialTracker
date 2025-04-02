import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { useAddTransaction } from "../hooks/useAddTransaction";
import {
  TransactionFormValidationSchema,
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
import { TransactionCard } from "../components/ui/transaction-card";

export const Route = createFileRoute("/_auth/transactions/create")({
  component: AddTransactionForm,
});

function AddTransactionForm() {
  const navigate = useNavigate();
  const transactions = useAddTransaction();

  const handleSave = (transaction: TransactionFormSchema) => {
    const result = TransactionFormValidationSchema.safeParse(transaction);

    console.log(
      "Submitting FormData transaction: " + JSON.stringify(transaction)
    );

    if (!result.success) {
      console.log("Zod Error: " + result.error);
    }

    transactions.mutate(transaction);
  };

  return (
    <TransactionCard
      title="Add Transaction"
      description="Enter in transaction below"
      onSubmit={handleSave}
      onCancel={() => navigate({ to: `/transactions`, search: (prev) => prev })}
    />
  );
}
