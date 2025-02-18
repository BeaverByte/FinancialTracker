/**
 * Constants for application's routes
 * @constant
 */
export const APP_ROUTES = {
  ROOT: "/",
  CREATE_TRANSACTION: "/transactions/new",
  TRANSACTIONS_LIST: "/transactions",
  TRANSACTION: "/transactions",
  FIND_TRANSACTION: "/effectivedate",
  LOGIN: "/auth/login",
  LOGOUT: "/auth/logout",
  BACK: -1, // Wrapper for React Router to avoid navigate(-1) indirection
};
