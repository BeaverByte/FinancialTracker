import { z } from "zod";

export const validationSchema = z
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
      .min(6, { message: "Password must be at least 6 characters" }),
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

export type ValidationSchema = z.infer<typeof validationSchema>;
