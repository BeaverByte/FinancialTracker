import { z, ZodType } from "zod";
import { Transaction } from "../Transaction";

export type TransactionFormData = Omit<Transaction, "id">;

export const FormSchema: ZodType<TransactionFormData> = z.object({
  date: z
    .string()
    .date("Date not recognized")
    .nonempty({ message: "Date is required" }),
  merchant: z.string().nonempty({ message: "Merchant is required" }),
  account: z.string(),
  category: z.string(),
  amount: z
    .union([z.string(), z.number()])
    .transform((value) => {
      if (typeof value === "string") {
        const cleanedValue = removeNonNumericExceptDecimals(value);
        return parseFloat(cleanedValue);
      }
      return value;
    })
    .refine((value) => !isNaN(value), {
      message: "Amount must be a valid number",
    }),
  note: z.string(),
});

export type FormSchemaType = z.infer<typeof FormSchema>;

function removeNonNumericExceptDecimals(word: string) {
  return word.replace(/[^0-9.-]+/g, "");
}
