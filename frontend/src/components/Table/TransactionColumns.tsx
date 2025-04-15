import { ColumnDef, createColumnHelper, RowData } from "@tanstack/react-table";
import { Transaction } from "../../types/Transaction";
import { DollarSign } from "lucide-react";
import { ActionsMenu } from "./ActionsMenu";

declare module "@tanstack/react-table" {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  interface ColumnMeta<TData extends RowData, TValue> {
    filterKey?: keyof TData;
    filterVariant?: "text" | "number";
    className?: React.HTMLAttributes<HTMLElement>["className"];
  }
}

// columnHelper returns utility for creating different column definition types for safety
const columnHelper = createColumnHelper<Transaction>();

export const getTransactionColumns: ColumnDef<Transaction>[] = [
  // Display Column
  columnHelper.display({
    id: "actions",
    header: () => <span>Actions</span>,
    cell: (cell) => {
      return <ActionsMenu transactionId={cell.row.original.id} />;
    },
  }),
  {
    accessorKey: "id",
    header: () => <span>ID</span>,
    meta: { filterKey: "id", filterVariant: "number" },
  },
  {
    accessorKey: "date",
    header: () => <span>Date</span>,
    meta: { filterKey: "date" },
  },
  {
    accessorKey: "merchant",
    header: () => <span>Merchant</span>,
    meta: { filterKey: "merchant" },
  },
  {
    accessorKey: "account",
    header: () => "Account",
    meta: { filterKey: "account" },
  },
  {
    accessorKey: "category",
    header: () => "Category",
    meta: { filterKey: "category" },
  },
  {
    accessorKey: "amount",
    header: () => (
      <>
        <DollarSign />
        Amount
      </>
    ),
    meta: { filterKey: "amount" },
    cell: ({ row }) => {
      const amount = parseFloat(row.getValue("amount"));
      const formatted = new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "USD",
      }).format(amount);

      return <div className="text-right">{formatted}</div>;
    },
  },
  {
    accessorKey: "note",
    header: () => "Note",
    meta: { filterKey: "note" },
  },
];
