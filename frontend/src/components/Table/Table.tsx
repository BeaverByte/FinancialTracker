import { Transaction } from "../../pages/Transactions/Transactions";

const defaultHeaders = [
  "Date",
  "Merchant",
  "Account",
  "Category",
  "Amount",
  "Note",
];

// Provide type safety for Table Props
type TableProps = {
  data: Transaction[];
  headers?: Headers;
  onChangeTransaction: (id: number) => Promise<void>;
  onDeleteTransaction: (id: number) => Promise<void>;
};

type Headers = string[];

/**
 *
 * @param data Data that will fill table, an object corresponding to a row
 * @param [headers] Optional - Headers row that describe respective columns. If not provided, default Headers will inject
 * @returns { JSX.Element }
 */
export default function Table({
  data,
  headers,
  onChangeTransaction,
  onDeleteTransaction,
}: Readonly<TableProps>): JSX.Element {
  return (
    <div>
      <table>
        <thead>
          <TableHeader headers={headers} />
        </thead>
        <tbody>
          {data?.map((transaction: Transaction) => (
            <tr key={transaction.id}>
              <td>{transaction.date}</td>
              <td>{transaction.merchant}</td>
              <td>{transaction.account}</td>
              <td>{transaction.category}</td>
              <td>{transaction.amount}</td>
              <td>
                {transaction.note}
                {transaction.id}
              </td>
              <td>
                <button onClick={() => onDeleteTransaction(transaction.id)}>
                  Delete
                </button>
              </td>
              <td>
                <button onClick={() => onChangeTransaction(transaction.id)}>
                  Edit
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function TableHeader({ headers = defaultHeaders }) {
  const headersExist = headers && headers.length > 0;

  // Render a row per header
  const renderHeaderColumns = () => {
    return headers.map((header, index) => <th key={index}>{header}</th>);
  };

  return (
    <tr>{headersExist ? renderHeaderColumns() : <th>No data provided</th>}</tr>
  );
}
