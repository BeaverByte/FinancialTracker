import {
  useForm,
  UseFormRegister,
  FieldErrors,
  FieldValues,
} from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  FormSchema,
  FormSchemaType,
  TransactionFormData,
} from "../../types/schemas/transactionSchema";
import { UseAddTransaction } from "../../services/transactions";
import { useNavigate } from "react-router";
import { capitalizeFirstLetter } from "../../utility/stringUtils";

function AddTransactionForm() {
  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<FormSchemaType>({
    resolver: zodResolver(FormSchema),
  });
  const navigate = useNavigate();

  const transactions = UseAddTransaction();

  const onSubmit = (data: FormSchemaType) => {
    const postTransaction = data;

    const result = FormSchema.safeParse(postTransaction);

    if (!result.success) {
      console.log("Zod Error: " + result.error);
    }
    console.log(
      "No Zod Error, submitting FormData transaction: " +
        JSON.stringify(postTransaction)
    );

    transactions.mutate(postTransaction);
    navigate("/transactions");
  };

  const setDateToToday = () => {
    const today = new Date().toISOString().split("T")[0];
    setValue("date", today);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <InputField name={"date"} register={register} errors={errors} />
      <button type="button" onClick={setDateToToday}>
        Set to Today
      </button>
      <InputField name={"merchant"} register={register} errors={errors} />
      <InputField name={"account"} register={register} errors={errors} />
      <InputField name={"category"} register={register} errors={errors} />
      <InputField name={"amount"} register={register} errors={errors} />
      <InputField name={"note"} register={register} errors={errors} />
      <button type="submit">Add Transaction</button>
    </form>
  );
}

type InputFieldPropsType = {
  name: string;
  register: UseFormRegister<TransactionFormData>;
  errors: FieldErrors<FieldValues>;
};

function InputField({ name, register, errors }: Readonly<InputFieldPropsType>) {
  return (
    <div>
      <label>
        {capitalizeFirstLetter(name)}:
        <input {...register(name)} />
      </label>
      <InputFieldErrorMessage name={name} errors={errors} />
    </div>
  );
}

function InputFieldErrorMessage({ name, errors }: FieldValues) {
  return errors[name]?.message ?? null;
}

export { AddTransactionForm };
