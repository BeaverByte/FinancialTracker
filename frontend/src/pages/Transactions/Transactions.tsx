import Banner from "../../components/Banner/Banner.tsx";
import Table from "../../components/Table/Table.tsx";
import { UseGetTransactions } from "../../services/transactions.ts";
import { useNavigate } from "react-router";

export default function Transactions() {
  const navigate = useNavigate();

  const handleEditTransaction = (id: number) => {
    console.log("Opening transaction modal for editing transaction ");
    navigate(`/transactions/edit/${id}`);
  };

  const { data, error, isLoading } = UseGetTransactions();

  if (isLoading) return <p>Loading...</p>;

  if (error) return <p>Error: {error.message}</p>;

  return (
    <div>
      <Banner />
      <h1>Transactions</h1>
      <button onClick={() => navigate("/transactions/new")}>
        Add Transaction
      </button>
      <Table data={data} onEditTransaction={handleEditTransaction} />
    </div>
  );
}
