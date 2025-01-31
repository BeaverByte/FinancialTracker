// const TRANSACTIONS_API = "http://localhost:8080/api";

import { formSchemaType } from "../types/schemas/transactionSchema";

const fetchData = async (url: string, options = {}) => {
  try {
    const response = await fetch(url, options);

    const data = await response.json();

    if (!response.ok) {
      throw new Error(
        `Request failed with status ${response.status} and error: ${data.message}`
      );
    }

    return data;
  } catch (error) {
    console.error("Error fetching data:", error);
    throw error;
  }
};

export const fetchTransactions = async (url: string) => {
  return fetchData(url, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
  });
};

export const postTransaction = async (url: string, data: formSchemaType) => {
  return fetchData(url, {
    method: "POST",
    headers: {
      "content-type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify(data),
  });
};
