import { TableHeaderConfig, Transaction } from "../../types/Transaction";
import ActionsColumn from "./ActionsColumn";
import TableColumn from "./TableColumn";

type TableBodyProps = {
  data: Transaction[];
  headers: TableHeaderConfig[];
  onEditTransaction: (id: number) => void;
  onDeleteTransaction: (id: number) => void;
};

function TableBody({
  data,
  headers,
  onEditTransaction,
  onDeleteTransaction,
}: Readonly<TableBodyProps>) {
  return (
    <tbody>
      {data?.map((transaction: Transaction) => (
        <tr key={transaction.id}>
          {headers.map((header) => {
            if (header.field === "actions" && header.visible) {
              return (
                <ActionsColumn
                  key={header.field}
                  transactionId={transaction.id}
                  onEditTransaction={onEditTransaction}
                  onDeleteTransaction={onDeleteTransaction}
                />
              );
            }

            if (header.field !== "actions" && header.visible) {
              return (
                <TableColumn key={header.field}>
                  {transaction[header.field]}
                </TableColumn>
              );
            }

            return null;
          })}
        </tr>
      ))}
    </tbody>
  );
}

export default TableBody;
