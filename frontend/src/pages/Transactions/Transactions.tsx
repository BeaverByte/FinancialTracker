import { useNavigate } from "@tanstack/react-router";
import Table from "../../components/Table/Table.tsx";
import { useGetTransactions } from "../../hooks/useGetTransactions.ts";
import { APP_ROUTES } from "../routes.ts";

export default function Transactions() {
  const navigate = useNavigate();

  const { data, error, isLoading } = useGetTransactions();

  if (isLoading) return <p>Loading...</p>;

  if (error) return <p>Error getting transactions: {error.message}</p>;

  const handleEditTransaction = (id: number) => {
    console.log("Opening transaction modal for editing transaction ");
    navigate({ to: `/transactions/edit/${id}` });
  };

  return (
    <div>
      <h1>Transactions</h1>
      <button onClick={() => navigate({ to: APP_ROUTES.CREATE_TRANSACTION })}>
        Add Transaction
      </button>
      <Table data={data} onEditTransaction={handleEditTransaction} />
    </div>
  );
}
