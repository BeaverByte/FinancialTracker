import { http, HttpResponse } from "msw";
import { API_ROUTES } from "../utils/API_ROUTES";

// const allTransactions = new Map();

export const fakeTransaction = {
  id: 1,
  amount: 2.5,
  category: "mcronalds",
};

export const handlers = [
  http.get(`${API_ROUTES.TRANSACTIONS.GET_TRANSACTIONS}`, () => {
    return HttpResponse.json(fakeTransaction);
  }),

  http.get(
    `${API_ROUTES.TRANSACTIONS.GET_TRANSACTIONS}/${fakeTransaction.id}`,
    () => {
      return HttpResponse.json(fakeTransaction);
    }
  ),

  http.post(`${API_ROUTES.TRANSACTIONS.POST_TRANSACTION}`, () => {
    return HttpResponse.json(fakeTransaction);
  }),

  http.delete(
    `${API_ROUTES.TRANSACTIONS.DELETE_TRANSACTION}/${fakeTransaction.id}`,
    () => {
      return HttpResponse.json(fakeTransaction);
    }
  ),
];
