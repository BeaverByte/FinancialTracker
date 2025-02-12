import { render, renderHook, screen, waitFor } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { describe, expect, vi } from "vitest"; // For mocking
import { getTransactions } from "../../src/services/transactions";

import { test } from "../../src/mocks/test-extend";
import { useAddTransaction } from "../../src/hooks/useAddTransaction";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 5,
    },
  },
});
// Wrapper builds QueryClient and QueryClientProvider to isolate from other tests
const wrapper = ({ children }) => (
  <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
);

function TestComponent() {
  const transactions = useAddTransaction();

  return (
    <div>
      {data?.map((transaction: any) => (
        <div key={transaction.id}>{transaction.name}</div>
      ))}
    </div>
  );
}

describe("useAddTransaction", () => {
  test("Should succeed to give response", async () => {
    const { result } = renderHook(() => useAddTransaction(), { wrapper });
    const mockTransaction = {
      date: "2025-02-11",
      merchant: "Amazon",
      account: "Credit Card - Chase",
      category: "Electronics",
      amount: 120.99,
      note: "Purchased a new pair of headphones",
    };

    result.current.mutate(mockTransaction);

    await waitFor(() => expect(result.current.isSuccess).toBe(true));

    expect(result.current.data).toBeDefined();
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
