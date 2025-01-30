import { useForm, Controller, SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  validationSchema,
  ValidationSchema,
} from "../../schemas/transactionSchema";

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
      <button type="submit">Add Transaction</button>
    </form>
  );
}

export { Form };
