import { CircleEllipsis } from "lucide-react";
import { Button } from "../ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "../ui/dropdown-menu";
import { Link } from "@tanstack/react-router";

export function ActionsMenu({
  transactionId,
}: {
  transactionId: number | string;
}) {
  const transactionIdParam = String(transactionId);
  const handleDelete = () => {
    if (
      window.confirm(
        `Are you sure you want to delete transaction ${transactionId}?`
      )
    ) {
      console.log(`Deleting transaction with id: ${transactionId}`);
    }
  };
  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button variant={"outline"}>
          <CircleEllipsis />
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent>
        <DropdownMenuGroup>
          <DropdownMenuItem asChild>
            <Link
              className="text-primary"
              resetScroll={false}
              to={`/transactions/edit/$transactionId`}
              params={{ transactionId: transactionIdParam }}
              search={(prev) => prev}
            >
              Edit
            </Link>
          </DropdownMenuItem>
          <DropdownMenuItem variant="destructive" onClick={handleDelete}>
            Delete
          </DropdownMenuItem>
        </DropdownMenuGroup>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
