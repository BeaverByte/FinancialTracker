import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  FormSchema,
  TransactionFormSchema,
} from "../../types/schemas/transactionSchema";
import { Input } from "../ui/input";
import { Button } from "../ui/button";
import { capitalizeFirstLetter } from "../../utils/stringUtils";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "../ui/form";

export type TransactionFormProps = {
  onSubmit: (data: TransactionFormSchema) => void;
  onCancel: () => void;
};

function TransactionForm({
  onSubmit,
  onCancel,
}: Readonly<TransactionFormProps>) {
  const form = useForm<TransactionFormSchema>({
    resolver: zodResolver(FormSchema),
    defaultValues: {
      date: "",
      merchant: "",
      account: "",
      category: "",
      amount: "",
      note: "",
    },
  });

  const transactionFormKeys: (keyof TransactionFormSchema)[] = [
    "account",
    "amount",
    "category",
    "date",
    "merchant",
    "note",
  ];

  const setDateToToday = () => {
    const today = new Date().toISOString().split("T")[0];
    form.setValue("date", today);
  };

  const inputTypes: Record<string, string> = {
    amount: "number",
    date: "date",
  };

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)}>
        {transactionFormKeys.map((key) => (
          <FormField
            key={key}
            control={form.control}
            name={key}
            render={({ field }) => (
              <FormItem>
                <FormLabel>{capitalizeFirstLetter(field.name)}</FormLabel>
                <FormControl>
                  <Input
                    type={inputTypes[key] ?? "text"}
                    placeholder={field.name}
                    // value={field.value ?? ""}
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
        ))}
        <Button type={"button"} onClick={setDateToToday}>
          Set to Today
        </Button>
        <Button type={"button"} onClick={onCancel}>
          Cancel
        </Button>
        <Button type={"submit"}>Save</Button>
      </form>
    </Form>
  );
}

export { TransactionForm };
