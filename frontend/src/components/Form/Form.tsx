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
    setValue("date", today);
  };

  return (
    /* handleSubmit will validate inputs before onSubmit */
    <form onSubmit={handleSubmit(onSubmit)}>
      <div>
        <label>
          <span>Date</span>
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
          <span>Merchant</span>
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
          <span>Account</span>
          <input type="text" />
        </label>
      </div>
      <div>
        <label>
          <span>Category</span>
          <input type="text" />
        </label>
      </div>
      <div>
        <label>
          <span>Amount</span>
          <input type="number" />
        </label>
      </div>
      <div>
        <label>
          <span>Note</span>
          <input type="text" />
        </label>
      </div>
      <button type="submit">Add Transaction</button>
    </form>
  );
}

export { Form };
