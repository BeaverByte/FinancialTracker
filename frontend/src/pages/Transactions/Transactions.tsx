import Banner from "../../components/Banner/Banner.tsx";
import { useEffect, useState } from "react";
import Table from "../../components/Table/Table.tsx";
import { Form } from "../../components/Form/Form.tsx";
import {
  deleteTransaction,
  fetchTransactions,
  postTransaction,
  putTransaction,
} from "../../services/transactions.ts";
import {
  FormSchemaType,
  Transaction,
} from "../../types/schemas/transactionSchema.ts";
import { TRANSACTIONS_ROUTES } from "../../utility/API_ROUTES.ts";

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
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchTransactionsData = async () => {
      try {
        console.log("Getting transactions...");
        const transactions = await fetchTransactions(
          TRANSACTIONS_ROUTES.GET_TRANSACTIONS
        );
        setTransactions(transactions);
      } catch (error) {
        setError(`${error}`);
      }
    };

    fetchTransactionsData();
  }, []);

  const handleAddTransaction = async (data: FormSchemaType) => {
    try {
      const transaction = await postTransaction(
        TRANSACTIONS_ROUTES.POST_TRANSACTION,
        data
      );

      setTransactions((prevTransactions) => [...prevTransactions, transaction]);
      console.log("Transactions State updated");
    } catch (error) {
      setError(`${error}`);
    }
  };

  const handleUpdateTransaction = async (data: FormSchemaType, id: number) => {
    console.log("Updating transaction " + id);
    try {
      // const response = await putTransaction(
      //   TRANSACTIONS_ROUTES.PUT_TRANSACTION,
      //   data,
      //   id
      // );
      // TODO update this to instead just update visual without refetch
    } catch (error) {
      setError(`${error}`);
    }
  };

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
      {error && <p>{error}</p>}
      <Banner />
      <h1>Transactions</h1>
      <Table
        data={transactions}
        onDeleteTransaction={handleDeleteTransaction}
        onChangeTransaction={handleUpdateTransaction}
      />
      <h2>Add New Transaction</h2>
      <Form onSubmit={handleAddTransaction} />
    </div>
  );
}
