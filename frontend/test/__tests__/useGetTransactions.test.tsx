import { renderHook, waitFor } from "@testing-library/react";
import { describe, expect } from "vitest"; // For mocking
import { useGetTransactions } from "../../src/hooks/useGetTransactions";

import { test } from "../../src/mocks/test-extend";
import { http, HttpResponse } from "msw";
import { API_ROUTES } from "../../src/utils/API_ROUTES";
import { worker } from "../../src/mocks/browser";
import { NetworkError } from "../../src/services/errors";
import { createTestQueryWrapper } from "./utility/TestQueryWrapper";

describe("useGetTransactions", () => {
  test("Should succeed with response", async () => {
    const { result } = renderHook(() => useGetTransactions(), {
      wrapper: createTestQueryWrapper(),
    });

    await waitFor(() => expect(result.current.isSuccess).toBe(true));

    expect(result.current.data).toBeDefined();
  });

  test("Should initially have Loading text visible in document", async () => {
    const { result } = renderHook(() => useGetTransactions(), {
      wrapper: createTestQueryWrapper(),
    });

    await waitFor(() => expect(result.current.isLoading).toBe(true));
  });

  test("Should render error message on error", async () => {
    worker.use(
      http.get(`${API_ROUTES.TRANSACTIONS.GET_TRANSACTIONS}`, () => {
        return new HttpResponse(null, { status: 401 });
      })
    );

    const { result } = renderHook(() => useGetTransactions(), {
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
      http.get(`${API_ROUTES.TRANSACTIONS.GET_TRANSACTIONS}`, () => {
        return HttpResponse.error();
      })
    );

    const { result } = renderHook(() => useGetTransactions(), {
      wrapper: createTestQueryWrapper(),
    });

    await waitFor(() => expect(result.current.isError).toBe(true));

    expect(result.current.error).toBeInstanceOf(NetworkError);
  });
});
