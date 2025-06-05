import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  TransactionFormValidationSchema,
  TransactionFormSchema,
} from "../types/schemas/transactionSchema";
import { Input } from "./ui/input";
import { Button } from "./ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "./ui/form";
import { CustomCurrencyInput } from "./ui/currency-input";
import { capitalizeFirstLetter } from "../utils/stringUtils";

type TransactionFormProps = {
  onSubmit: (data: TransactionFormSchema) => void;
  onCancel: () => void;
  formValues?: Partial<TransactionFormSchema>;
};

function TransactionForm({
  onSubmit,
  onCancel,
  formValues,
}: Readonly<TransactionFormProps>) {
  const form = useForm<TransactionFormSchema>({
    resolver: zodResolver(TransactionFormValidationSchema),
    defaultValues: {
      date: "",
      merchant: "",
      account: "",
      category: "",
      amount: "",
      note: "",
      ...formValues,
    },
  });

  const transactionFormKeys: (keyof TransactionFormSchema)[] = [
    "merchant",
    "account",
    "category",
    "amount",
    "date",
    "note",
  ];

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
              <FormItem className="mb-4">
                <FormLabel>{capitalizeFirstLetter(field.name)}</FormLabel>
                <FormControl>
                  {/* // Setup Input as Currency if key equals amount */}
                  {key === "amount" ? (
                    <CustomCurrencyInput
                      id="amount-input"
                      name={key}
                      value={field.value}
                      onValueChange={(value) => {
                        field.onChange(value);
                      }}
                      decimalsLimit={2}
                      prefix="$"
                      step={1}
                    />
                  ) : (
                    // Other Inputs are text
                    <Input type={inputTypes[key] ?? "text"} {...field} />
                  )}
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
        ))}
        <div className="mt-6 flex items-center gap-3 justify-between">
          <div className="flex items-center gap-3">
            <Button type={"button"} variant={"outline"} onClick={onCancel}>
              Cancel
            </Button>
          </div>
          <Button type={"submit"}>Save</Button>
        </div>
      </form>
    </Form>
  );
}

export { TransactionForm };
