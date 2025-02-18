import { Meta, StoryObj } from "@storybook/react";
import { useForm, SubmitHandler } from "react-hook-form";
import { InputField } from "./Input";

const meta: Meta<typeof InputField> = {
  component: InputField,
};

export default meta;
type Story = StoryObj<typeof InputField>;

const InputFieldInForm = () => {
  const {
    handleSubmit,
    formState: { errors },
  } = useForm();

  const onSubmit: SubmitHandler<any> = (data) => {
    console.log(data); // Handle form data on submit
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <InputField
        name="username"
        value=""
        onChange={() => {}}
        errors={errors}
      />
      <button type="submit">Submit</button>
    </form>
  );
};

export const Primary: Story = {
  render: () => <InputFieldInForm />,
};
