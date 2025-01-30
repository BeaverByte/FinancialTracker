import Banner from "../../components/Banner/Banner.tsx";
import { useEffect, useState } from "react";
import Table from "../../components/Table/Table.tsx";
import { Form } from "../../components/Form/Form.tsx";
import { useForm, SubmitHandler } from "react-hook-form";
import { z } from "zod";
import { fetchTransactions } from "../../services/transactions.ts";

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

// Schema to validate incoming data against for transactions
const transactionSchema = z.object({
  date: z.date(),
  merchant: z.string(),
  account: z.string(),
  category: z.string(),
  amount: z.number(),
  note: z.string(),
});

// Describe "transactions" as array of objects
const transactionsSchema = z.array(transactionSchema);

const TRANSACTIONS_API = "http://localhost:8080/api";

export default function Transactions() {
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchTransactionsData = async () => {
      try {
        const transactions = await fetchTransactions();
        setTransactions(transactions);
      } catch (error) {
        console.error("Error in fetching transactions:", error);
      }
    };

    fetchTransactionsData();
  }, []);

  const handleAddTransaction = async (e) => {
    e.preventDefault();
    console.log("Adding transaction");
    try {
      const response = await fetch(`${TRANSACTIONS_API}/transactions`, {
        method: "POST",
        headers: {
          "content-type": "application/json",
        },
        // body: JSON.stringify(newTransaction),
      });

      if (!response.ok) {
        throw new Error(`Error adding response due to ${response}`);
      }

      const createdTransaction = await response.json();
      setTransactions((prevTransactions) => [
        ...prevTransactions,
        createdTransaction,
      ]);
    } catch (error) {
      setError("Error adding transaction" + error);
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
