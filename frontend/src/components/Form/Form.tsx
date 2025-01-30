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
    }
    onSubmit(data);
  };

  const setDateToToday = () => {
    const today = new Date().toISOString().split("T")[0];
    setValue("date", today);
  };

  return (
    <form onSubmit={handleSubmit(onSubmitForm)}>
      <div>
        <label>
          <span>Date</span>
          {/* <input {...register("date")} type="date" /> */}
          <input {...register("date")} />
        </label>
        <button type="button" onClick={setDateToToday}>
          Set to Today
        </button>
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
