import { http, HttpResponse } from "msw";
import { API_ROUTES } from "../utility/API_ROUTES";

const allTransactions = new Map();

const transactionMock = {
  id: 1234,
  amount: 2.5,
  category: "mcronalds",
};

export const handlers = [
  http.get(`${API_ROUTES.TRANSACTIONS.GET_TRANSACTIONS}`, () => {
    return HttpResponse.json(transactionMock);
  }),

  http.post(`${API_ROUTES.TRANSACTIONS.POST_TRANSACTION}`, () => {
    return HttpResponse.json(transactionMock);
  }),

  // http.get(`/stupid`, () => {
  //   // ...and respond to them using this JSON response.
  //   return HttpResponse.json({
  //     id: "c7b3d8e0-5e0b-4b0f-8b3a-3b9f4b3d3b3d",
  //     firstName: "John",
  //     lastName: "Maverick",
  //   });
  // }),

  // http.get("/transactions", () => {
  //   return HttpResponse.json(Array.from(allTransactions.values()));
  // }),

  // http.post("/transactions", async ({ request }) => {
  //   const newTransaction = await request.json();

  //   allTransactions.set(newTransaction.id, newTransaction);

  //   return HttpResponse.json(newTransaction, { status: 201 });
  // }),

  // http.delete("/transactions/:id", ({ params }) => {
  //   const { id } = params;

  //   const deletedTransaction = allTransactions.get(id);

  //   if (!deletedTransaction) {
  //     return new HttpResponse(null, { status: 404 });
  //   }

  //   allTransactions.delete(id);

  //   return HttpResponse.json(deletedTransaction);
  // }),
];
