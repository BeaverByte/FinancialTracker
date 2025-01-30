const TRANSACTIONS_API = "http://localhost:8080/api";

const fetchData = async (url: string, options = {}) => {
  try {
    const response = await fetch(url, options);

    if (!response.ok) {
      throw new Error(`Request failed with status ${response.status}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching data:", error);
    throw error;
  }
};

export const fetchTransactions = async () => {
  const url = `${TRANSACTIONS_API}/transactions`;
  return fetchData(url, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
  });
};
