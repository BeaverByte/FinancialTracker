import { renderHook, waitFor } from "@testing-library/react";
import { describe, expect } from "vitest"; // For mocking

import { test } from "../../../src/mocks/test-extend";

import { createTestQueryWrapper } from "../utility/TestQueryWrapper";
import { useGetTransactionById } from "@/hooks/useGetTransactionById";
import { worker } from "@/mocks/browser";
import { http, HttpResponse } from "msw";
import { API_ROUTES } from "@/utils/API_ROUTES";
import { NetworkError } from "@/services/errors";
import { fakeTransaction } from "@/mocks/handlers";

describe("useGetTransactionById", () => {
  const id = fakeTransaction.id;

  test("Should succeed with response", async () => {
    const { result } = renderHook(() => useGetTransactionById(id), {
      wrapper: createTestQueryWrapper(),
    });

    await waitFor(() => expect(result.current.isSuccess).toBe(true));

    expect(result.current.data).toBeDefined();
  });

  test("Should render error message on error", async () => {
    worker.use(
      http.get(`${API_ROUTES.TRANSACTIONS.GET_TRANSACTIONS}/${id}`, () => {
        return new HttpResponse(null, { status: 401 });
      })
    );

    const { result } = renderHook(() => useGetTransactionById(id), {
      wrapper: createTestQueryWrapper(),
    });

    console.log(result);

    await waitFor(() => expect(result.current.isError).toBe(true), {
      timeout: 10000,
    });

    expect(result.current.error).toBeDefined();
  });

  test("Should handle network error", async () => {
    worker.use(
      http.get(`${API_ROUTES.TRANSACTIONS.GET_TRANSACTIONS}/${id}`, () => {
        return HttpResponse.error();
      })
    );

    const { result } = renderHook(() => useGetTransactionById(id), {
      wrapper: createTestQueryWrapper(),
    });

    await waitFor(() => expect(result.current.isError).toBe(true));

    expect(result.current.error).toBeInstanceOf(NetworkError);
  });
});
