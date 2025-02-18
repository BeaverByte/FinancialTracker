import { FieldValues } from "react-hook-form";
import { capitalizeFirstLetter } from "../../utility/stringUtils";
import { ChangeEventHandler } from "react";

type InputFieldPropsType = {
  name: string;
  value: string | number;
  onChange: ChangeEventHandler<HTMLInputElement>;
};

export function InputField({
  name,
  value,
  onChange,
}: Readonly<InputFieldPropsType>) {
  return (
    <label>
      {capitalizeFirstLetter(name)}:
      <input name={name} value={value} onChange={onChange} />
    </label>
  );
}

export function InputFieldErrorMessage({ name, errors }: FieldValues) {
  return errors[name]?.message ?? null;
}

export function Input({ children }) {
  return <div>{children}</div>;
}
