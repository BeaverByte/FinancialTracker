import { z, ZodType } from "zod";

export type LoginFormData = {
  username?: string;
  password?: string;
};

export const LoginFormValidationSchema: ZodType<LoginFormData> = z
  .object({
    username: z.string().min(1, "Username is required"),
    password: z.string().min(1, "Password is required"),
  })
  .strict();

export type LoginFormSchema = z.infer<typeof LoginFormValidationSchema>;
