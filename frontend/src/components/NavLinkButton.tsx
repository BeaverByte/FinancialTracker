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
          className: "cursor-none focus:ring-primary underline",
          onClick: (e) => e.preventDefault(),
        }}
        inactiveProps={{
          className: "focus:ring-primary cursor-default",
        }}
      ></Link>
    </Button>
  );
}
