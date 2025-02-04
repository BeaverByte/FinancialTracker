import {
  UseDeleteTransaction,
  UseUpdateTransaction,
} from "../../services/transactions";
import { Transaction } from "../../types/schemas/transactionSchema";
import DropdownMenu from "../DropdownMenu/DropdownMenu";

const defaultHeaders = [
  "Date",
  "Merchant",
  "Account",
  "Category",
  "Amount",
  "Note",
];

type TableProps = {
  data: Transaction[];
  headers?: Headers;
};

type Headers = string[];

export default function Table({
  data,
  headers,
}: Readonly<TableProps>): JSX.Element {
  const editMutation = UseUpdateTransaction();
  const deleteMutation = UseDeleteTransaction();

  function handleEditClick(id: number) {
    // TODO Open Modal for editing transaction
    console.log("Editing transaction with id ");
  }
  function handleDeleteClick(id: number) {
    console.log("DELETE transaction with id " + id);
    deleteMutation.mutate(id);
  }

  return (
    <div style={{ overflowX: "auto", maxHeight: "500px", overflowY: "auto" }}>
      {/* TODO will need to replace overflow with actual pagination */}
      <table>
        <thead>
          <TableHeader headers={headers} />
        </thead>
        <tbody>
          {data?.map((transaction: Transaction) => (
            <tr key={transaction.id}>
              <td>
                <button onClick={() => handleEditClick(transaction.id)}>
                  Edit
                </button>
                <DropdownMenu>
                  <button onClick={() => handleDeleteClick(transaction.id)}>
                    Delete
                  </button>
                </DropdownMenu>
              </td>
              <td>{transaction.date}</td>
              <td>{transaction.merchant}</td>
              <td>{transaction.account}</td>
              <td>{transaction.category}</td>
              <td>{transaction.amount}</td>
              <td>
                {transaction.note}
                {transaction.id}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function TableHeader({ headers = defaultHeaders }) {
  return (
    <tr>
      <th>Actions</th>
      {headers.map((header, index) => (
        <th key={index}>{header}</th>
      ))}
    </tr>
  );
}
