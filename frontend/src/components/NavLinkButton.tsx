import { Link, LinkProps } from "@tanstack/react-router";
import { Button } from "./ui/button";

type NavLinkButtonProps = LinkProps & {
  children: React.ReactNode;
};

export function NavLinkButton(props: NavLinkButtonProps) {
  return (
    <Button variant="link" asChild>
      <Link
        {...props}
        activeProps={{
          className: "focus:ring-primary underline cursor-default",
          onClick: (e) => e.preventDefault(),
        }}
      ></Link>
    </Button>
  );
}
