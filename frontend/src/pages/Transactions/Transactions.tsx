import Banner from "../../components/Banner/Banner.tsx";
import { useState } from "react";
import Table from "../../components/Table/Table.tsx";
import {
  UseGetTransactions,
  UseUpdateTransaction,
} from "../../services/transactions.ts";
import { AddTransactionForm } from "../../components/Form/AddTransactionForm.tsx";
import { EditTransactionModal } from "../../components/Form/EditTransactionModal.tsx";

const initialTransaction = {
  date: "",
  merchant: "",
  account: "",
  category: "",
  amount: "",
  note: "",
};

export default function Transactions() {
  const [editingTransaction, setEditingTransaction] = useState(null);
  // const [transactions, setTransactions] = useState<Transaction[]>([]);

  const editMutation = UseUpdateTransaction();

  const handleSave = (updatedTransaction) => {
    console.log("Saving transaction to id " + updatedTransaction.id);
    editMutation.mutate({
      id: updatedTransaction.id,
      updates: updatedTransaction,
    });
    setEditingTransaction(null);
  };

  const handleEditTransaction = (transaction) => {
    console.log("Opening transaction modal for editing transaction ");
    setEditingTransaction(transaction);
  };

  const { data, error, isLoading } = UseGetTransactions();

  if (isLoading) return <p>Loading...</p>;

  if (error) return <p>Error: {error.message}</p>;

  return (
    <div>
      <Banner />
      <h1>Transactions</h1>
      {editingTransaction && (
        <EditTransactionModal
          transaction={editingTransaction}
          onClose={() => setEditingTransaction(null)}
          onSave={handleSave}
        />
      )}
      {!editingTransaction && (
        <Table data={data} onEditTransaction={handleEditTransaction} />
      )}
      <h2>Add New Transaction</h2>
      <AddTransactionForm />
    </div>
  );
}
