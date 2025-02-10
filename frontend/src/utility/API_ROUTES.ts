export const BASE_URL = import.meta.env.VITE_API_BASE_URL;

const transactionsEndpoint = `${BASE_URL}/api/transactions`;
export const TRANSACTIONS_ROUTES = {
  ENDPOINT: transactionsEndpoint,
  GET_TRANSACTIONS: transactionsEndpoint,
  POST_TRANSACTION: transactionsEndpoint,
  PUT_TRANSACTION: transactionsEndpoint,
  DELETE_TRANSACTION: transactionsEndpoint,
};
