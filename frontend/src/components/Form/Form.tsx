import { useForm, Controller, SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Input } from "../Input/Input";

// Schema to validate form inputs against
const validationSchema = z
  .object({
    customInput: z.string().min(1, { message: "Custom input is required" }),
    merchant: z.string().min(1, { message: "Merchant is required" }),
    date: z.string().date().min(1, { message: "Date is required" }),
    firstName: z.string().min(1, { message: "Firstname is required" }),
    lastName: z.string().min(1, { message: "Lastname is required" }),
    email: z.string().min(1, { message: "Email is required" }).email({
      message: "Must be a valid email",
    }),
    password: z
      .string()
      .min(6, { message: "Password must be atleast 6 characters" }),
    confirmPassword: z
      .string()
      .min(1, { message: "Confirm Password is required" }),
    terms: z.literal(true, {
      errorMap: () => ({ message: "You must accept Terms and Conditions" }),
    }),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Passwords don't match",
    path: ["confirmPassword"],
  });

function Form({ onSubmit }) {
  const {
    register,
    handleSubmit,
    watch,
    control,
    setValue,
    formState: { errors },
  } = useForm<ValidationSchema>({
    resolver: zodResolver(validationSchema),
  });

  // Changes to an input's data should update form state
  const handleChange = (field, value) => {
    setFormData((previousData) => ({
      ...previousData,
      [field]: value,
    }));
  };
  // Update form's date property with today's value
  const setDateToToday = () => {
    const today = new Date().toISOString().split("T")[0];
    console.log("Updating date to " + today);
    setValue("date", today);
  };

  return (
    /* handleSubmit will validate inputs before onSubmit */
    <form onSubmit={handleSubmit(onSubmit)}>
      <div>
        <Controller
          control={control}
          name="customInput"
          render={({
            field: { onChange },
            fieldState: { invalid, isTouched, isDirty, error },
            formState,
          }) => (
            <Input
              id="custom-input"
              label="Custom input"
              placeholder="Custom input"
              required={true}
              onChange={onChange}
              state={
                errors.customInput && {
                  type: "error",
                  message: errors.customInput?.message,
                }
              }
            />
          )}
        />

        <label>
          Date
          <input {...register("date")} type="date" />
        </label>
        {
          <button type="button" onClick={setDateToToday}>
            Set to Today
          </button>
        }
        {errors.date && <p>{errors.date?.message}</p>}
      </div>
      <div>
        <label>
          Merchant
          <input
            {...register("merchant", {
              required: "Merchant is required",
            })}
            type="text"
          />
        </label>
        {errors.merchant && <p>{errors.merchant?.message}</p>}
      </div>
      <div>
        <label>
          Account
          <input type="text" />
        </label>
      </div>
      <div>
        <label>
          Category
          <input type="text" />
        </label>
      </div>
      <div>
        <label>
          Amount
          <input type="number" />
        </label>
      </div>
      <div>
        <label>
          Note
          <input type="text" />
        </label>
      </div>
      {/* <ErrorMessage
        errors={errors}
        name="date"
        render={({ messages }) =>
          messages &&
          Object.entries(messages).map(([type, message]) => (
            <p key={type}>{message}</p>
          ))
        }
      /> */}
      {/* {fields.map((field) => (
        <div key={field.name}>
          <label>
            {field.placeholder}
            <input
              {...register(field.placeholder, {
                required: field.validationError,
                maxLength: {
                  value: 1,
                  message: "too big",
                },
              })}
              type={field.type}
            />
          </label>
          {field.name === "date" && (
            <button type="button" onClick={setDateToToday}>
              Set to Today
            </button>
          )}
          <ErrorMessage
            errors={errors}
            name={field.placeholder}
            render={({ messages }) =>
              messages &&
              Object.entries(messages).map(([type, message]) => (
                <p key={type}>{message}</p>
              ))
            }
          />
        </div>
      ))} */}
      <button type="submit">Add Transaction</button>
    </form>
  );
}

export { Form };
