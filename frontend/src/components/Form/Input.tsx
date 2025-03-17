import { FieldValues } from "react-hook-form";
import { capitalizeFirstLetter } from "../../utils/stringUtils";
import { ChangeEventHandler } from "react";
import { Label } from "../ui/label";
import { Input } from "../ui/input";

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
    <>
      <Label htmlFor={name}>{capitalizeFirstLetter(name)}</Label>
      <Input id={name} placeholder="Test" value={value} onChange={onChange} />
      {/* <input name={name} value={value} onChange={onChange} /> */}
    </>
  );
}

export function InputFieldErrorMessage({ name, errors }: FieldValues) {
  return errors[name]?.message ?? null;
}
