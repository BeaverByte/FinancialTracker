import Table from "../Table/Table";

function TransactionsTable({
  transactions,
  onDeleteTransaction,
  onChangeTransaction,
}) {
  return (
    <div>
      <Table
        data={transactions}
        onDeleteTransaction={onDeleteTransaction}
        onChangeTransaction={onChangeTransaction}
      />
    </div>
  );
}

export default TransactionsTable;
