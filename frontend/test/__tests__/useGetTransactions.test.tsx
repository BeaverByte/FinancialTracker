import { render, renderHook, screen, waitFor } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { describe, expect, vi } from "vitest"; // For mocking
import { useGetTransactions } from "../../src/hooks/useGetTransactions";
import { getTransactions } from "../../src/services/transactions";

import { http, HttpResponse } from "msw";
import { setupWorker } from "msw/browser";
import { test } from "../../src/mocks/test-extend";
// import { setupServer } from "msw/node";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 2,
    },
  },
});
// Wrapper builds QueryClient and QueryClientProvider to isolate from other tests
const wrapper = ({ children }) => (
  <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
);

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
  test("Should succeed to give response", async () => {
    const { result } = renderHook(() => useGetTransactions(), { wrapper });

    await waitFor(() => expect(result.current.isSuccess).toBe(true));

    // expect(result.current.data).toBeDefined();
  });

  test("Testing url", async () => {
    const response = await fetch("/stupid");
    const user = await response.json();
    expect(user).toBeDefined();
  });

  test("Should have Loading text visible in document", async () => {
    const { getByText } = render(
      <QueryClientProvider client={queryClient}>
        <TestComponent />
      </QueryClientProvider>
    );

    expect(getByText("Loading...")).toBeInTheDocument();
  });

  test("Should render provided transaction", async () => {
    const mockData = [{ id: 1, name: "Transaction 1" }];

    render(
      <QueryClientProvider client={queryClient}>
        <TestComponent />
      </QueryClientProvider>
    );

    await waitFor(() =>
      expect(screen.getByText("Transaction 1")).toBeInTheDocument()
    );
  });

  test("Should render error message on error", async () => {
    vi.mocked(getTransactions).mockRejectedValue(
      new Error("Failed to fetch transactions")
    );

    const { findByText } = render(
      <QueryClientProvider client={queryClient}>
        <TestComponent />
      </QueryClientProvider>
    );

    const errorElement = await findByText("Error!");
    expect(errorElement).toBeInTheDocument();
  });
});
