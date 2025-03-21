import { ColumnDef, createColumnHelper, RowData } from "@tanstack/react-table";
import { Transaction } from "../../types/Transaction";
import { Link } from "@tanstack/react-router";

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
      const transactionId = cell.row.original.id;
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
        <>
          <Link
            to={`/transactions/edit/$transactionId`}
            params={{ transactionId: transactionIdParam }}
            search={(prev) => prev}
          >
            Edit
          </Link>
          <button
            onClick={handleDelete}
            className="text-red-600 hover:text-red-800 focus:outline-none"
          >
            Delete
          </button>
        </>
      );
    },
    // enableSorting: false,
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
    header: () => "Amount",
    meta: { filterKey: "amount" },
  },
  {
    accessorKey: "note",
    header: () => "Note",
    meta: { filterKey: "note" },
  },
];
