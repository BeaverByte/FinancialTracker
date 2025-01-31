import { z } from "zod";

export const formSchema = z.object({
  // date: z.string().date().min(1, { message: "Date is required" }),
  date: z
    .string()
    .date("Date not recognized")
    .nonempty({ message: "Date is required" }),
  merchant: z.string().nonempty({ message: "Merchant is required" }),
  account: z.string(),
  category: z.string(),
  amount: z
    .string()
    .transform((value) => parseFloat(value))
    .refine((value) => !isNaN(value), {
      message: "Amount must be a valid number",
    }),
  note: z.string(),
});

export type formSchemaType = z.infer<typeof formSchema>;
