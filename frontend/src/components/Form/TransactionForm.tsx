import { Controller, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  FormSchema,
  TransactionFormSchema,
} from "../../types/schemas/transactionSchema";
import { ZodObject, ZodRawShape } from "zod";
import { InputField, InputFieldErrorMessage } from "./Input";
import Button from "../Button/Button";
import { Transaction } from "../../types/Transaction";
import { Input } from "../ui/input";

export type TransactionFormProps = {
  initialValues: object | Transaction;
  onSubmit: (data: TransactionFormSchema) => void;
  onCancel: () => void;
};

function TransactionForm({
  initialValues,
  onSubmit,
  onCancel,
}: Readonly<TransactionFormProps>) {
  const {
    handleSubmit,
    control,
    setValue,
    formState: { errors },
  } = useForm<TransactionFormSchema>({
    resolver: zodResolver(FormSchema),
    defaultValues: initialValues || {},
  });

  const setDateToToday = () => {
    const today = new Date().toISOString().split("T")[0];
    setValue("date", today);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      {Object.keys((FormSchema as unknown as ZodObject<ZodRawShape>).shape)
        .filter((key) => key !== "id") // Exclude id field
        .map((key) => (
          <Controller
            key={key}
            name={key as keyof TransactionFormSchema}
            control={control}
            render={({ field }) => (
              <>
                <InputField
                  name={key}
                  value={field.value}
                  onChange={field.onChange}
                />
                <InputFieldErrorMessage name={key} errors={errors} />
              </>
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
  );
}

export { TransactionForm };
