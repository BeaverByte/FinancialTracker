import Banner from "../../components/Banner/Banner.tsx";
import { useEffect, useState } from "react";
import Table from "../../components/Table/Table.tsx";
import { Form } from "../../components/Form/Form.tsx";
import { useForm, SubmitHandler } from "react-hook-form";
import { z } from "zod";

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
  const [error, setError] = useState(null);

  useEffect(() => {
    // Fetch Transactions from API
    const fetchTransactions = async () => {
      try {
        const response = await fetch(`${TRANSACTIONS_API}/transactions`, {
          method: "GET",
          // mode: "no-cors",
          credentials: "include",
          headers: {
            // authorization: `Basic ${encodedAuth}`,
            "content-type": "application/json",
          },
        });
        const result = await response.json();
        setTransactions(result); // Store the fetched data in the state
        console.log(data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    fetchTransactions();
  }, []);

  // Handle adding a new transaction
  const handleAddTransaction = async (e) => {
    e.preventDefault();
    console.log("Adding transaction");
    try {
      const response = await fetch(`${TRANSACTIONS_API}/transactions`, {
        method: "POST",
        headers: {
          // authorization: `Basic ${encodedAuth}`,
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
    } catch (err) {
      setError("Error adding transaction");
    }
  };

  const handleUpdateTransaction = async (id) => {
    // Implement the update logic here (e.g., open a modal or form to edit the transaction)
    console.log("Updating transaction " + id);
    try {
      const response = await fetch(`${TRANSACTIONS_API}/transactions/${id}`, {
        method: "PUT",
        headers: {
          // authorization: `Basic ${encodedAuth}`,
          "content-type": "application/json",
        },
        // body: JSON.stringify(),
      });

      if (!response.ok) throw new Error("Failed to update transaction");

      // Optionally, refetch the transactions or update state
      // fetchTransactions();
    } catch (error) {
      console.error("Error updating transaction:", error);
    }
  };

  const handleDeleteTransaction = async (id) => {
    // Implement the delete logic here
    try {
      console.log("Deleting id " + id);
      const response = await fetch(`${TRANSACTIONS_API}/transactions/${id}`, {
        method: "DELETE",
        headers: {
          // authorization: `Basic ${encodedAuth}`,
          "content-type": "application/json",
        },
      });
      if (!response.ok) throw new Error("Failed to delete transaction");
      // Remove the deleted transaction from the state without refetching all data
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

const getTransactions = async () => {
  try {
    const response = await fetch(`${TRANSACTIONS_API}/transactions`, {
      method: "GET",
      headers: {
        authorization: `Basic ${encodedAuth}`,
        "content-type": "application/json",
      },
    });
    const result = await response.json();

    // Return the result if everything is fine
    return { success: true, result };
  } catch (error) {
    // Handle the error and return a response with success: false
    console.error("Error fetching transactions:", error);
    return { success: false, error };
  }
};
