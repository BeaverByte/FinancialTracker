import { JSX } from "react";
import { TableHeaderConfig, TableProps } from "../../types/Transaction";
import TableBody from "./TableBody";
import TableHeader from "./TableHeader";
import { useDeleteTransaction } from "../../hooks/useDeleteTransaction";

const defaultHeaders: TableHeaderConfig[] = [
  { label: "Actions", field: "actions", visible: true },
  { label: "Date", field: "date", visible: true },
  { label: "Merchant", field: "merchant", visible: true },
  { label: "Account", field: "account", visible: true },
  { label: "Category", field: "category", visible: true },
  { label: "Amount", field: "amount", visible: true },
  { label: "Note", field: "note", visible: true },
];

export default function Table({
  data,
  headers = defaultHeaders,
  onEditTransaction,
}: Readonly<TableProps>): JSX.Element {
  const deleteMutation = useDeleteTransaction();

  function handleDeleteClick(id: number) {
    console.log("DELETE transaction with id " + id);
    deleteMutation.mutate(id);
  }

  return (
    <div style={{ overflowX: "auto", maxHeight: "500px", overflowY: "auto" }}>
      {/* TODO will need to replace overflow with actual pagination */}
      <table>
        <TableHeader headers={headers} />
        <TableBody
          data={data}
          headers={headers}
          onEditTransaction={onEditTransaction}
          onDeleteTransaction={handleDeleteClick}
        />
      </table>
    </div>
  );
}
