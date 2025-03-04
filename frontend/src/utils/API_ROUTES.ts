export const BASE_URL = import.meta.env.VITE_API_BASE_URL;

const transactionsEndpoint = `${BASE_URL}/api/transactions`;
const authEndpoint = `${BASE_URL}/api/auth`;

export const API_ROUTES = {
  TRANSACTIONS: {
    ENDPOINT: transactionsEndpoint,
    GET_TRANSACTIONS: transactionsEndpoint,
    POST_TRANSACTION: transactionsEndpoint,
    PUT_TRANSACTION: transactionsEndpoint,
    DELETE_TRANSACTION: transactionsEndpoint,
  },

  AUTH: {
    ENDPOINT: authEndpoint,
    SIGN_IN: authEndpoint + "/signin",
    SIGN_OUT: authEndpoint + "/signout",
  },
};
