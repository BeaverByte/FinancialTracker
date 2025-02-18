import DropdownMenu from "../DropdownMenu/DropdownMenu";
import TableColumn from "./TableColumn";

type ActionsColumnProps = {
  transactionId: number;
  onEditTransaction: (id: number) => void;
  onDeleteTransaction: (id: number) => void;
};

function ActionsColumn({
  transactionId,
  onEditTransaction,
  onDeleteTransaction,
}: Readonly<ActionsColumnProps>) {
  return (
    <TableColumn>
      <button onClick={() => onEditTransaction(transactionId)}>Edit</button>
      <DropdownMenu>
        <button onClick={() => onDeleteTransaction(transactionId)}>
          Delete
        </button>
      </DropdownMenu>
    </TableColumn>
  );
}

export default ActionsColumn;
