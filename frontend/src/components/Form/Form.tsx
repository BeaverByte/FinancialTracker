import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  formSchema,
  formSchemaType,
} from "../../types/schemas/transactionSchema";

function Form({ onSubmit }) {
  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<formSchemaType>({
    resolver: zodResolver(formSchema),
  });

  const onSubmitForm = (data: formSchemaType) => {
    const result = formSchema.safeParse(data);
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

function InputField({ name, register, errors }) {
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

function InputFieldErrorMessage({ name, errors }) {
  return errors[name]?.message ?? null;
}

export { Form };
