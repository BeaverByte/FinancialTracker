import Banner from "../../components/Banner/Banner.tsx";
import { useState } from "react";
import Table from "../../components/Table/Table.tsx";
import {
  deleteTransaction,
  UseGetTransactions,
} from "../../services/transactions.ts";
import { Transaction } from "../../types/schemas/transactionSchema.ts";
import { TRANSACTIONS_ROUTES } from "../../utility/API_ROUTES.ts";
import { AddTransactionForm } from "../../components/Form/AddTransactionForm.tsx";

const initialTransaction = {
  date: "",
  merchant: "",
  account: "",
  category: "",
  amount: "",
  note: "",
};

export default function Transactions() {
  const [transactions, setTransactions] = useState<Transaction[]>([]);

  // const [error, setError] = useState<string | null>(null);

  const { data, error, isLoading } = UseGetTransactions();

  if (isLoading) return <p>Loading...</p>;

  if (error) return <p>Error: {error.message}</p>;

  // const handleAddTransaction = async (data: FormSchemaType) => {
  //   try {
  //     const transaction = await postTransaction(
  //       TRANSACTIONS_ROUTES.POST_TRANSACTION,
  //       data
  //     );

  //     setTransactions((prevTransactions) => [...prevTransactions, transaction]);
  //     console.log("Transactions State updated");
  //   } catch (error) {
  //     setError(`${error}`);
  //   }
  // };

  // const handleUpdateTransaction = async (data: FormSchemaType, id: number) => {
  //   console.log("Updating transaction " + id);
  //   try {
  //     // const response = await putTransaction(
  //     //   TRANSACTIONS_ROUTES.PUT_TRANSACTION,
  //     //   data,
  //     //   id
  //     // );
  //     // TODO update this to instead just update visual without refetch
  //   } catch (error) {
  //     setError(`${error}`);
  //   }
  // };

  const handleDeleteTransaction = async (id: number) => {
    try {
      console.log("Deleting id " + id);

      await deleteTransaction(TRANSACTIONS_ROUTES.DELETE_TRANSACTION, id);

      setTransactions((prevData) =>
        prevData.filter((transaction) => transaction.id !== id)
      );
    } catch (error) {
      console.error("Error deleting transaction:", error);
    }
  };

  return (
    <div>
      <Banner />
      <h1>Transactions</h1>
      <Table data={data} />
      <h2>Add New Transaction</h2>
      <AddTransactionForm />
    </div>
  );
}
