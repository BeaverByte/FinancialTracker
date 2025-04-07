import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  LoginFormSchema,
  LoginFormValidationSchema,
} from "../types/schemas/loginSchema";
import { Input } from "./ui/input";
import { Button } from "./ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "./ui/form";
import { capitalizeFirstLetter } from "../utils/stringUtils";

type LoginFormProps = {
  onSubmit: (data: LoginFormSchema) => void;
  isLoggingIn: boolean;
  onCancel: () => void;
  error?: string;
};

function LoginForm({
  onSubmit,
  isLoggingIn,
  onCancel,
  error,
}: Readonly<LoginFormProps>) {
  const form = useForm<LoginFormSchema>({
    resolver: zodResolver(LoginFormValidationSchema),
    defaultValues: {
      username: "",
      password: "",
    },
  });

  const loginFormKeys: (keyof LoginFormSchema)[] = ["username", "password"];

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)}>
        {loginFormKeys.map((key) => (
          <FormField
            control={form.control}
            key={key}
            name={key}
            render={({ field }) => (
              <FormItem>
                <FormLabel>{capitalizeFirstLetter(field.name)}</FormLabel>
                <FormControl>
                  <Input {...field} type="text" value={field.value ?? ""} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
        ))}

        {error && <p className="text-red-500">{error}</p>}
        {isLoggingIn ? <p>Loading...</p> : ""}

        <div className="mt-6 flex items-center gap-3 justify-between">
          <Button type={"button"} variant={"outline"} onClick={onCancel}>
            Cancel
          </Button>
          <Button type={"submit"}>Login</Button>
        </div>
      </form>
    </Form>
  );
}

export { LoginForm };
