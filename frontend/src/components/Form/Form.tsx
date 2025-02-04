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
} from "../../types/schemas/transactionSchema";

type FormPropsType = {
  onSubmit: (data: FormSchemaType) => Promise<void>;
};

function Form({ onSubmit }: Readonly<FormPropsType>) {
  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<FormSchemaType>({
    resolver: zodResolver(FormSchema),
  });

  const onSubmitForm = (data: FormSchemaType) => {
    const result = FormSchema.safeParse(data);
    if (!result.success) {
      console.log("Zod Error: " + result.error);
    } else {
      console.log(
        "No Zod error, confirming for logging purposes" + result.error
      );
    }
    console.log("Submitting Form with data");
    onSubmit(data);
  };

  const setDateToToday = () => {
    const today = new Date().toISOString().split("T")[0];
    setValue("date", today);
  };

  return (
    <form onSubmit={handleSubmit(onSubmitForm)}>
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
  register: UseFormRegister<FieldValues>;
  errors: FieldErrors<FieldValues>;
};

function InputField({ name, register, errors }: Readonly<InputFieldPropsType>) {
  return (
    <div>
      <label>
        {capitalizeFirstLetter(name)}
        <input {...register(name)} />
      </label>
      <InputFieldErrorMessage name={name} errors={errors} />
    </div>
  );
}

function capitalizeFirstLetter(word: string) {
  return word.charAt(0).toUpperCase() + word.slice(1);
}

function InputFieldErrorMessage({ name, errors }: FieldValues) {
  return errors[name]?.message ?? null;
}

export { Form };
