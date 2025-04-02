import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
  CardFooter,
} from "./card";
import { LoginFormSchema } from "../../types/schemas/loginSchema";
import { LoginForm } from "../Form/LoginForm";

type LoginCardProps = {
  onSubmit: (data: LoginFormSchema) => void;
  isLoggingIn: boolean;
  onCancel: () => void;
  error?: string;
  showRedirectMessage?: boolean;
};

export function LoginCard({
  onSubmit,
  isLoggingIn,
  onCancel,
  error,
  showRedirectMessage,
}: Readonly<LoginCardProps>) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Login</CardTitle>
        <CardDescription>Enter your credentials below</CardDescription>
      </CardHeader>
      <CardContent>
        {showRedirectMessage && (
          <p className="text-red-500">
            You need to log in to access this page.
          </p>
        )}
        <LoginForm
          onSubmit={onSubmit}
          onCancel={onCancel}
          isLoggingIn={isLoggingIn}
          error={error}
        />
      </CardContent>
      <CardFooter />
    </Card>
  );
}
