import Banner from "../../components/Banner/Banner.tsx";
import Table from "../../components/Table/Table.tsx";
import { useNavigate } from "react-router";
import { useAuth } from "../../context/AuthContext.tsx";
import { useGetTransactions } from "../../hooks/useGetTransactions.ts";

export default function Transactions() {
  const navigate = useNavigate();

  // const { error: authError, isLoading: isAuthLoading, isLoggedIn } = useAuth();

  const { data, error, isLoading } = useGetTransactions();

  // if (isAuthLoading || isLoading) return <p>Loading...</p>;
  if (isLoading) return <p>Loading...</p>;

  // if (authError) return <p>{authError.message}</p>;

  // if (!isLoggedIn) {
  //   return (
  //     <p>
  //       You are not logged in. <a href="/auth/login">Login here</a>
  //     </p>
  //   );
  // }

  if (error) return <p>Error getting transactions: {error.message}</p>;

  const handleEditTransaction = (id: number) => {
    console.log("Opening transaction modal for editing transaction ");
    navigate(`/transactions/edit/${id}`);
  };

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
