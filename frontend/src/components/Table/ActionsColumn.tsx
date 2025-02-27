import { Link } from "@tanstack/react-router";
import DropdownMenu from "../DropdownMenu/DropdownMenu";
import TableColumn from "./TableColumn";
import { APP_ROUTES } from "../../pages/routes";

type ActionsColumnProps = {
  transactionId: string;
  onEditTransaction: (id: string) => void;
  onDeleteTransaction: (id: string) => void;
};

function ActionsColumn({
  transactionId,
  onEditTransaction,
  onDeleteTransaction,
}: Readonly<ActionsColumnProps>) {
  return (
    <TableColumn>
      <Link
        to={`${APP_ROUTES.EDIT_TRANSACTION}`}
        params={{ transactionId: transactionId.toString() }}
        // replace={true}
      >
        <button onClick={() => onEditTransaction(transactionId)}>Edit</button>
      </Link>
      <DropdownMenu>
        <button onClick={() => onDeleteTransaction(transactionId)}>
          Delete
        </button>
      </DropdownMenu>
    </TableColumn>
  );
}

export default ActionsColumn;
