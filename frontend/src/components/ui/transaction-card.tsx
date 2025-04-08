import {
  TransactionFormData,
  TransactionFormSchema,
} from "../../types/schemas/transactionSchema";
import { TransactionForm } from "../TransactionForm";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "./card";

type TransactionCardProps = {
  title: string;
  description: string;
  onSubmit: (data: TransactionFormSchema) => void;
  formValues?: Partial<TransactionFormData>;
  onCancel: () => void;
};

export function TransactionCard({
  title,
  description,
  onSubmit,
  formValues,
  onCancel,
}: Readonly<TransactionCardProps>) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>{title}</CardTitle>
        <CardDescription>{description}</CardDescription>
      </CardHeader>
      <CardContent>
        <TransactionForm
          onSubmit={onSubmit}
          formValues={formValues}
          onCancel={onCancel}
        />
      </CardContent>
      <CardFooter />
    </Card>
  );
}
