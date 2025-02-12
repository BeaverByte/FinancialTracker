import { renderHook, waitFor } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { describe, expect } from "vitest"; // For mocking
import { useGetTransactions } from "../../src/hooks/useGetTransactions";

import { test } from "../../src/mocks/test-extend";
import { http, HttpResponse } from "msw";
import { API_ROUTES } from "../../src/utility/API_ROUTES";
import { worker } from "../../src/mocks/browser";
import { NetworkError } from "../../src/services/transactions";

const createQueryWrapper = () => {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        retry: false,
      },
    },
  });
  return ({ children }) => (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  );
};

function TestComponent() {
  const { data, isLoading, error } = useGetTransactions();

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error!</div>;

  return (
    <div>
      {data?.map((transaction: any) => (
        <div key={transaction.id}>{transaction.name}</div>
      ))}
    </div>
  );
}

describe("useGetTransactions", () => {
  test("Should succeed with response", async () => {
    const { result } = renderHook(() => useGetTransactions(), {
      wrapper: createQueryWrapper(),
    });

    await waitFor(() => expect(result.current.isSuccess).toBe(true));

    expect(result.current.data).toBeDefined();
  });

  test("Should initially have Loading text visible in document", async () => {
    const { result } = renderHook(() => useGetTransactions(), {
      wrapper: createQueryWrapper(),
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
      wrapper: createQueryWrapper(),
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
      wrapper: createQueryWrapper(),
    });

    await waitFor(() => expect(result.current.isError).toBe(true));

    expect(result.current.error).toBeInstanceOf(NetworkError);
  });
});
