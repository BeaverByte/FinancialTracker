import { ColumnDef, RowData } from "@tanstack/react-table";
import { Transaction } from "../../types/Transaction";

declare module "@tanstack/react-table" {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  interface ColumnMeta<TData extends RowData, TValue> {
    filterKey?: keyof TData;
    filterVariant?: "text" | "number";
  }
}

export const TRANSACTION_COLUMNS: ColumnDef<Transaction>[] = [
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
    header: () => "Amount",
    meta: { filterKey: "amount" },
  },
  {
    accessorKey: "note",
    header: () => "Note",
    meta: { filterKey: "note" },
  },
];
