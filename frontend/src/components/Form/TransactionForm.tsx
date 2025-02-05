import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  FormSchema,
  FormSchemaType,
} from "../../types/schemas/transactionSchema";
import { InputField } from "./InputField";
import { ZodObject, ZodRawShape } from "zod";

function TransactionForm({ initialValues, onSubmit, onCancel }) {
  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<FormSchemaType>({
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
          <InputField
            key={key}
            name={key}
            register={register}
            errors={errors}
          />
        ))}

      <button type="button" onClick={setDateToToday}>
        Set to Today
      </button>
      <button type="button" onClick={onCancel}>
        Cancel
      </button>
      <button type="submit">Save</button>
    </form>
  );
}

export { TransactionForm };
