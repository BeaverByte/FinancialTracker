import Banner from "../../components/Banner/Banner.tsx";
import { useEffect, useState } from "react";
import Table from "../../components/Table/Table.tsx";
import { Form } from "../../components/Form/Form.tsx";
import {
  fetchTransactions,
  postTransaction,
} from "../../services/transactions.ts";
import { formSchemaType } from "../../types/schemas/transactionSchema.ts";
import { TRANSACTIONS_ROUTES } from "../../utility/API_ROUTES.ts";

export type Transaction = {
  id: number;
  date: string;
  merchant: string;
  account: string;
  category: string;
  amount: number;
  note: string;
};

const initialTransaction = {
  date: "",
  merchant: "",
  account: "",
  category: "",
  amount: "",
  note: "",
};

const TRANSACTIONS_API = "http://localhost:8080/api";

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

  const handleAddTransaction = async (data: formSchemaType) => {
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

  const handleUpdateTransaction = async (id: number) => {
    console.log("Updating transaction " + id);
    try {
      const response = await fetch(`${TRANSACTIONS_API}/transactions/${id}`, {
        method: "PUT",
        headers: {
          "content-type": "application/json",
        },
        // body: JSON.stringify(),
      });

      if (!response.ok) throw new Error("Failed to update transaction");

      // TODO update this to instead just update visual without refetch
      fetchTransactions();
    } catch (error) {
      console.error("Error updating transaction:", error);
    }
  };

  const handleDeleteTransaction = async (id: number) => {
    try {
      console.log("Deleting id " + id);
      const response = await fetch(`${TRANSACTIONS_API}/transactions/${id}`, {
        method: "DELETE",
        headers: {
          "content-type": "application/json",
        },
        credentials: "include",
      });
      if (!response.ok) throw new Error("Failed to delete transaction");
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
