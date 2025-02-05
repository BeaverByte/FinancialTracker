import { FieldErrors, FieldValues, UseFormRegister } from "react-hook-form";
import { TransactionFormData } from "../../types/schemas/transactionSchema";
import { capitalizeFirstLetter } from "../../utility/stringUtils";

type InputFieldPropsType = {
  name: string;
  register: UseFormRegister<TransactionFormData>;
  errors: FieldErrors<FieldValues>;
};

export function InputField({
  name,
  register,
  errors,
}: Readonly<InputFieldPropsType>) {
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
