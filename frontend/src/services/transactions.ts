import { Transaction } from "../pages/Transactions/Transactions";

const TRANSACTIONS_API = "http://localhost:8080/api";

// export const transactionsApi = createApi({
//   reducerPath: "transactionsApi",
//   baseQuery: fetchBaseQuery({ baseUrl: TRANSACTIONS_API }),
//   endpoints: (builder) => ({
//     getTransactionById: builder.query<Transaction, number>({
//       query: (id) => `/transactions/${id}`,
//     }),
//   }),
// });

// Export hooks for usage in functional components, which are
// auto-generated based on the defined endpoints
// export const { useGetTransactionByIdQuery } = transactionsApi;
